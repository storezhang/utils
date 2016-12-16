package com.ruijc.shiro;

import com.ruijc.util.HttpUtils;
import com.ruijc.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 基于表单的授权过滤器
 *
 * @author Storezhang
 */
public class AuthFilter extends FormAuthenticationFilter {

    private ThreadLocal<String> dataLocal;
    private String dataParamName;

    public AuthFilter() {
        dataLocal = new ThreadLocal<String>();
    }

    public String getDataParamName() {
        return dataParamName;
    }

    public void setDataParamName(String dataParamName) {
        this.dataParamName = dataParamName;
    }

    /**
     * 用户登录
     *
     * @param user 存在Session中的用户对象
     */
    protected void onUserLogin(Object user) {
        // 用户登录
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean isAccessAllowed;

        if (null != mappedValue) {
            String[] methods = (String[]) mappedValue;
            String method = getHttpMethodAction(request);
            isAccessAllowed = !StringUtils.isBlank(method) && Arrays.asList(methods).contains(method) ? super.isAccessAllowed(request, response, mappedValue) : true;
        } else {
            isAccessAllowed = super.isAccessAllowed(request, response, mappedValue);
        }

        return isAccessAllowed;
    }

    protected String getHttpMethodAction(ServletRequest request) {
        String method = "";

        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod().toLowerCase();
        }

        return method;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject;
        try {
            subject = SecurityUtils.getSubject();
        } catch (Exception e) {
            return super.onAccessDenied(request, response);
        }

        if (subject.isRemembered() && !subject.isAuthenticated()) {
            onUserLogin(subject.getPrincipal());
            return true;
        }

        String data = request.getParameter(dataParamName);
        dataLocal.set(data);

        return super.onAccessDenied(request, response);
    }

    /**
     * Ajax环境下跳转到登录界面
     *
     * @param req  请求对象
     * @param rsp  响应对象
     * @param data 登录前传过来的参数
     */
    protected void onAjaxRedirectToLogin(HttpServletRequest req, HttpServletResponse rsp, String data) throws IOException {
        super.redirectToLogin(req, rsp);
    }

    /**
     * 普通环境下跳转到登录界面
     *
     * @param req  请求对象
     * @param rsp  响应对象
     * @param data 登录前传过来的参数
     */
    protected void onRedirectToLogin(HttpServletRequest req, HttpServletResponse rsp, String data) throws IOException {
        super.redirectToLogin(req, rsp);
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            super.redirectToLogin(request, response);
            return;
        }

        String data = dataLocal.get();
        HttpServletRequest req = HttpUtils.toHttp(request);
        HttpServletResponse rsp = HttpUtils.toHttp(response);
        if (HttpUtils.isAjax(request)) {
            rsp.addHeader("WWW-Authentication", "ACME-AUTH");
            rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            onAjaxRedirectToLogin(req, rsp, data);
        } else {
            onRedirectToLogin(req, rsp, data);
        }
    }
}
