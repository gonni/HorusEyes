package com.yg.horus.scheduler.custom;

import com.yg.horus.crawl.CrawlDataUnit;
import com.yg.horus.crawl.custom.NaverStockIndexCrawler;
import com.yg.horus.data.KospiRepository;
import com.yg.horus.doc.DailyInvestDoc;
import com.yg.horus.scheduler.Job;
import com.yg.horus.scheduler.JobBuilder;
import com.yg.horus.scheduler.JobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jeff on 21. 4. 16.
 */
@Slf4j
@Service
public class NaverStockJobManager {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;

    @Autowired
    private JobBuilder jobBuilder = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;
    @Autowired
    private NaverStockIndexCrawler naverStockIndexCrawler = null ;
    @Autowired
    private KospiRepository kospiRepository = null ;

    public static void main(String ... v) {
        String seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20200124&page=1";
        seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date='20210422'&page='7'";


        NaverStockJobManager test = new NaverStockJobManager();
        System.out.println("Yesterday -> " + test.getYesterday("20210501"));

        test.execSerialJobs(19L, "20210428", "20210425");
    }

    public void execSerialJobsCrawlKospiInvest() {
        for(int i = 0;i < 1362; i++) {
            List<DailyInvestDoc> idxValues = this.naverStockIndexCrawler.getIndexValue(i);
            log.info("Crawl KOSPI Values : {}", idxValues.get(0));

            this.kospiRepository.saveAll(idxValues);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i=1;i<=405;i++) {
            List<DailyInvestDoc> investers = this.naverStockIndexCrawler.getInvesters("20210514", i);
            log.info("Crawl KOSPI Invester : {}", investers.get(0));

            this.kospiRepository.saveAll(investers);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.info("KOSPI Crawl Job Completed ..");
    }

    public void execSerialJobs(long seedNo, String start, String end) {
        //https://finance.naver.com/news/news_list.nhn?
        // mode=LSS3D&section_id=101&section_id2=258&section_id3=401&date=20210504
        String seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=%s&page=%s";

        String targetUrl = null; //String.format(seedUrlFormat, "AAAA", 9);

        int id = 0;
        while(start.compareTo(end) >= 0) {

            for(int i=1;i<30;i++) {
                targetUrl = String.format(seedUrlFormat, start, i);
                Job scJob = this.jobBuilder.createSeedListCrawlJob(targetUrl, seedNo);

                try {
                    List<CrawlDataUnit> lstSeed = (List<CrawlDataUnit>)scJob.start();
                    log.info("id: {}, date: {} - index: {} -> crawled: {}", id++, start, i, lstSeed.size());

                    Thread.sleep(1013L);    // for delay crawl


                    if(lstSeed == null || lstSeed.size() == 0) {
                        break ;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    break ;
                }
            }

            start = this.getYesterday(start) ;
        }
    }

    private String getYesterday(String yyyymmdd) {
        try {
            Date date = sdf.parse(yyyymmdd);

            Calendar day = Calendar.getInstance();
            day.setTime(date);
            day.add(Calendar.DATE, -1);

            return this.sdf.format(day.getTime()) ;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null ;
    }


}
