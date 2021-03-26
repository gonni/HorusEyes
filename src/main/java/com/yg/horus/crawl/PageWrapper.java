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

    private HttpResourceCrawler httpResourceCrawler = null ;

    public PageWrapper() {
        this.httpResourceCrawler = new HttpResourceCrawler() ;
    }

    //TODO need to consider ...
    public ContentPageDoc getContentPageDoc(String url, WrappingRule wrappingRule) {
        ContentPageDoc contentPageDoc = new ContentPageDoc() ;

        Document pageDoc = null ;
        try {
            pageDoc = this.httpResourceCrawler.getPageDoc(url);

        } catch (IOException e) {
            e.printStackTrace();
            contentPageDoc.setStatus(ContentPageDoc.PageDocStatus.FAIL);
        }

        contentPageDoc.setTitle(pageDoc.title());


        return contentPageDoc ;
    }

    public static void main(String ... v) {
        System.out.println("Active System ..");





    }
}
