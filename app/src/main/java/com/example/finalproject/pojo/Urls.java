package com.example.finalproject.pojo;

/**
 * 便于调试时更改url为本地或者服务器端的后端
 */
public class Urls {
//    private static String url = "http://192.168.123.188:8080/";
    private static String url = "http://weichenhao.cn:8080/";


    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Urls.url = url;
    }
}
