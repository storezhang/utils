/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc;

/**
 * Http操作响应对象
 *
 * @author Storezhang
 */
public class Response extends BaseObject {

    public static final int OK = 200;//操作正确
    public static final int FAILD = 400;//操作有错误
    public static final int ERROR = 500;//异常
    public static final int DISABLED = 700;//该功能已经被禁用

    private int code;//操作的状态码
    private String content;//操作提示信息
    private Object data;//操作的返回数据

    public Response() {
        code = OK;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
