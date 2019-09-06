package com.imooc.passbook_user.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.passbook_user.constant.Constants;
import com.imooc.passbook_user.constant.PassStatus;
import com.imooc.passbook_user.dao.MerchantsDao;
import com.imooc.passbook_user.entity.Merchants;
import com.imooc.passbook_user.mapper.PassRowMapper;
import com.imooc.passbook_user.service.IUserPassService;
import com.imooc.passbook_user.vo.Pass;
import com.imooc.passbook_user.vo.PassInfo;
import com.imooc.passbook_user.vo.PassTemplate;
import com.imooc.passbook_user.vo.PassTemplate.BaseInfo;
import com.imooc.passbook_user.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户优惠券相关功能实现
 */
@Slf4j
@Service
public class UserPassServiceImpl implements IUserPassService{

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Autowired
    private MerchantsDao merchantsDao;

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.ALL);
    }

    @Override
    public Response userUsePass(Pass pass) {

        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString());
        Scan scan = new Scan();
        List<Filter> filterList = new ArrayList<>();
        filterList.add(new PrefixFilter(rowPrefix));
        filterList.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.TEMPLATE_ID.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(pass.getTemplateId())
        ));
        filterList.add(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY_I.getBytes(),
                Constants.PassTable.CON_DATE.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));

        scan.setFilter(new FilterList(filterList));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());

        if (null == pass || passes.size() != 1) {
           log.error("UserUsePass Error: {}", JSON.toJSONString(pass));
           return Response.failure("UserUsePass Error");
        }

        byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] CON_DATE = Constants.PassTable.CON_DATE.getBytes();

        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(FAMILY_I, CON_DATE,
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()))
        );
        datas.add(put);

        hbaseTemplate.saveOrUpdate(Constants.PassTable.TABLE_NAME, datas.get(0));

        return Response.success();
    }

    /**
     * 根据优惠券状态获取优惠券信息
     * @param userId
     * @param status
     * @return
     * @throws Exception
     */
    private Response getPassInfoByStatus(Long userId, PassStatus status) throws Exception {

        // 根据userId 构造行键前缀
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());

        CompareFilter.CompareOp compareOp =
                status == PassStatus.ALL ?
                        CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

        Scan scan = new Scan();

        List<Filter> filterList = new ArrayList<>();

        // 行键前缀过滤器，找到特定用户的优惠券
        filterList.add(new PrefixFilter(rowPrefix));
        // 基于列单元值的过滤器，找到未使用的优惠券
        if (status != PassStatus.ALL) {
            filterList.add(new SingleColumnValueFilter(
                    Constants.PassTable.FAMILY_I.getBytes(),
                    Constants.PassTable.CON_DATE.getBytes(), compareOp,
                    Bytes.toBytes("-1")
            ));
        }

        scan.setFilter(new FilterList(filterList));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(
                new ArrayList<>(passTemplateMap.values()));

        List<PassInfo> result = new ArrayList<>();

        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(
                    pass.getTemplateId(), null
            );
            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchants = merchantsMap.getOrDefault(passTemplate.getBaseInfo().getId(), null);
            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getBaseInfo().getId());
                continue;
            }

            result.add(new PassInfo(pass, passTemplate, merchants));
        }

        return new Response(result);
    }

    /**
     * 通过获取的 pass 对象构造map
     * @param passes
     * @return
     * @throws Exception
     */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws Exception {

        String[] patterns = new String[] {"yyyy-MM-dd"};

        byte[] FAMILY_B = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(Constants.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(Constants.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(Constants.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(Constants.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(Constants.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(Constants.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(Constants.PassTemplateTable.END);

        List<String> templateIds = passes.stream().map(
                Pass::getTemplateId
        ).collect(Collectors.toList());

        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));

        Result[] templateResults = hbaseTemplate.getConnection()
                .getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME))
                .get(templateGets);

        // 构造PassTemplateId -> Passtemplate Object 的Map， 用于构造 PassInfo
        Map<String, PassTemplate> templateId2Object = new HashMap<>();
        for (Result item : templateResults) {
            PassTemplate passTemplate = new PassTemplate();

            passTemplate.setBaseInfo(
                    new BaseInfo(
                        Bytes.toInt(item.getValue(FAMILY_B, ID)),
                        Bytes.toString(item.getValue(FAMILY_B, TITLE)),
                        Bytes.toString(item.getValue(FAMILY_B, SUMMARY)),
                        Bytes.toString(item.getValue(FAMILY_B, DESC)),
                        Bytes.toBoolean(item.getValue(FAMILY_B, HAS_TOKEN)),
                        Bytes.toInt(item.getValue(FAMILY_B, BACKGROUND))
                    )
            );

            passTemplate.setConstraint(
                    new PassTemplate.Constraint(
                            Bytes.toLong(item.getValue(FAMILY_C, LIMIT)),
                            DateUtils.parseDate(Bytes.toString(item.getValue(FAMILY_C, START)), patterns),
                            DateUtils.parseDate(Bytes.toString(item.getValue(FAMILY_C, END)), patterns)
                    )
            );

            templateId2Object.put(Bytes.toString(item.getRow()), passTemplate);
        }

        return templateId2Object;
    }

    /**
     * 通过获取的Passtemplate 对象构造Merchants Map
     * @param passTemplates {@link PassTemplate}
     * @return
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate> passTemplates) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<PassTemplate.BaseInfo> passTemplatebaseInfo = passTemplates.stream().map(
                PassTemplate::getBaseInfo
        ).collect(Collectors.toList());

        List<Integer> merchantsIds = passTemplatebaseInfo.stream().map(
                PassTemplate.BaseInfo::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIdIn(merchantsIds);

        merchants.forEach(m -> merchantsMap.put(m.getId(), m));

        return merchantsMap;
    }
}
