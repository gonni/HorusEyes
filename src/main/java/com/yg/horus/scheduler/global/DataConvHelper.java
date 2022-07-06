package com.yg.horus.scheduler.global;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.*;
import com.yg.horus.dto.ListCrawlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataConvHelper {

    private final SeedRepository seedRepository ;
    private final WrapperRepository wrapperRepository ;

    @Autowired
    public DataConvHelper(WrapperRepository _wrapperRepository, SeedRepository _seedRepository) {
        this.seedRepository = _seedRepository ;
        this.wrapperRepository = _wrapperRepository ;
    }

    public ListCrawlOption getListPageWrapRule(long seedNo) {
        TopSeeds topSeed = this.seedRepository.findBySeedNo(seedNo);
        ListCrawlOption option = new ListCrawlOption() ;
        option.setTargetSeedUrl(topSeed.getUrlPattern());

        List<WrapperRule> wRules = this.wrapperRepository.findBySeedNo(seedNo);
        wRules.forEach(rule -> {
            if(rule.getWrapType().equals(WrapType.LIST_URL_TOP_AREA_FILTER)) {
                option.setFilterDomGroupAttr(rule.getWrapVal());
            } else if(rule.getWrapType().equals(WrapType.LIST_URL_PATTERN_FILTER)) {
                option.setFilterCrawlUrlRxPattern(rule.getWrapVal());
            }
        });

        return option ;
    }

    public ContentsPageWrappingRule getContentPageWrapRule(long seedNo) {
        List<WrapperRule> lstWrapperRule = this.wrapperRepository.findBySeedNo(seedNo);
        ContentsPageWrappingRule wrapRule = new ContentsPageWrappingRule();

        for(WrapperRule wrule : lstWrapperRule) {
            if(wrule.getWrapType().equals(WrapType.CONT_DATE_ON_PAGE)) {
                wrapRule.setContDate(wrule.getWrapVal());
            } else if(wrule.getWrapType().equals(WrapType.CONT_MAIN_CONT)) {
                wrapRule.getContents().add(wrule.getWrapVal());
            } else if(wrule.getWrapType().equals(WrapType.CONT_TITLE_ON_PAGE)) {
                wrapRule.setTitleOnContents(wrule.getWrapVal());
            }
        }

        return wrapRule ;
    }

}
