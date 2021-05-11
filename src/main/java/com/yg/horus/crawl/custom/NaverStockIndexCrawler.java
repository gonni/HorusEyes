package com.yg.horus.crawl.custom;

import com.yg.horus.crawl.CrawlBase;
import com.yg.horus.doc.DailyIndexDoc;
import com.yg.horus.doc.DailyInvestersDoc;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000074 on 11/05/2021.
 */
public class NaverStockIndexCrawler extends CrawlBase {

    private static final DecimalFormat NumFormat = new DecimalFormat("###,###,###");

    public NaverStockIndexCrawler() {
        ;
    }

    public List<DailyInvestersDoc> getInvesters(String url) {
        ArrayList<DailyInvestersDoc> lstInvesters = new ArrayList<>() ;

        try {
            Document dom = super.getPageDoc(url);
            Elements trs = dom.select("table.type_1 tr > td.date2");

            trs.forEach(element -> {
                System.out.println("--->>> " + element.text()) ;
                try {
                    DailyInvestersDoc dailyInvestersDoc = DailyInvestersDoc.builder()
                            .pageDatetime(element.text())
                            .ant(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .foreigner(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .company(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .investBank(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .insurance(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .investTrust(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .bank(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .etcBank(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .pensionFund(NumFormat.parse((element = element.nextElementSibling()).text()).intValue())
                            .build();

                    lstInvesters.add(dailyInvestersDoc) ;
                } catch(Exception ee) {
                    ee.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstInvesters ;
    }


    public List<DailyIndexDoc> getIndexValue() {
        return null ;
    }

    public static void main(String ... v) {
        String targetUrl = "https://finance.naver.com/sise/investorDealTrendDay.nhn?bizdate=20210511&sosok=&page=2";
        NaverStockIndexCrawler test = new NaverStockIndexCrawler();

        List<DailyInvestersDoc> investers = test.getInvesters(targetUrl);

        System.out.println("Wrapped Result ------ ");
        investers.forEach(System.out::println);



        String kospiUrl = "https://finance.naver.com/sise/sise_index_day.nhn?code=KOSPI&page=1";
    }

}
