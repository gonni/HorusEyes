package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.data.*;
import com.yg.horus.scheduler.listcrawl.CommonListCrawlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ManagedJobScheduler {
    @Autowired
    private CrawlRepository crawlRepository = null ;
    @Autowired
    private SeedRepository seedRepository = null ;
    @Autowired
    private WrapperRepository wrapperRepository = null ;

    private Map<Long, JobletProcessor> jobletProcessors = null ;

    public ManagedJobScheduler() {
        jobletProcessors = new HashMap<>();
    }

    public JobletProcessor getJobletProcessor(long seedId){
        JobletProcessor jobletProcessor = this.jobletProcessors.get(seedId);
        if(jobletProcessor == null) {
            JobletProcessor jp = new JobletProcessor("JobSeed :" + seedId);
            jp.startWorker();

            jobletProcessors.put(seedId, jp);
            jobletProcessor = jp;
        }

        return jobletProcessor;
    }

    public Job<List<CrawlDataUnit>> createManagedListCrawlJob(long seedNo, String strDtStamp, String endDtStamp) {
        TopSeeds seed = this.seedRepository.findBySeedNo(seedNo);
        log.info("--> Detected Job Creation for Seed : {}", seed);
        if(seed == null||
                seed.getWrapperRules() == null ||
                seed.getWrapperRules().size() < 1) {
            return null ;
        }

        List<WrapperRule> wrapperRules = seed.getWrapperRules();

        log.info("Crawl Seed : {}", seed);
        log.info("Crawl Wrapper : {}", wrapperRules);

        String grabUrlPattern = null ;
        String groupQry = null ;
        for(WrapperRule wr : wrapperRules) {
            WrapType wrapType = wr.getWrapType();

            if(wrapType.equals(WrapType.LIST_URL_TOP_AREA_FILTER)) {
                groupQry = wr.getWrapVal();
            } else if(wrapType.equals(WrapType.LIST_URL_PATTERN_FILTER)) {
                grabUrlPattern = wr.getWrapVal();
            }
        }

        CommonListCrawlJob job = new CommonListCrawlJob(
                seedNo,
                seed.getUrlPattern(),
                grabUrlPattern,
                groupQry,
                strDtStamp,
                endDtStamp,
                1);

        job.setCrawlRepository(this.crawlRepository);
        job.setJobletProcessor(this.getJobletProcessor(seedNo));

        return job ;
    }
}
