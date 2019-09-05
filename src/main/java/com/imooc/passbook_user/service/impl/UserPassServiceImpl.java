package com.imooc.passbook_user.service.impl;

import com.imooc.passbook_user.constant.Constants;
import com.imooc.passbook_user.dao.MerchantsDao;
import com.imooc.passbook_user.entity.Merchants;
import com.imooc.passbook_user.service.IUserPassService;
import com.imooc.passbook_user.vo.Pass;
import com.imooc.passbook_user.vo.PassTemplate;
import com.imooc.passbook_user.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户优惠券相关功能实现
 */
public class UserPassServiceImpl implements IUserPassService{

    @Autowired
    private HbaseTemplate hbaseTemplate;
    @Autowired
    private MerchantsDao merchantsDao;

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public Response userUsePass(Pass pass) {
        return null;
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
                    new PassTemplate.BaseInfo(
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
     * @param passTemplateBaseInfo {@link PassTemplate.BaseInfo}
     * @return
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate.BaseInfo> passTemplateBaseInfo) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = passTemplateBaseInfo.stream().map(
                PassTemplate.BaseInfo::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIdIn(merchantsIds);

        merchants.forEach(m -> merchantsMap.put(m.getId(), m));

        return merchantsMap;
    }
}
