package com.imooc.passbook_user.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogObject {

    /** 日志动作类型 */
    private String action;

    private Long userId;

    private Long timestamp;

    /** 客户端 ip 地址 */
    private String remoteIp;

    /** 日志信息 */
    private Object info = null;

}
