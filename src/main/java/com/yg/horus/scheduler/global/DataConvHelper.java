package com.yg.horus.scheduler.global;

import com.yg.horus.crawl.ContentsPageWrappingRule;
import com.yg.horus.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataConvHelper {
//    @Autowired
//    private CrawlRepository crawlRepository = null ;
//    @Autowired
//    private SeedRepository seedRepository = null ;
//    @Autowired
    private final WrapperRepository wrapperRepository ;

    @Autowired
    public DataConvHelper(WrapperRepository _wrapperRepository) {
        wrapperRepository = _wrapperRepository ;
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
