package com.imooc.passbook_user.controller;

import com.imooc.passbook_user.log.LogConstants;
import com.imooc.passbook_user.log.LogGenerator;
import com.imooc.passbook_user.service.IFeedbackService;
import com.imooc.passbook_user.service.IGainPassTemplateService;
import com.imooc.passbook_user.service.IInventoryService;
import com.imooc.passbook_user.service.IUserPassService;
import com.imooc.passbook_user.vo.Feedback;
import com.imooc.passbook_user.vo.GainPassTemplateRequest;
import com.imooc.passbook_user.vo.Pass;
import com.imooc.passbook_user.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Passbook Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassbookController {

    /** 用户优惠券服务 */
    @Autowired
    private IUserPassService iUserPassService;

    /** 优惠券库存服务 */
    @Autowired
    private IInventoryService iInventoryService;

    /** 领取优惠券服务 */
    @Autowired
    private IGainPassTemplateService iGainPassTemplateService;

    /** 用户反馈功能 */
    @Autowired
    private IFeedbackService iFeedbackService;

    /** HttpServletRequest */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 获取用户个人的优惠券信息
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/userpassinfo")
    Response userPassInfo(Long userId) throws Exception {
        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.USER_PASS_INFO,
                null
        );

        return iUserPassService.getUserPassInfo(userId);
    }

    /**
     * 获取用户使用了的优惠券信息
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/userusedpassinfo")
    Response userUsedPassInfo(Long userId) throws Exception {
        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.USER_USED_PASS_INFO,
                null
        );

        return iUserPassService.getUserUsedPassInfo(userId);
    }

    /**
     * 用户使用优惠券
     * @param pass
     * @return
     */
    @ResponseBody
    @PostMapping("/userusepass")
    Response userUsePass(@RequestBody Pass pass) {
        LogGenerator.genLog(
                httpServletRequest,
                pass.getUserId(),
                LogConstants.ActionName.USER_USE_PASS,
                pass
        );

        return iUserPassService.userUsePass(pass);
    }

    /**
     * 获取库存信息
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/inventoryinfo")
    Response inventoryInfo(Long userId) throws Exception {
        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.INVENTORY_INFO,
                null
        );

        return iInventoryService.getInventoryInfo(userId);
    }

    /**
     * 用户领取优惠券
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/gainpasstemplate")
    Response gainPassTemplate(@RequestBody GainPassTemplateRequest request) throws Exception {
        LogGenerator.genLog(
                httpServletRequest,
                request.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE,
                request
        );

        return iGainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * 用户创建评论
     * @param feedback
     * @return
     */
    @ResponseBody
    @PostMapping("/createfeedback")
    Response createFeedback(@RequestBody Feedback feedback) {
        LogGenerator.genLog(
                httpServletRequest,
                feedback.getUserId(),
                LogConstants.ActionName.CREATE_FEEDBACK,
                feedback
        );

        return iFeedbackService.createFeedback(feedback);
    }

    /**
     * 用户获取评论信息
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/getfeedback")
    Response getFeedback(Long userId) {
        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.GET_FEEDBACK,
                null
        );

        return iFeedbackService.getFeedback(userId);
    }

    /**
     * 异常演示接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("Welcome to imooc!");
    }

}
