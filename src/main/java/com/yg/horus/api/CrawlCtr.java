package com.yg.horus.api;

import com.yg.horus.crawl.ContentsPageCrawler;
import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.doc.ContentPageDoc;
import com.yg.horus.dto.ContentCrawlOption;
import com.yg.horus.dto.ListCrawl;
import com.yg.horus.dto.ListCrawlOption;
import com.yg.horus.dto.ListCrawlRes;
import com.yg.horus.scheduler.global.DataConvHelper;
import com.yg.horus.scheduler.realtime.jobs.CrawlListJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CrawlCtr {
    private final ListPageCrawler listPageCrawler ;
    private final CrawlRepository crawlRepository ;
    private final DataConvHelper dataConvHelper ;

    @Autowired
    public CrawlCtr(ListPageCrawler _listPageCrawler,
                    CrawlRepository _crawlRepository,
                    DataConvHelper _dataConvHelper) {
        this.listPageCrawler = _listPageCrawler ;
        this.crawlRepository = _crawlRepository ;
        this.dataConvHelper = _dataConvHelper ;
    }

    @RequestMapping("/crawl/conf/wrap/list")
    public @ResponseBody
    ListCrawlOption getListCrawlWrapper(@RequestParam long seedNo) {
        return this.dataConvHelper.getListPageWrapRule(seedNo);
    }

    @RequestMapping("/crawl/conf/wrap/content")
    public @ResponseBody
    ContentsPageWrappingRule getContnentWrapper(@RequestParam long seedNo) {
        return this.dataConvHelper.getContentPageWrapRule(seedNo);
    }

    @RequestMapping("/crawl/page/list")
    public @ResponseBody
    ListCrawlRes getListCrawl(@RequestBody ListCrawlOption listCrawlOptionReq) {
        log.info("Detected API {}", listCrawlOptionReq) ;
//        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
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
    }

    @RequestMapping("/crawl/page/list/job")
    public @ResponseBody
    ListCrawlRes listCrawlJob(@RequestBody ListCrawlOption listCrawlOptionReq) {
        log.info("Detected API {}", listCrawlOptionReq) ;

        CrawlListJob job = new CrawlListJob(-1, listCrawlOptionReq, this.crawlRepository);
        job.start();

//        List<CrawlDataUnit> matchedLinks = this.listPageCrawler.getMatchedLinks(listCrawlOptionReq.getTargetSeedUrl(),
//                listCrawlOptionReq.getFilterCrawlUrlRxPattern(),
//                listCrawlOptionReq.getFilterDomGroupAttr());

        List<CrawlDataUnit> matchedLinks = job.getCrawled();
        List<ListCrawl> crawls = matchedLinks.stream()
                .map(crawlUnit -> new ListCrawl(crawlUnit.getUrl(), crawlUnit.getAnchorText())).
                collect(Collectors.toList());

        ListCrawlRes response = ListCrawlRes.builder().lstCrawls(crawls).build() ;
        if(response.getLstCrawls() != null)
            log.info("Count of Crawled : {}", response.getLstCrawls().size()) ;

        return response ;
    }

    @RequestMapping("/crawl/page/content")
    public @ResponseBody
    ContentPageDoc getContentCrawl(@RequestBody ContentCrawlOption req) {
        log.info("Detected API {}", req) ;
        ContentsPageCrawler contentsPageCrawler = new ContentsPageCrawler();
        ContentsPageWrappingRule wrapRule = new ContentsPageWrappingRule() ;
        wrapRule.setTitleOnContents(req.getDocTitle()) ;
        wrapRule.getContents().add(req.getContentGrp()) ;
        wrapRule.setContDate(req.getDocDatetime());

        ContentPageDoc contentPageDoc = contentsPageCrawler.getContentPageDoc(req.getTargetUrl(), wrapRule);

        return contentPageDoc ;
    }


}
