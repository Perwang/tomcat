package com.wang.demo.tomcat.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by zhongruo
 * Date: 19/12/26
 * Time: 下午6:19
 * Described:
 */
@XmlRootElement(name="web-app")
public class ServletContext {
    private List<Servlet> servlets;

    @XmlElement(name="servlet")
    public List<Servlet> getServlets() {
        return servlets;
    }

    public void setServlets(List<Servlet> servlets) {
        this.servlets = servlets;
    }
}
