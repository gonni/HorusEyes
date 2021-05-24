package com.yg.horus.data;

import com.yg.horus.crawl.custom.NaverStockIndexCrawler;
import com.yg.horus.doc.DailyIndexDoc;
import com.yg.horus.doc.DailyInvestDoc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by a1000074 on 13/05/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
//@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KospiRepositoryTest {
    @Autowired
    private KospiRepository kospiRepository = null ;

    @Autowired
    private NaverStockIndexCrawler naverStockIndexCrawler = null ;

//    @Test
//    public void testStoreNewData() {
//
//        String indexValueSeed = "https://finance.naver.com/sise/sise_index_day.nhn?code=KOSPI&page=1";
//        List<DailyInvestDoc> indexValue = this.naverStockIndexCrawler.getIndexValue(indexValueSeed);
//        indexValue.forEach(System.out::println);
//
//        assert(indexValue.size() > 5);
//
////        DailyInvestDoc save = this.kospiRepository.save(indexValue.get(0));
//        List<DailyInvestDoc> dailyInvestDocs = this.kospiRepository.saveAll(indexValue);
//
////        assert(save != null);
//
//        String invsterSeed = "https://finance.naver.com/sise/investorDealTrendDay.nhn?bizdate=20210514&sosok=&page=1";
//        List<DailyInvestDoc> investers = this.naverStockIndexCrawler.getInvesters(invsterSeed);
//
////        System.out.println("==>" + investers.get(0));
////        this.kospiRepository.save(investers.get(0));
//        this.kospiRepository.saveAll(investers);
//
//        assert(true);
//    }

    @Test
    public void testGetRangedIndexes() {
        List<DailyInvestDoc> rangedIndexes = this.kospiRepository.getRangedIndexes(1.0f, 2.0f);
        assert (rangedIndexes != null && rangedIndexes.size() > 0);

        rangedIndexes.forEach(System.out::println);
        assert (true);
    }
}
