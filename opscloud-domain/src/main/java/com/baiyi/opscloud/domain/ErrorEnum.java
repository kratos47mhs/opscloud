package com.baiyi.opscloud.domain;

import lombok.Getter;


@Getter
public enum ErrorEnum {
    OK(0, "成功", 1),

    // ----------------------- 系统级错误 -----------------------
    SYSTEM_ERROR(10001, "系统错误"),
    GET_CONNECTION_ERROR(10002, "获取链接失败！"),
    CREATE_TABLE_ERROR(10003, "创建表失败！"),
    INVOKE_QUERY_ERROR(10004, "执行查询失败！"),
    ROW_TIME_FORMAT_ERROR(10005, "行数据时间格式转换失败！"),


    BINLOG_CONFIG_NOT_EXIST(10101, "binlog配置不存在！"),
    BINLOG_CONFIG_HAS_USED(10102, "binlog配置正在使用！"),
    BINLOG_CONFIG_TOPIC_TABLE_NOT_EXIST(10103, "binlog的topic&table配置不存在！"),

    BUSINESS_DETAIL_NOT_EXIST(10201, "业务域不存在！"),
    BUSINESS_DETAIL_HAS_USED(10202, "业务域正在使用！"),

    RULE_DETAIL_NOT_EXIST(10301, "规则SQL不存在！"),
    RULE_DETAIL_HAS_USED(10302, "规则SQL正在使用！"),

    BUSINESS_RULE_NOT_EXIST(10401, "业务规则不存在！"),
    BUSINESS_RULE_HAS_USED(10402, "业务规则正在使用！"),
    BUSINESS_RULE_UNIQUE_KEY_NOT_EXIST(10403, "业务规则依赖的唯一键未指定！"),
    BUSINESS_RULE_EXTEND_PARAM_ERROR(10404, "业务规则中的扩展参数信息不完善！"),
    BUSINESS_RULE_SQL_RULE_EXIST(10405, "业务规则组队的SQL规则已存在！"),
    BUSINESS_RULE_QUERY_MODE_CHECK_TYPE_NO_MATCH(10406, "业务规则的查询模式为1:n or n:1时，校验类型必须为正确性校验！"),
    BUSINESS_RULE_HAS_USED_RULE_DETAIL(10407, "有业务规则正在使用当前操作的规则SQL，请先停用！"),
    BUSINESS_RULE_TIMEOUT_MAX_ERROR(10408, "业务规则的超时过多，小时单位最多2小时；分钟单位最多60分钟！")
    ;

    private int code;
    private String message;

    /**
     * app需要的状态
     */
    private int appResultStatus;

    ErrorEnum(int code, String message) {
        this(code, message, 0);
    }

    ErrorEnum(int code, String message, int appResultStatus) {
        this.code = code;
        this.message = message;
        this.appResultStatus = appResultStatus;
    }

}