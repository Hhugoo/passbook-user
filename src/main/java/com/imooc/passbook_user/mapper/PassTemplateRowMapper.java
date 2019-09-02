package com.imooc.passbook_user.mapper;

import com.imooc.passbook_user.constant.Constants;
import com.imooc.passbook_user.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * HBase PassTemplate Row To PassTemplate Object
 */
public class PassTemplateRowMapper implements RowMapper<PassTemplate>{

    private static byte[] FAMILY_B = Constants.PassTemplateTable.FAMILY_B.getBytes();
    private static byte[] ID = Constants.PassTemplateTable.ID.getBytes();
    private static byte[] TITLE = Constants.PassTemplateTable.TITLE.getBytes();
    private static byte[] SUMMARY = Constants.PassTemplateTable.SUMMARY.getBytes();
    private static byte[] DESC = Constants.PassTemplateTable.DESC.getBytes();
    private static byte[] HAS_TOKEN = Constants.PassTemplateTable.HAS_TOKEN.getBytes();
    private static byte[] BACKGROUND = Constants.PassTemplateTable.BACKGROUND.getBytes();

    private static byte[] FAMILY_C = Constants.PassTemplateTable.FAMILY_C.getBytes();
    private static byte[] LIMIT = Constants.PassTemplateTable.LIMIT.getBytes();
    private static byte[] START = Constants.PassTemplateTable.START.getBytes();
    private static byte[] END = Constants.PassTemplateTable.END.getBytes();

    @Override
    public PassTemplate mapRow(Result result, int i) throws Exception {

        PassTemplate.BaseInfo baseInfo = new PassTemplate.BaseInfo(
                Bytes.toInt(result.getValue(FAMILY_B, ID)),
                Bytes.toString(result.getValue(FAMILY_B, TITLE)),
                Bytes.toString(result.getValue(FAMILY_B, SUMMARY)),
                Bytes.toString(result.getValue(FAMILY_B, DESC)),
                Bytes.toBoolean(result.getValue(FAMILY_B, HAS_TOKEN)),
                Bytes.toInt(result.getValue(FAMILY_B, BACKGROUND))
        );

        String[] patterns = new String[] {"yyyy-MM-dd"};

        PassTemplate.Constraint constraint = new PassTemplate.Constraint(
                Bytes.toLong(result.getValue(FAMILY_C, LIMIT)),
                DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, START)), patterns),
                DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, END)), patterns)
        );

        return new PassTemplate(
                baseInfo, constraint
        );
    }
}
