package com.yg.horus.crawl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a1000074 on 07/01/2021.
 */
public class HttpResourceCrawler {

    private PageWrapper pageWrapper = new PageWrapper() ;

    public List<CrawlDataUnit> getMatchedLinks(String url, String urlPattern) {
        List<CrawlDataUnit> subUrls = this.pageWrapper.getSubUrls(url);
        Pattern p = Pattern.compile(urlPattern) ;

        List<CrawlDataUnit> lstFiltered = new ArrayList<>() ;

        subUrls.stream().forEach(subUrl -> {
            String targetUlr = subUrl.getUrl();
            System.out.println("-> " + targetUlr);
            if(p.matcher(targetUlr).matches()) {
                System.out.println("Matched -> " +  subUrl);
            }
        });

        return lstFiltered ;
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
//        HttpResourceCrawler test = new HttpResourceCrawler();
//        String seedUrl = "https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2";
        String regexFilter = "^(https:\\/\\/finance.naver.com\\/news\\/news_read.nhn\\?article_id=).*$";
//        List<CrawlDataUnit> matchedLinks = test.getMatchedLinks(seedUrl, regexFilter);
//        System.out.println("ReX Filtered ..");
//        matchedLinks.stream().forEach(System.out::println);

        Pattern p = Pattern.compile(regexFilter) ;
        Matcher matcher = p.matcher("https://finance.naver.com/news/news_read.nhn?article_id=0003861512&office_id=011&mode=LSS3D&type=0&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2");
//
        System.out.println("MATCH FIND :" + matcher.find());
        System.out.println("MATCH MATCH :" + matcher.matches());

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
