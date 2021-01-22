package com.yg.horus.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.*;

import java.io.IOException;

/**
 * Created by a1000074 on 08/01/2021.
 */
public class PageWrapper {
    public static void main(String ... v) {
        try {
            Document doc = Jsoup.connect("https://www.naver.com").get();
            System.out.println("Title -> " + doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
