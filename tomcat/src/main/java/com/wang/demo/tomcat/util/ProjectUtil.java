package com.wang.demo.tomcat.util;

import com.wang.demo.tomcat.dto.ProjectContext;
import com.wang.demo.tomcat.dto.Servlet;
import com.wang.demo.tomcat.dto.ServletContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongruo
 * Date: 19/12/26
 * Time: 下午4:57
 * Described:
 */
public class ProjectUtil {


    /**
     * 根据地址web.xml 来组装ProjectContext
     * <p>
     * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     * <web-app>
     * <servlet>
     * <servlet-class>22</servlet-class>
     * <url-pattern>222</url-pattern>
     * </servlet>
     * <servlet>
     * <servlet-class>23</servlet-class>
     * <url-pattern>2232</url-pattern>
     * </servlet>
     * </web-app>
     *
     * @param file
     * @return
     */
    public static ProjectContext createProjectContext(File file) {
        JaxbUtil jaxbUtil = new JaxbUtil(ServletContext.class);
        ServletContext servletContext = jaxbUtil.fromXml(file);
        if (servletContext == null || servletContext.getServlets() == null || servletContext.getServlets().size() == 0) {
            return null;
        }
        Map<String,String> projectMap=new HashMap<String, String>();
        for(Servlet servlet:servletContext.getServlets()){
            projectMap.put(servlet.getUrlPattern(),servlet.getServletClass());
        }
        ProjectContext projectContext = new ProjectContext();
        projectContext.setProjectMap(projectMap);
        return projectContext;

    }
}
