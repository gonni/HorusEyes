package com.yg.horus.crawl.custom;

import com.yg.horus.crawl.CrawlBase;
import com.yg.horus.doc.DailyIndexDoc;
import com.yg.horus.doc.DailyInvestDoc;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000074 on 11/05/2021.
 */
@Service
public class NaverStockIndexCrawler extends CrawlBase {

    private static final DecimalFormat NumFormat = new DecimalFormat("###,###,###.##");
    private static final DecimalFormat IntNumFormat = new DecimalFormat("###,###;-###,###");
    private static final DecimalFormat UpDownPerFormat = new DecimalFormat("+##.##;-##.##");

    public NaverStockIndexCrawler() {
        ;
    }

    public List<DailyInvestDoc> getInvesters(String url) {
        ArrayList<DailyInvestDoc> lstInvesters = new ArrayList<>() ;

        try {
            Document dom = super.getPageDoc(url);
            Elements trs = dom.select("table.type_1 tr > td.date2");

            trs.forEach(element -> {
                System.out.println("--->>> " + element.text()) ;
                try {
                    DailyInvestDoc dailyInvestDoc = new DailyInvestDoc();
                    dailyInvestDoc.setTargetDt("20" + element.text());
                    String a = (element = element.nextElementSibling()).text();
                    System.out.println("a -> " + a);
                    dailyInvestDoc.setAnt(IntNumFormat.parse(a).intValue());
                    dailyInvestDoc.setForeigner(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setCompany(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setInvestBank(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setInsurance(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setInvestTrust(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setBank(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setEtcBank(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());
                    dailyInvestDoc.setPensionFund(IntNumFormat.parse((element = element.nextElementSibling()).text()).intValue());

                    lstInvesters.add(dailyInvestDoc) ;
                } catch(Exception ee) {
                    ee.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstInvesters ;
    }

//    public List<DailyIndexDoc> getIndexValue(@NonNull String url) {
    public List<DailyInvestDoc> getIndexValue(@NonNull String url) {
        ArrayList<DailyInvestDoc> lstDailyIndex = new ArrayList<>() ;

        Document dom = null;
        try {
            dom = super.getPageDoc(url);
            // table.type_1 tr:gt(1) > td.date / table.type_1 tr:has(td.date) > td:eq(0)
            Elements trs = dom.select("table.type_1 tr:has(td.date) ");

            trs.forEach(element -> {
                DailyInvestDoc did = new DailyInvestDoc() ;
                did.setTargetDt(element.select("td.date").text());
                did.setIndexValue(this.getFloatAmount(element, "td:eq(1)")) ;
                did.setDiffAmount(this.getFloatAmount(element, "td:eq(2)")) ;
                did.setUpDownPer(this.getPerValue(element, "td:eq(3)")) ;
                did.setTotalEa((int)this.getFloatAmount(element, "td:eq(4)")) ;
                did.setTotalVolume((int)this.getFloatAmount(element, "td:eq(5)")) ;

                if(element.select("td.rate_down").select("span.red02").size() > 0) {
                    did.setIndexUp(true);
                }

                lstDailyIndex.add(did);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstDailyIndex ;
    }

    private float getPerValue(@NonNull Element elem, @NonNull String selectQry) {

        try {
            String strVal = elem.select(selectQry).text() ;
            if(strVal != null && strVal.endsWith("%"))
                strVal = strVal.substring(0, strVal.length() -1);
            return UpDownPerFormat.parse(strVal).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1F;
    }

    private float getFloatAmount(@NonNull Element elem, @NonNull String selectQry) {
        try {
            return NumFormat.parse(elem.select(selectQry).text()).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1F;
    }

    public static void main(String ... v) throws Exception {
        String targetUrl = "https://finance.naver.com/sise/investorDealTrendDay.nhn?bizdate=20210511&sosok=&page=2";
        NaverStockIndexCrawler test = new NaverStockIndexCrawler();
        List<DailyInvestDoc> investers = test.getInvesters(targetUrl);

        System.out.println("Wrapped Result ------ ");
        investers.forEach(System.out::println);

//        String kospiUrl = "https://finance.naver.com/sise/sise_index_day.nhn?code=KOSPI&page=1";
//        test.getIndexValue(kospiUrl).forEach(System.out::println);

//        System.out.println("Parsed -> " + test.UpDownPerFormat.parse("+0.78").floatValue());
//        System.out.println(test.getPerValue());
        System.out.println("=====>" + IntNumFormat.parse("1,003").intValue());
    }

}
