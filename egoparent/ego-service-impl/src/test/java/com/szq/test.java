package com.szq;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class test {
    /**
     * 使用httpclient 执行get请求
     * @throws Exception
     */
    @Test
    public void doGet() throws Exception{
        /*
        1、创建一个httpclient对象
        2、创建一个get对象
        3、执行请求
        4、取响应结果
        5、关闭httpclient
         */
        //closeableHttpClient:负责发送请求和接收响应。相当与浏览器
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get=new HttpGet("http://www.sogou.com");
        CloseableHttpResponse response = httpclient.execute(get);
        //响应码
        int status= response.getStatusLine().getStatusCode();
        System.out.println(status);
        //响应体
        HttpEntity entity=response.getEntity();
        //把响应体用utf-8输出
        String string= EntityUtils.toString(entity,"utf-8");
        System.out.println(string);
        response.close();
        httpclient.close();
    }

    //折行get请求带参数
    @Test
    public void doGetWithParam() throws Exception{
        /*
        1、创建一个httpclient对象
        2、创建一个uri对象
        3.折行请求
        4、取响应的结果
        5、关闭httpclient
         */
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder=new URIBuilder("http://www.sogou.com");
        uriBuilder.addParameter("query","java");
        HttpGet get =new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(get);
        //响应码
        int status= response.getStatusLine().getStatusCode();
        System.out.println(status);
        //响应体
        HttpEntity entity=response.getEntity();
        //把响应体用utf-8输出
        String string= EntityUtils.toString(entity,"utf-8");
        System.out.println(string);
        response.close();
        httpClient.close();
    }

}
