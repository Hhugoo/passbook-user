package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.Pass;
import com.imooc.passbook_user.vo.Response;

/**
 * 获取用户个人优惠券信息
 */
public interface IUserPassService {

    /**
     * 获取用户个人优惠券信息
     * 即我的优惠券
     * @param userId
     * @return
     * @throws Exception
     */
    Response getUserPassInfo(Long userId) throws Exception;

    /**
     * 获取用户以及消费了的优惠券， 即已使用优惠券功能实现
     * @param userId
     * @return
     * @throws Exception
     */
    Response getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * 获取用户所有的优惠券信息（使用的+未使用的）
     * 优惠券记录
     * @param userId
     * @return
     * @throws Exception
     */
    Response getUserAllPassInfo(Long userId) throws Exception;

    /**
     * 用户使用优惠券
     * @param pass {@link Pass}
     * @return
     */
    Response userUsePass(Pass pass);

}
