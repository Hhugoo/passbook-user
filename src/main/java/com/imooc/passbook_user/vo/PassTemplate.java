package com.imooc.passbook_user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 投放的优惠券对象定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplate {

    private BaseInfo baseInfo;

    private Constraint constraint;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseInfo {
        private Integer id;
        private String title;
        private String summary;
        private String desc;
        private Boolean hasToken;
        private Integer background;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Constraint {
        private Long limit;
        private Date start;
        private Date end;
    }


}
