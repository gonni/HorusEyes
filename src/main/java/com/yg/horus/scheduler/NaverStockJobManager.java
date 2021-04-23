package com.yg.horus.scheduler;

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

        test.execSerialJobs(19L, "20210423", "20210331");
    }

    public void execSerialJobs(long seedNo, String start, String end) {
        String seedUrlFormat = "https://finance.naver.com/news/news_list.nhn?" +
                "mode=LSS3D&section_id=101&section_id2=258&section_id3=402&date=%s&page=%s";

        String targetUrl = String.format(seedUrlFormat, "AAAA", 9);

        System.out.println("TargetURL -> " + targetUrl);

        int id = 0;
        while(start.compareTo(end) >= 0) {
            System.out.println(id++ + " --> " + start) ;
            start = this.getYesterday(start) ;

            for(int i=0;i<30;i++) {

            }

        }


    }

    public String getYesterday(String yyyymmdd) {
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
