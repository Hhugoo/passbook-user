package com.imooc.passbook_user.controller;

import com.imooc.passbook_user.log.LogConstants;
import com.imooc.passbook_user.log.LogGenerator;
import com.imooc.passbook_user.service.IUserService;
import com.imooc.passbook_user.vo.Response;
import com.imooc.passbook_user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建用户服务
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class CreateUserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 创建用户
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/createuser")
    Response createUser(@RequestBody User user) throws Exception{
        LogGenerator.genLog(
                httpServletRequest,
                -1l,
                LogConstants.ActionName.CREATE_USER,
                user
        );

        return iUserService.createUser(user);
    }
}
