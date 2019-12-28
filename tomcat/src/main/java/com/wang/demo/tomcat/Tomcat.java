package com.wang.demo.tomcat;

import com.wang.demo.tomcat.dto.ProjectContext;
import com.wang.demo.tomcat.util.ProjectUtil;
import com.wang.demo.tomcat.util.WarUtils;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Date: 19/12/25
 * Time: 下午4:35
 * Described:
 */
public class Tomcat {

    //线程池，用来处理客户端发送过来的请求
    private static ExecutorService executor = Executors.newFixedThreadPool(100);

    private static String PATH="/Users/zhongruo/work/study/tomcat/web-app/";

    public static void main(String[] args) throws Exception {
        String localPath=PATH;
        File file=new File(localPath);
        Path localPath2= file.toPath();
        WarUtils.unZipWar(localPath2,localPath2);

        // key 为项目名称。key 为url路径和class全路径
        Map<String,Map<String,String>> pathMap=new HashMap<>();

        Map<String,Object> url2ObjectMap=new HashMap<>();
        //2、读取下面的文件
        // 先找到项目文件夹，然后读取项目文件夹里的类和web.xml文件
        //获取项目名称
        File[] projects = file.listFiles(pathname -> pathname.isDirectory());
        // 对项目里的内容进行扫描
        for(File project : projects){
            //扫描web.xml文件
            ProjectContext projectContext=ProjectUtil.createProjectContext(new File(project.getPath()+"/web.xml"));
            pathMap.put(project.getName(),projectContext.getProjectMap());
        }

        //3、把pathMap里面的东西加载到classloader中
        URL url =new URL("file:"+localPath+"/classes");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        for(Map.Entry<String,Map<String,String>> entry:pathMap.entrySet()){
            String key=entry.getKey();
            Map<String,String> value=entry.getValue();
            for(Map.Entry<String,String> entry1:value.entrySet()){
                //会把相关的类都加载进来的。找的路径就是url
                //加载
                Class<?> aClass = urlClassLoader.loadClass(entry1.getValue());
                //创建对象
                Object servlet=aClass.newInstance();
                url2ObjectMap.put("/"+key+entry1.getKey(),servlet);
            }
        }

        ServerSocket server = new ServerSocket(8080);
        while (true){
            Socket socket = server.accept();
            ServerRunnable serverRunnable=new ServerRunnable(socket,url2ObjectMap);
            executor.submit(serverRunnable);
        }


    }


}
