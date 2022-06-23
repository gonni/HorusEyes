package com.yg.horus.api;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.dto.ListCrawl;
import com.yg.horus.dto.ListCrawlOptionReq;
import com.yg.horus.dto.ListCrawlRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CrawlCtr {
    private final ListPageCrawler listPageCrawler ;

    @Autowired
    public CrawlCtr(ListPageCrawler _listPageCrawler) {
        this.listPageCrawler = _listPageCrawler ;
    }

    @RequestMapping("/crawl/page/list")
    @GetMapping("/http-servlet-response")
    public @ResponseBody
    ListCrawlRes getListCrawl(@RequestBody ListCrawlOptionReq listCrawlOptionReq, HttpServletResponse httpResponse) {
        log.info("Detected API {}", listCrawlOptionReq) ;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        List<CrawlDataUnit> matchedLinks = this.listPageCrawler.getMatchedLinks(listCrawlOptionReq.getTargetSeedUrl(),
                listCrawlOptionReq.getFilterCrawlUrlRxPattern(),
                listCrawlOptionReq.getFilterDomGroupAttr());
        List<ListCrawl> crawls = matchedLinks.stream()
                .map(crawlUnit -> new ListCrawl(crawlUnit.getUrl(), crawlUnit.getAnchorText())).
                collect(Collectors.toList());

        ListCrawlRes response = ListCrawlRes.builder().lstCrawls(crawls).build() ;
        if(response.getLstCrawls() != null)
            log.info("Count of Crawled : {}", response.getLstCrawls().size()) ;

        return response ;
//        String crawlResult = matchedLinks.stream().map(unit -> unit.toString()).collect(Collectors.joining("\n<br>"));
//        return "Crawled -> " + crawlResult ;


    }




}
