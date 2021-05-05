package com.yg.horus.scheduler;

import com.yg.horus.crawl.CrawlDataUnit;
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
    private JobManager jobManager = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;


    public static void main(String ... v) {
        String seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=20200124&page=1";
        seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date='20210422'&page='7'";


        NaverStockJobManager test = new NaverStockJobManager();
        System.out.println("Yesterday -> " + test.getYesterday("20210501"));

        test.execSerialJobs(19L, "20210428", "20210425");
    }

    public void execSerialJobs(long seedNo, String start, String end) {
        String seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=%s&page=%s";

        String targetUrl = null; //String.format(seedUrlFormat, "AAAA", 9);

        System.out.println("TargetURL -> " + targetUrl);

        int id = 0;
        while(start.compareTo(end) >= 0) {
//            log.info(id++ + " --> " + start) ;

            for(int i=1;i<30;i++) {
                targetUrl = String.format(seedUrlFormat, start, i);
//                System.out.println("Target URL -> " + targetUrl);
                Job scJob = this.jobManager.createSeedListCrawlJob(targetUrl, seedNo);
//                System.out.println("Created Job -> " + scJob);

                try {
                    List<CrawlDataUnit> lstSeed = (List<CrawlDataUnit>)scJob.start();
//                    lstSeed.forEach(System.out::println);
                    log.info("id: {}, date: {} - index: {} -> crawled: {}", id++, start, i, lstSeed.size());

                    Thread.sleep(1013L);    // for delay crawl


                    if(lstSeed == null || lstSeed.size() == 0) {
//                        System.out.println("---------> Escape condition : " + i) ;
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
