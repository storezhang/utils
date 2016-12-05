package com.ruijc.sample.spring.boot;

import com.ruijc.shiro.AuthFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthFilter extends AuthFilter {

    @Override
    protected void onAjaxRedirectToLogin(HttpServletRequest req, HttpServletResponse rsp, String data) throws IOException {
        super.onAjaxRedirectToLogin(req, rsp, data);
        System.err.println("--->Ajax redirect to login!");
    }

    @Override
    protected void onRedirectToLogin(HttpServletRequest req, HttpServletResponse rsp, String data) throws IOException {
        super.onRedirectToLogin(req, rsp, data);
        System.err.println("--->Common redirect to login!");
    }
}
