package com.imooc.passbook_user.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志生成器
 */

@Slf4j
public class LogGenerator {

    public static void genLog(HttpServletRequest request, Long userId, String action, String info) {
        log.info(
                JSON.toJSONString(
                        new LogObject(action, userId, System.currentTimeMillis(), request.getRemoteUser(), info)
                )
        );
    }

}
