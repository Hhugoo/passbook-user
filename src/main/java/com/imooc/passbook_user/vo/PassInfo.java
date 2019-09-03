package com.imooc.passbook_user.vo;

import com.imooc.passbook_user.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取的优惠券信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfo {

    /** 优惠券 */
    private Pass pass;

    /** 优惠券模版 */
    private PassTemplate passTemplate;

    /** 优惠券对应的商户 */
    private Merchants merchants;

}
