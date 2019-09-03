package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.Response;
import com.imooc.passbook_user.vo.User;

/**
 * 用户服务, 创建user服务
 *
 */
public interface IUserService {

    /**
     * 创建用户
     * @param user {@link User }
     * @return {@link Response}
     * @throws Exception
     */
    Response createUser(User user) throws Exception;

}
