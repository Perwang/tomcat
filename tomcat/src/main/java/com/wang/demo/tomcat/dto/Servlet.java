package com.wang.demo.tomcat.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by zhongruo
 * Date: 19/12/26
 * Time: 下午6:16
 * Described:
 */
public class Servlet {


    private String servletClass;


    private String urlPattern;

    @XmlElement(name="servlet-class")
    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }
    @XmlElement(name="url-pattern")
    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
