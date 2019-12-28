package com.wang.demo.tomcat.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongruo
 * Date: 19/12/26
 * Time: 下午5:02
 * Described:
 */
public class ProjectContext {
    /**
     * key url
     * value 为项目里的url对应的servlet全路径
     */
    private  Map<String,String> projectMap=new HashMap<String, String>();

    public  Map<String, String> getProjectMap() {
        return projectMap;
    }

    public  void setProjectMap(Map<String,String> projectMap) {
        this.projectMap = projectMap;
    }

    public ProjectContext() {
    }
}
