package com.yg.horus.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000074 on 08/01/2021.
 */
public class PageWrapper {

    public PageWrapper() {
        this.initSslConnection();
    }

    private void initSslConnection() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return new X509Certificate[0];}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            System.out.println("Custom SSL setting is just initialized ..");

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<CrawlDataUnit> getSubUrls(String url) {
        List<CrawlDataUnit> lstUrls = new ArrayList<CrawlDataUnit>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a");

//            System.out.println("Links : " + links.size());


            links.forEach(link -> {
//                System.out.println("HTML ->" + link.outerHtml());


                String anchorText = link.text();
                Elements imgElement = link.select("img");
                if(imgElement != null && imgElement.size() > 0) {
//                    System.out.println("IMG detected ..");
                    anchorText = "[IMG]" ;
                }

                String linkUrl = link.attr("abs:href");

                System.out.println("URL ->" + linkUrl);
                lstUrls.add(new CrawlDataUnit(anchorText, linkUrl));
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstUrls ;
    }


    public static void main(String ... v) {
        try {
            PageWrapper pageWrapper = new PageWrapper();

//            Document doc = Jsoup.connect("https://www.naver.com").get();
            //https://finance.naver.com/news/news_list.nhn?mode=LSS2D&section_id=101&section_id2=258&date=20210121&page=8
//            Document doc = Jsoup.connect("https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2").get();
//
//            Elements links = doc.select("dd.articleSubject");
//            links.stream().forEach(link -> {
//                System.out.println("LINK -> " + link.text());
//            });
//
//            System.out.println("Title -> " + doc.title());

            String url = "https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2";
            List<CrawlDataUnit> subUrls = pageWrapper.getSubUrls(url);
            System.out.println("Size -> " + subUrls.size());

            int i = 0;
            for (CrawlDataUnit subUrl : subUrls) {
                System.out.println(i++ + " " + subUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
