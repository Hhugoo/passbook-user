package com.imooc.passbook_user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户领取的优惠券
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pass {

    private Long userId;

    /** pass 在HBase 中的RowKey */
    private String rowKey;

    /** PassTemplate 在Hbase 中的RowKey */
    private String templateId;

    /** 优惠券 token, 有可能是null,则填充-1  */
    private String token;

    private Date assignedDate;

    /** 优惠券使用日期，不为空则表明以及被消费 */
    private Date conDate;


}
