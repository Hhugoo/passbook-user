package com.imooc.passbook_user.utils;

import com.imooc.passbook_user.vo.Feedback;
import com.imooc.passbook_user.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * RowKey 生成器工具类
 */
@Slf4j
public class RowKeyGenUtil {

    /**
     * 根据提供的 passTemplate 对象生成RowKey
     * @param passTemplate {@link PassTemplate}
     * @return String rowKey
     */
    public static String genPassTemplateRowKey(PassTemplate passTemplate) {

        String passInfo = String.valueOf(passTemplate.getBaseInfo().getId()) + "_" + passTemplate.getBaseInfo().getTitle();
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("GenPassTemplateRowKey: {}, {}", passInfo, rowKey);

        return rowKey;

    }

    /**
     * 根据feedback构造 RowKey
     * @param feedback {@link Feedback}
     * @return
     */
    public static String genFeedbackRowKey(Feedback feedback) {

        return new StringBuilder(String.valueOf(feedback.getUserId())).reverse().toString() +
                (Long.MAX_VALUE - System.currentTimeMillis());
    }

}
