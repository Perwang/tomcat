package com.wang.demo.tomcat.util;

/**
 * Created by zhongruo
 * Date: 19/12/26
 * Time: 下午6:14
 * Described:
 */

import com.wang.demo.tomcat.dto.Servlet;
import com.wang.demo.tomcat.dto.ServletContext;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 使用Jaxb2.0实现XML与Java Object互转的Binder.
 *
 * 特别支持Root对象是List的情形.
 */
public class JaxbUtil {
    // 多线程安全的Context.
    private JAXBContext jaxbContext;

    /**
     * @param types
     *            所有需要序列化的Root对象的类型.
     */
    public JaxbUtil(Class<?>... types) {
        try {
            jaxbContext = JAXBContext.newInstance(types);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //Java Object to Xml.

    public String toXml(Object root, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //Java Object to Xml, 特别支持对Root Element是Collection的情形.
    @SuppressWarnings("unchecked")
    public String toXml(Collection root, String rootName, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
                    new QName(rootName), CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(wrapperElement, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //Xml to Java Object.
    @SuppressWarnings("unchecked")
    public <T> T fromXml(String xml) {
        try {
            StringReader reader = new StringReader(xml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromXml(File file){
        try {
            return (T) createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }

    }

    // Xml to Java Object, 支持大小写敏感或不敏感.
    @SuppressWarnings("unchecked")
    public <T> T fromXml(String xml, boolean caseSensitive) {
        try {
            String fromXml = xml;
            if (!caseSensitive){
                fromXml = xml.toLowerCase();
            }
            StringReader reader = new StringReader(fromXml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //创建Marshaller, 设定encoding(可为Null).
    public Marshaller createMarshaller(String encoding) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (null==encoding && encoding.length()>0) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //创建UnMarshaller.
    public Unmarshaller createUnmarshaller() {
        try {
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //封装Root Element 是 Collection的情况.
    public static class CollectionWrapper {
        @SuppressWarnings("unchecked")
        @XmlAnyElement
        protected Collection collection;
    }

    public static void main(String[] args) {
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<web-app>" +
                "    <servlets>" +
                "        <servlet-class>22</servlet-class>" +
                "        <url-pattern>222</url-pattern>" +
                "    </servlets>" +
                "    <servlets>" +
                "        <servlet-class>22</servlet-class>" +
                "        <url-pattern>222</url-pattern>" +
                "    </servlets>" +
                "</web-app>";

        JaxbUtil jaxbUtil=new JaxbUtil(ServletContext.class);
        //ServletContext servletContext2 = jaxbUtil.fromXml(xml);

        ServletContext servletContext=new ServletContext();
        List<Servlet> servlets=new ArrayList<Servlet>();
        Servlet servlet=new Servlet();
        servlet.setServletClass("22");
        servlet.setUrlPattern("222");
        servlets.add(servlet);

        Servlet servlet2=new Servlet();
        servlet2.setServletClass("23");
        servlet2.setUrlPattern("2232");
        servlets.add(servlet2);

        servletContext.setServlets(servlets);

       // ServletContext servletContext = jaxbUtil.fromXml(xml);

        String xmlTemp =jaxbUtil.toXml(servletContext,"utf-8");
        File file= new File("/Users/zhongruo/work/study/tomcat/web-app/web.xml");
        ServletContext servletContext2 =jaxbUtil.fromXml(file);
        System.out.println(xmlTemp);
    }
}
