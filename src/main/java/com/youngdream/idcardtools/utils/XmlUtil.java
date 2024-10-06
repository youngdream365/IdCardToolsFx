package com.youngdream.idcardtools.utils;

import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * XML解析工具类（基于JAXB）
 *
 * @author YoungDream
 */
public class XmlUtil {
    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class.getName());

    /**
     * 加载xml文件为InputStream
     */
    public static InputStream loadXml(String xmlName) {
        InputStream inputStream = null;
        try {
            inputStream = XmlUtil.class.getClassLoader().getResourceAsStream("xml/" + xmlName);
        } catch (Exception e) {
            LOGGER.warning("加载xml文件异常");
        }
        return inputStream;
    }

    /**
     * 将Xml的Dom转换为实体类
     * 参考: https://blog.csdn.net/wn084/article/details/80853587
     */
    public static <T> T xml2Entity(InputStream xmlInputStream, Class<T> clzz) {
        T target = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clzz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            target = (T) unmarshaller.unmarshal(xmlInputStream);
        } catch (JAXBException e) {
            LOGGER.warning("xmlInputStream转换为entity异常");
        }
        return target;
    }

    /**
     * 将xml的inputStream转换为Document
     */
    public static Document xml2Document(InputStream xmlInputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document dom = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            dom = builder.parse(xmlInputStream);
        } catch (Exception e) {
            LOGGER.warning("xmlInputStream转换为Document异常");
        }
        return dom;
    }

    /**
     * @desc obj->xml (Marshaller)
     * @author YoungDream
     */
    public static <T> void obj2Xml(Object jaxbObject, OutputStream xml) throws JAXBException {
        Class<?> cazz = jaxbObject.getClass();
        // 获取JAXB的上下文环境，并注册对象类
        JAXBContext context = JAXBContext.newInstance(cazz);
        // 创建 Marshaller 实例（obj->xml）
        Marshaller marshaller = context.createMarshaller();
        // 设置转换参数，格式化输出(嵌套缩进)
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        // 输出到目标源
        marshaller.marshal(jaxbObject, xml);
    }

    /**
     * @desc xml->obj (Unmarshaller)
     * @author YoungDream
     */
    public static <T> T xml2Obj(InputStream xml, Class<T> type) throws JAXBException {
        // 获取JAXB的上下文环境，并注册对象类
        JAXBContext context = JAXBContext.newInstance(type);
        // 创建 UnMarshaller 实例(xml->obj)
        Unmarshaller unmarshaller = context.createUnmarshaller();
        // 将XML数据序列化，返回值为Object需要强转类型
        return (T) unmarshaller.unmarshal(xml);
    }
}