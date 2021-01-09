package com.common.config;

import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;


/**
 * @author weimin
 */
public class MyServletContext extends MockServletContext{

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter){
        return null;
    }
}
