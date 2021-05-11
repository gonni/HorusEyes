package com.yg.horus.crawl.custom;

import com.yg.horus.crawl.CrawlBase;

/**
 * Created by a1000074 on 11/05/2021.
 */
public class CustomPageCrawler <W, R> extends CrawlBase {

    public CustomPageCrawler() {
        ;
    }

    public R getWrappedData(String url, W wrapper) {
        return null ;
    }

    public static void main(String ... v) {
        System.out.println("Active ..");



    }
}
