/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML工具类
 *
 * @author Storezhang
 */
public class XmlUtils {

    /**
     * 将XML字符串转换成对象
     *
     * @param <T>   泛型
     * @param xml   字符串
     * @param clazz 类型信息
     * @return 对象
     */
    public static <T> T parseObject(String xml, Class<T> clazz) {
        T obj;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            obj = (T) um.unmarshal(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    /**
     * 将对象转换成XML字符串
     *
     * @param <T>   泛型
     * @param obj   对象
     * @param clazz 类型信息
     * @return 字符串
     */
    public static <T> String toXml(Object obj, Class<T> clazz) {
        String ret;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            ret = writer.toString();
        } catch (JAXBException e) {
            ret = "";
        }

        return ret;
    }

    /**
     * 简单读取XML方法
     *
     * @param data    XML字符串
     * @param element XML元素
     * @return 文本
     */
    public static String getText(String data, String element) {
        String result = "";
        Pattern pattern = Pattern.compile(makePattern(element));
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            result = getXmlClearText(matcher.group(1));
        }
        return result;
    }

    /**
     * 简单读取XML方法
     *
     * @param data    XML字符串
     * @param element XML元素
     * @return 整数
     */
    public static int getInt(String data, String element) {
        String result = "";
        Pattern pattern = Pattern.compile(makePattern(element));
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            result = getXmlClearText(matcher.group(1));
        }
        return NumberUtils.getInt(result);
    }

    /**
     * 简单读取XML方法
     *
     * @param data XML字符串
     * @param path XML的Path
     * @return 文本
     */
    public static String getTextX(String data, String path) {
        String result = "";
        String[] elements = path.split("/");
        for (int index = 0; index < elements.length; ++index) {
            if (index != elements.length - 1) {
                data = getText(data, elements[index]);
            } else {
                result = getText(data, elements[index]);
            }
        }
        return result;
    }

    /**
     * 简单读取XML方法
     *
     * @param data XML字符串
     * @param path XML的Path
     * @return 整数
     */
    public static int getIntX(String data, String path) {
        String result = "";
        String[] elements = path.split("/");
        for (int index = 0; index < elements.length; ++index) {
            if (index != elements.length - 1) {
                data = getText(data, elements[index]);
            } else {
                result = getText(data, elements[index]);
            }
        }
        return NumberUtils.getInt(result);
    }

    /**
     * 简单读取XML方法
     *
     * @param data    XML字符串
     * @param element XML元素
     * @return 文本列表
     */
    public static List<String> getTexts(String data, String element) {
        List<String> textList = new ArrayList<String>();
        Pattern pattern = Pattern.compile(makePattern(element));
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            textList.add(getXmlClearText(matcher.group(1)));
        }
        return textList;
    }

    /**
     * 简单读取XML方法
     *
     * @param data XML字符串
     * @param path XML的Path
     * @return 文本列表
     */
    public static List<String> getTextsX(String data, String path) {
        List<String> textList = new ArrayList<String>();
        String[] elements = path.split("/");
        for (int index = 0; index < elements.length; ++index) {
            if (index != elements.length - 1) {
                data = getText(data, elements[index]);
            } else {
                textList = getTexts(data, elements[index]);
            }
        }
        return textList;
    }

    private static String makePattern(String param) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(param);
        sb.append(">");
        sb.append("(.*?)");
        sb.append("</");
        sb.append(param);
        sb.append(">");
        return sb.toString();
    }

    /**
     * 取得真正的XML字符串，除掉注释
     *
     * @param xml 原始XML字符串
     * @return 真正的字符串
     */
    private static String getXmlClearText(String xml) {
        return xml.replaceAll("\\<\\!\\[CDATA\\[", "").replaceAll("]]>", "");
    }

    public static Map<String, String> convert(String xml) throws Exception {
        xml = getXmlClearText(xml);
        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);

        return createMap(document.getDocumentElement());
    }

    public static Map<String, String> createMap(Node node) {
        Map<String, String> map = new HashMap<String, String>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.hasAttributes()) {
                for (int j = 0; j < currentNode.getAttributes().getLength(); j++) {
                    Node item = currentNode.getAttributes().item(i);
                    map.put(item.getNodeName(), item.getTextContent());
                }
            }
            if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
                map.putAll(createMap(currentNode));
            } else if (node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                map.put(node.getLocalName(), node.getTextContent());
            }
        }

        return map;
    }

    public static void main(String[] args) {
        String xmlText = "<A><B>1</B></A>";
        System.err.println("--->" + getTextX(xmlText, "A/B"));
        String xmlTexts = "<items><item>1</item><item>2</item></items>";
        System.err.println("--->" + getTextsX(xmlTexts, "items/item"));
    }
}
