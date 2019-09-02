package com.imooc.passbook_user.constant;

/**
 * 常量定义
 */
public class Constants {

    /**商户优惠券投放的kafka Topic */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /** token文件存储目录 */
    public static final String TOKEN_DIR= "/tmp/token/";

    /** 已使用的 token 文件名后缀 */
    public static final String USED_TOKEN_SUFFIX = "_";

    /** 用户数的redis key */
    public static final String USE_COUNT_REDIS_KEY = "imooc-user-count";

    /**
     * User HBase Table
     */
    public class UserTable {

        /** User HBase 表名 */
        public static final String TABLE_NAME = "pb:user";

        /** 基本信息列族  basic*/
        public static final String FAMILY_B = "b";

        public static final String NAME = "name";

        public static final String AGE = "age";

        public static final String SEX = "sex";

        /** 额外信息列族 other*/
        public static final String FAMILY_O = "o";

        public static final String PHONE = "phone";

        public static final String ADDRESS = "address";

    }

    /**
     * PassTemplate HBase Table
     */
    public class PassTemplateTable {
        /** PassTemplate HBase 表名 */
        public static final String TABLE_NAME = "pb:passtemplate";

        /** 基本信息列族 */
        public static final String FAMILY_B = "b";

        public static final String ID = "id";

        /** 优惠券的标题 */
        public static final String TITLE = "title";

        /** 优惠券摘要 */
        public static final String SUMMARY = "summary";

        public static final String DESC = "desc";

        /** 优惠券是否有token */
        public static final String HAS_TOKEN = "has_token";

        public static final String BACKGROUND = "backgroud";

        /** 约束信息列族 constraint*/
        public static final String FAMILY_C = "c";

        public static final String LIMIT = "limit";

        public static final String START = "start";

        public static final String END = "end";
    }

    /**
     * Pass HBase Table
     */
    public class PassTable {

        /** Pass HBase 表名 */
        public static final String TABLE_NAME = "pb:pass";

        /** 信息列族 information*/
        public static final String FAMILY_I = "i";

        public static final String USER_ID = "user_id";

        /** 优惠券id */
        public static final String TEMPLATE_ID = "template_id";

        /** 优惠券识别码 */
        public static final String TOKEN = "token";

        /** 领取日期 */
        public static final String ASSIGNED_DATE = "assigned_date";

        /** 消费日期 */
        public static final String CON_DATE = "con_date";

    }

    /**
     * Feedback HBase Table
     */
    public class Feedback {

        /** Feedback HBase 表名 */
        public static final String TABLE_NAME = "pb:feedback";

        /** 信息列族 */
        public static final String FAMILY_I = "i";

        public static final String USER_ID = "user_id";

        /** 评论类型 */
        public static final String TYPE = "type";

        /** PassTemplate RowKey, 如果是 app 评论， 则是-1 */
        public static final String TEMPLATE_ID = "template_id";

        /** 评论内容 */
        public static final String COMMIT = "commit";

    }
 }
