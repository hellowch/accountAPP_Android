package com.example.finalproject.pojo;

public class Urls {
    private static String url = "http://192.168.123.188:8080/";
//    private static String url = "http://weichenhao.cn:8080/";


    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Urls.url = url;
    }
}
