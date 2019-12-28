package com.wang.demo.tomcat;


import com.wang.demo.tomcat.util.HttpUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongruo
 * Date: 19/12/25
 * Time: 下午5:05
 * Described:
 */
public class ServerRunnable implements Runnable {

    private Socket socket;

    private  Map<String,Object> url2ObjectMap=new HashMap<>();;

    public ServerRunnable(Socket socket, Map<String,Object> url2ObjectMap) {
        this.socket = socket;
        this.url2ObjectMap = url2ObjectMap;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;

            System.out.println("收到来自客户端的信息 ");
            StringBuffer stringBuffer = new StringBuffer();
            while ((msg = reader.readLine()) != null) {
                if (msg.length() == 0) {
                    break;
                }
                System.out.println(msg);
                stringBuffer.append(msg).append("\r\n");
            }
            if (stringBuffer.length() == 0) {
                return;
            }
            HttpServletRequest httpServletRequest = HttpUtils.createHttpServletRequest(stringBuffer.toString());
            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write("HTTP/1.1 200 OK \n".getBytes());
            outputStream.write("Content-Length:200 \n ".getBytes());
            outputStream.write("\r\n".getBytes());
            outputStream.write("Content-Type: text/html;charset=UTF-8 \n".getBytes());
            outputStream.write("\r\n".getBytes());
            ServletOutputStream servletOutputStream = new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    outputStream.write(b);
                }
            };
            HttpServletResponse httpServletResponse = HttpUtils.createResponse(servletOutputStream);
            // 取出url路径
            String fisrtLine=stringBuffer.toString().split("\r\n")[0];
            String url=fisrtLine.split(" ")[1];

            Servlet servlet = (Servlet)url2ObjectMap.get(url);
            servlet.service(httpServletRequest, httpServletResponse);

            System.out.println("-----收到来自客户端的信息");
            outputStream.flush();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


}
