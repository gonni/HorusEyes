package com.yg.horus.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by a1000074 on 07/01/2021.
 */
@Service
public class ListPageCrawler extends CrawlBase {

    public ListPageCrawler() {
        super() ;
    }

    public List<CrawlDataUnit> getMatchedLinks(String url, String urlPattern, String innerNodeSelector) {
        List<CrawlDataUnit> subUrls = this.getSubUrls(url, innerNodeSelector);
        Pattern p = Pattern.compile(urlPattern) ;

        List<CrawlDataUnit> lstFiltered = new ArrayList<>() ;
        subUrls.stream().forEach(subUrl -> {
            String targetUlr = subUrl.getUrl();
            if(p.matcher(targetUlr).matches()) {
                lstFiltered.add(subUrl);
            }
        });

        return lstFiltered ;
    }


    public List<CrawlDataUnit> getSubUrls(String url, String innerNodeSelector) {
        List<CrawlDataUnit> lstUrls = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements filteredDoc = null ;
            if(innerNodeSelector != null )
                filteredDoc = doc.select(innerNodeSelector) ;
            else
                filteredDoc = doc.select("html") ;

            Elements links = filteredDoc.select("a");

            links.forEach(link -> {
                String anchorText = link.text();
                String linkUrl = link.attr("abs:href");
                Elements imgElement = link.select("img");
                if(imgElement != null && imgElement.size() > 0) {
                    anchorText = imgElement.attr("src") ;
                    lstUrls.add(new CrawlDataUnit(CrawlDataUnit.AnchorType.IMG, anchorText, linkUrl));
                } else {
                    lstUrls.add(new CrawlDataUnit(CrawlDataUnit.AnchorType.TEXT, anchorText, linkUrl));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstUrls ;
    }

    public static void main(String ... v) throws Exception {
        ListPageCrawler test = new ListPageCrawler();
        String seedUrl = "https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20200124&page=1";
//        seedUrl = "https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402";
//
//        String regexFilter = "^(https:\\/\\/finance.naver.com\\/news\\/news_read.nhn\\?article_id=).*$";
//        List<CrawlDataUnit> matchedLinks = test.getMatchedLinks(seedUrl, regexFilter, "ul.realtimeNewsList");

        seedUrl = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=263&sid1=101&mid=sec&listType=title&date=20211020&page=3";
        String regexFilter = "^(https:\\/\\/n.news.naver.com\\/mnews\\/).*$";
        List<CrawlDataUnit> matchedLinks = test.getMatchedLinks(seedUrl, regexFilter, "ul.type02");


        final Pattern p = Pattern.compile(regexFilter) ;
        System.out.println("====> ReX Filtered :" + matchedLinks.size());
        matchedLinks.stream().forEach(System.out::println);


//        for (CrawlDataUnit crawlDataUnit : test.getSubUrls(seedUrl, "ul.realtimeNewsList")) {
//            System.out.println("Filtered URL :" + crawlDataUnit.toString());
//        }



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
