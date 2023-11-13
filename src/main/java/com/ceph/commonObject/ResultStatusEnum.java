package com.ceph.commonObject;

public enum ResultStatusEnum {
    SUCCESS(200,"SUCCESS","成功"),//成功
    ERROR(500,"ERROR","失败"),//失败
    DATAISEMPTY(300,"DATAISEMPTY","数据为空"),//数据为空
    LOGICAL_EXCEPTION(1000,"LOGICAL_EXCEPTION","try catch异常");//用于try catch 测试

    private final int code;

    private final String desc;

    private final String description;

    ResultStatusEnum(int code, String desc, String description){
        this.code = code;
        this.desc = desc;
        this.description = description;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescription() {
        return description;
    }
}
