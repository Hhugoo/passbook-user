package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.Feedback;
import com.imooc.passbook_user.vo.Response;

/**
 * 评论相关功能实现
 */
public interface IFeedbackService {

    /**
     * 创建评论
     * @param feedback {@link Feedback}
     * @return {@link Response}
     */
    Response createFeedback(Feedback feedback);

    /**
     * 获取用户评论
     * @param userId
     * @return {@link Response}
     */
    Response getFeedback(Long userId);

}
