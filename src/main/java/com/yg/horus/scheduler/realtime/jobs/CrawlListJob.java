package com.yg.horus.scheduler.realtime.jobs;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.TopSeeds;
import com.yg.horus.dto.ListCrawlOption;
import com.yg.horus.scheduler.realtime.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CrawlListJob implements Job<List<CrawlDataUnit>> {
    private ListCrawlOption listCrawlOption = null ;
    private List<CrawlDataUnit> result = null ;
    private CrawlRepository crawlRepository = null;

    private long seedNo = -1L;

    public CrawlListJob(long seedNo, ListCrawlOption _listCrawlOption, CrawlRepository _crawlRepository) {
        this.listCrawlOption = _listCrawlOption ;
        this.crawlRepository = _crawlRepository ;
        this.seedNo = seedNo ;
    }

    @Override
    public void start() {
        ListPageCrawler lpc = new ListPageCrawler();
        this.result = lpc.getMatchedLinks(this.listCrawlOption.getTargetSeedUrl(),
                this.listCrawlOption.getFilterCrawlUrlRxPattern(),
                this.listCrawlOption.getFilterDomGroupAttr()) ;

        this.storeCrawlData(this.result);
    }

    private boolean storeCrawlData(List<CrawlDataUnit> result) {
        if(this.crawlRepository != null ) {
            result.forEach(link -> {
                //TODO changed query to reduce cup load
                CrawlUnit crawlUnit = this.crawlRepository.findOneByUrl(link.getUrl());

                if(crawlUnit == null) {
                    log.info("Crawled Unit New : {}", link);
                    crawlUnit = CrawlUnit.builder()
                            .url(link.getUrl())
                            .anchorText(link.getAnchorText())
                            .status(CrawlStatus.IURL)
                            .build();

//                crawlUnit.setSeedNo(this.seedNo);
                    if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.IMG)) {
                        crawlUnit.setAnchorImg(link.getAnchorText());
                        crawlUnit.setAnchorText(null);
                    }
                    // cnt++
                } else {
//                    log.info("Crawled Unit Duplicated : {}", link);

                    if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.IMG)) {
                        crawlUnit.setAnchorImg(link.getAnchorText());
                    } else if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.TEXT)) {
                        crawlUnit.setAnchorText(link.getAnchorText());
                    }
                }

                TopSeeds topSeeds = new TopSeeds();
                topSeeds.setSeedNo(this.seedNo);
                crawlUnit.setTopSeeds(topSeeds);

                this.crawlRepository.save(crawlUnit);
            });
        }

        return false ;
    }

    @Override
    public List<CrawlDataUnit> getCrawled() {
        return this.result ;
    }

}
