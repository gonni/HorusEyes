package com.yg.horus.crawl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a1000074 on 07/01/2021.
 */
public class HttpResourceCrawler {

    public HttpResourceCrawler() {
        this.initSslConnection(); ;
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

    public List<CrawlDataUnit> getMatchedLinks(String url, String urlPattern) {
        List<CrawlDataUnit> subUrls = this.getSubUrls(url);
        Pattern p = Pattern.compile(urlPattern) ;

        List<CrawlDataUnit> lstFiltered = new ArrayList<>() ;

        subUrls.stream().forEach(subUrl -> {
            String targetUlr = subUrl.getUrl();
//            System.out.println("-> " + targetUlr);
            if(p.matcher(targetUlr).matches()) {
//                System.out.println("Matched -> " +  subUrl);
                lstFiltered.add(subUrl);
            }
        });

        return lstFiltered ;
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

    public Document getPageDoc(String url) throws IOException {
        return  Jsoup.connect(url).get();
    }

    public String getBodyData(String url) throws IOException{
        String responseBody = null ;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpget, responseHandler);

        } finally {
            httpclient.close();
        }

        return responseBody ;
    }

    public static void main(String ... v) throws Exception {
        HttpResourceCrawler test = new HttpResourceCrawler();
        String seedUrl = "https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2";
        String regexFilter = "^(https:\\/\\/finance.naver.com\\/news\\/news_read.nhn\\?article_id=).*$";
        List<CrawlDataUnit> matchedLinks = test.getMatchedLinks(seedUrl, regexFilter);

        final Pattern p = Pattern.compile(regexFilter) ;
        System.out.println("====> ReX Filtered .." + matchedLinks.size());
        matchedLinks.stream().forEach(System.out::println);
//        matchedLinks.stream().forEach(cdu->{
//            Matcher matcher = p.matcher(cdu.getUrl());
//            System.out.println("url -> " + cdu.getUrl());
//            if(matcher.matches()) {
//                System.out.println("> " + cdu.getAnchorText() + "\n --> " + cdu.getAnchorText());
//            }
//        });
//
//        Pattern p1 = Pattern.compile(regexFilter) ;
//        Matcher matcher = p1.matcher("https://finance.naver.com/news/news_read.nhn?article_id=0004570979&office_id=014&mode=LSS3D&type=0&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2");
//
//        System.out.println("MATCH FIND :" + matcher.find());
//        System.out.println("MATCH MATCH :" + matcher.matches());

//        String bodyData = test.getBodyData("https://www.naver.com");

//        System.out.println("HTML body -> " + bodyData);

//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpGet httpget = new HttpGet("https://www.naver.com");
//
//            System.out.println("Executing request " + httpget.getRequestLine());
//
//            // Create a custom response handler
//            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
//
//                @Override
//                public String handleResponse(
//                        final HttpResponse response) throws ClientProtocolException, IOException {
//                    int status = response.getStatusLine().getStatusCode();
//                    if (status >= 200 && status < 300) {
//                        HttpEntity entity = response.getEntity();
//                        return entity != null ? EntityUtils.toString(entity) : null;
//                    } else {
//                        throw new ClientProtocolException("Unexpected response status: " + status);
//                    }
//                }
//
//            };
//            String responseBody = httpclient.execute(httpget, responseHandler);
//            System.out.println("----------------------------------------");
//            System.out.println(responseBody);
//        } finally {
//            httpclient.close();
//        }

//        HttpPost httpPost = new HttpPost("http://targethost/login");
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        nvps.add(new BasicNameValuePair("username", "vip"));
//        nvps.add(new BasicNameValuePair("password", "secret"));
//        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//        CloseableHttpResponse response2 = httpclient.execute(httpPost);
//
//        try {
//            System.out.println(response2.getStatusLine());
//            HttpEntity entity2 = response2.getEntity();
//            // do something useful with the response body
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity2);
//        } finally {
//            response2.close();
//        }
    }
}
