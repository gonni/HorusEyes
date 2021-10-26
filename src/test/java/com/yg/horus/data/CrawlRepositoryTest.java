package com.yg.horus.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by a1000074 on 15/03/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CrawlRepositoryTest {
    @Autowired
    private CrawlRepository crawlRepository = null ;
    @Autowired
    private SeedRepository seedRepository = null ;

//    @Test
//    public void getGetRange() {
//        List<CrawlUnit> rangeUnits = this.crawlRepository.getDateRangedUnits("2021-05-01", "2021-05-02");
//        assert(rangeUnits.size() > 0);
//        System.out.println("Count of Range Data = " + rangeUnits.size());
//        rangeUnits.forEach(System.out::println);
//
//        System.out.println("---------------- RANGED DATA SIZE :" + rangeUnits.size());
//
//        List<CrawlUnit> target = this.crawlRepository.findByPageDateStartsWith("2021-05-01");
//        System.out.println("================ TARGET DATA SIZE :" + target.size());
//
//        assert (true);
//    }

    @Test
    public void testInOutCrawl() {
        String anchorText = "Test Title Anchor at " + System.currentTimeMillis() ;

//        TopSeeds topSeeds = TopSeeds.builder()
//                .urlPattern("http://navertest.com/news/today/" + System.currentTimeMillis())
//                .title("TEST_SITE").build();
//
//        this.seedRepository.save(topSeeds) ;
        TopSeeds topSeeds = TopSeeds.builder().build();
        topSeeds.setSeedNo(27);


        CrawlUnit crawlUnit = CrawlUnit.builder()
                .url("https://www.naver.com/news/"+System.currentTimeMillis()+"test3.hml")
                .anchorText(anchorText)
                .status(CrawlStatus.INIT)
                .build();
        crawlUnit.setPageDate("2021-01-24 15:12");
        crawlUnit.setPageText("TEST at ");
        crawlUnit.setTopSeeds(topSeeds);



        CrawlUnit save = this.crawlRepository.save(crawlUnit);
        System.out.println("Inserted Crawl No -> " + save.getCrawlNo() + " --> " + topSeeds.toString());

        List<CrawlUnit> icu = this.crawlRepository.findByStatusAndTopSeedsSeedNoOrderByCrawlNoDesc(CrawlStatus.INIT,
                save.getTopSeeds().getSeedNo(), PageRequest.of(0, 2));
        icu.forEach(System.out::println);


//        save.setPageText("This is Text message for test created at " + System.currentTimeMillis() + " #id:"
//                + save.getCrawlNo()) ;
//        save.setStatus(CrawlStatus.SUCC);
//
//        this.crawlRepository.save(save);
//List<CrawlUnit> findByStatusOrderByCrawlNoDesc(CrawlStatus crawlStatus, Pageable pageable) ;
//        System.out.println("CrawlNo ->" + save.getCrawlNo());
//
//        this.crawlRepository.findAll().forEach(c -> {
//            try {
//                System.out.println("CrawlNo :" + c.getCrawlNo() + ", SeedClass :" + c.getTopSeeds());
//            } catch(Exception e) {
//                System.out.println("ERR :" + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//
//        List<CrawlUnit> byUrl = this.crawlRepository.findByUrl("https://www.naver.com/news/test1.hml");
//        if(byUrl != null && byUrl.size() > 0)
//            System.out.println("Find -> " + byUrl.get(0).getUrl());
//        else
//            System.out.println("Failed ..");

        assertTrue(true) ;
    }

//    @Test
//    public void testCrawlSeed() {
//        System.out.println("Test SeedRep ..");
//
//        TopSeeds bySeedNo = this.seedRepository.findBySeedNo(19L);
//        assertTrue(bySeedNo != null) ;
//        assertTrue(bySeedNo.getWrapperRules().size() > 0);
//
//        System.out.println("Read Result ..");
//        bySeedNo.getWrapperRules().forEach(System.out::println);
//
//        System.out.println("Test completed for SeedRep ..");
//    }

//    @Test
//    public void testFind() {
//        CrawlUnit oneByUrl = this.crawlRepository.findOneByUrl("https://www.naver.com/news/test3.hml");
//        System.out.println("Result ->" + oneByUrl);
//        assertTrue(oneByUrl != null);
//    }

//    @Test
//    public void testLimit() {
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        List<CrawlUnit> crawlUnits = this.crawlRepository.findByStatusOrderByCrawlNoDesc(CrawlStatus.IURL, pageRequest);
//
//        crawlUnits.stream().forEach(System.out::println);
//
//        assertTrue(crawlUnits.size() == 5);
//    }

}