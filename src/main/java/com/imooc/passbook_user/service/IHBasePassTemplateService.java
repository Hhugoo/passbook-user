package com.imooc.passbook_user.service;

import com.imooc.passbook_user.vo.PassTemplate;

/**
 * Pass HBase 服务
 */
public interface IHBasePassTemplateService {

    /**
     * 将PassTemplate 写入HBase
     * @param passTemplate {@link PassTemplate}
     * @return
     */
    boolean dropPassTemplateToHBase(PassTemplate passTemplate);

}
