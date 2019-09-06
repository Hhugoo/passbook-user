package com.imooc.passbook_user.service.impl;

import com.imooc.passbook_user.constant.Constants;
import com.imooc.passbook_user.mapper.PassTemplateRowMapper;
import com.imooc.passbook_user.service.IInventoryService;
import com.imooc.passbook_user.utils.RowKeyGenUtil;
import com.imooc.passbook_user.vo.PassTemplate;
import com.imooc.passbook_user.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 获取库存信息，只返回用户没有领取的
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService{

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public Response getInventoryInfo(Long userId) throws Exception {
        return null;
    }

    /**
     * 获取系统中可用的优惠券
     * @param excludeIds 需要排除的优惠券id
     * @return
     */
    private List<PassTemplate> getAvailablePassTemplate(List<String> excludeIds) {

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(0L)
                )
        );
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.EQUAL,
                        Bytes.toBytes("-1")
                )
        );

        Scan scan = new Scan();
        scan.setFilter(filterList);

        List<PassTemplate> vaildTemplates = hbaseTemplate.find(
                Constants.PassTemplateTable.TABLE_NAME, scan, new PassTemplateRowMapper()
        );
        List<PassTemplate> availablePassTemplates = new ArrayList<>();

        Date cur = new Date();

        for(PassTemplate item : vaildTemplates) {

            if (excludeIds.contains(RowKeyGenUtil.genPassTemplateRowKey(item))) {
                continue;
            }

            if(cur.getTime() >= item.getConstraint().getStart().getTime()
                    && cur.getTime() <= item.getConstraint().getEnd().getTime()) {
                availablePassTemplates.add(item);
            }
        }

        return availablePassTemplates;
    }
}
