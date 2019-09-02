package com.imooc.passbook_user.vo;

import com.google.common.base.Enums;
import com.imooc.passbook_user.constant.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评论表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    private Long userId;

    /** 评论类型 */
    private String type;

    /** PassTemplate 在Hbase 中的RowKey， 如果是app类型的评论，则没有 */
    private String templateId;

    /** 评论内容 */
    private String comment;

    public boolean validate() {

        FeedbackType feedbackType = Enums.getIfPresent(
                FeedbackType.class, type.toUpperCase()
        ).orNull();

        return !(null == feedbackType || null == comment);
    }

}
