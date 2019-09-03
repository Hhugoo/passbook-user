package com.imooc.passbook_user.utils;

import com.imooc.passbook_user.vo.Feedback;
import com.imooc.passbook_user.vo.GainPassTemplateRequest;
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
     * 根据提供的领取优惠券请求生成RowKey，只可以在领取优惠券的时候使用
     * pass RowKey = reversed(userId) + inverse(timestamp) + PassTemplate RowKey
     * inverse: Long.Max_value - System.currentMill();
     * @param request
     * @return
     */
    public static String genPassRowKey(GainPassTemplateRequest request) {
        return new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis())
                + genPassTemplateRowKey(request.getPassTemplate());
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
