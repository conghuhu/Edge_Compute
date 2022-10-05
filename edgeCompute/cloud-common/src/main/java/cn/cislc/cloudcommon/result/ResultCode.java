package cn.cislc.cloudcommon.result;

/**
 * @Author: conghuhu
 * @Description: 返回码定义
 * 规定:
 *
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * #4001~4999 区间表示用户错误
 * #6000~6999 区间表示邮件错误
 * @Date Create in 2021.9.30
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(500, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAMS_ERROR(10001, "参数有误"),
    SESSION_TIME_OUT(90001, "会话超时"),

    /*docker service错误 */
    CONNECT_ERROR(2000,"docker连接错误"),

    /* 用户错误 */
    USER_NOT_LOGIN(4010, "用户未登录"),
    USER_ACCOUNT_EXPIRED(4011, "账号已过期，请重新登录"),
    USER_CREDENTIALS_ERROR(4012, "密码错误"),
    USER_ACCOUNT_DISABLE(4013, "账号不可用"),
    USER_ACCOUNT_NOT_EXIST(4014, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(4015, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(4016, "账号下线"),
    ACCOUNT_PWD_NOT_EXIST(4017, "用户名或密码不存在"),

    /* 邮件服务 */
    MAIL_SEND_ERROR(6000, "邮件发送失败，请重试"),
    MAIL_MESSAGE_ERROR(6001, "邮件消息生成失败"),
    MAIL_PARAM_ERROR(6002, "邮件参数有误，请检查"),
    MAIL_HTML_ERROR(6003, "读取html文件错误"),
    MAIL_TEMPLATE_ERROR(6004, "freeMaker生成邮件模板错误"),
    MAIL_CODE_ALREADY_SEND(6005, "已发送验证码至邮箱，稍后重试"),
    MAIL_CODE_ERROR(6006, "验证码有误，请检查"),

    /* 查询错误 */
    NOT_FOUND(7000, "查询无果"),

    /* 业务错误 */
    NO_PERMISSION(7001, "没有权限");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
