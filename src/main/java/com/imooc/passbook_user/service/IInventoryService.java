package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.Response;

/**
 * 获取库存信息
 * 只返回用户没有领取的，即优惠券库存功能实现接口定义
 */
public interface IInventoryService {

    /**
     * 获取库存信息
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    Response getInventoryInfo(Long userId) throws Exception;

}
