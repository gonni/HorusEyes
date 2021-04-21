package com.yg.horus.crawl;

import com.yg.horus.doc.ContentPageDoc;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by a1000074 on 25/03/2021.
 */
@Slf4j
public class PageWrapper {

    private ListPageCrawler listPageCrawler = null ;

    public PageWrapper() {
        this.listPageCrawler = new ListPageCrawler() ;
    }

    //TODO need to consider ...
    public ContentPageDoc getContentPageDoc(String url, ContentsPageWrappingRule contentsPageWrappingRule) {
        ContentPageDoc contentPageDoc = new ContentPageDoc() ;

        Document pageDoc = null ;
        try {
            pageDoc = this.listPageCrawler.getPageDoc(url);

        } catch (IOException e) {
            e.printStackTrace();
            contentPageDoc.setStatus(ContentPageDoc.PageDocStatus.FAIL);
        }

        contentPageDoc.setTitle(pageDoc.title());
        contentPageDoc.setTitleOnContent(pageDoc.select(contentsPageWrappingRule.getTitleOnContents()).text());
        contentPageDoc.setDateOnContent(pageDoc.select(contentsPageWrappingRule.getContDate()).text());

        StringBuilder sb = new StringBuilder();
        for(String rule : contentsPageWrappingRule.getContents()) {
            sb.append(pageDoc.select(rule).text()).append("\n---\n");
        }

        contentPageDoc.setContent(sb.toString());

        return contentPageDoc ;
    }

    public static void main(String ... v) {
        System.out.println("Active System ..");

        String targetUrl = "https://finance.naver.com/news/news_read.nhn?article_id=0004570979&office_id=014&mode=LSS3D&type=0&section_id=101&section_id2=258&section_id3=402&date=20210124&page=2";
        targetUrl = "https://finance.naver.com/news/news_read.nhn?article_id=0004575978&office_id=008&mode=LSS3D&type=0&section_id=101&section_id2=258&section_id3=401&date=20210421&page=1";

        PageWrapper pageWrapper = new PageWrapper();

        ContentsPageWrappingRule wrapRule = new ContentsPageWrappingRule() ;
        wrapRule.setTitleOnContents("div.article_info > h3");
        wrapRule.getContents().add("div#content");
        wrapRule.setContDate("span.article_date");

        ContentPageDoc contentPageDoc = pageWrapper.getContentPageDoc(targetUrl, wrapRule);

        System.out.println("Result --> \n" + contentPageDoc);
    }
}
