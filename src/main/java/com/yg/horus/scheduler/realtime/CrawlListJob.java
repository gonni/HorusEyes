package com.yg.horus.scheduler.realtime;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.ListPageCrawler;
import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlStatus;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.TopSeeds;
import com.yg.horus.dto.ListCrawlOption;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CrawlListJob implements Job<List<CrawlDataUnit>> {
    private ListCrawlOption listCrawlOption = null ;
    private List<CrawlDataUnit> result = null ;
    private CrawlRepository crawlRepository = null;

    public CrawlListJob(ListCrawlOption _listCrawlOption, CrawlRepository _crawlRepository) {
        this.listCrawlOption = _listCrawlOption ;
        this.crawlRepository = _crawlRepository ;
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
                CrawlUnit crawlUnit = this.crawlRepository.findOneByUrl(link.getUrl());

                if(crawlUnit == null) {
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
                    log.info("Dupplicated : {}", link);

                    if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.IMG)) {
                        crawlUnit.setAnchorImg(link.getAnchorText());
                    } else if(link.getAnchorType().equals(CrawlDataUnit.AnchorType.TEXT)) {
                        crawlUnit.setAnchorText(link.getAnchorText());
                    }
                }
                TopSeeds topSeeds = new TopSeeds();
                topSeeds.setSeedNo(-1L);
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
