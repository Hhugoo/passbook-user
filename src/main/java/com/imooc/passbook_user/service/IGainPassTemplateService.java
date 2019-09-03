package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.GainPassTemplateRequest;
import com.imooc.passbook_user.vo.Response;

/**
 * 用户领取优惠券功能实现
 */
public interface IGainPassTemplateService {

    /**
     * 用户领取优惠券
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     * @throws Exception
     */
    Response gainPassTemplate(GainPassTemplateRequest request) throws Exception;

}
