package com.yg.horus.data;

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

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by a1000074 on 15/03/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
//@DataJpaTest
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CrawlRepositoryTest {
    @Autowired
    private CrawlRepository crawlRepository = null ;
    @Autowired
    private SeedRepository seedRepository = null ;

    @Test
    public void inOutCrawlTest() {
        String anchorText = "Test Title Anchor at " + System.currentTimeMillis() ;

        TopSeeds topSeeds = TopSeeds.builder()
                .urlPattern("http://navertest.com/news/today")
                .title("TEST SITE").build();

        this.seedRepository.save(topSeeds) ;

        CrawlUnit crawlUnit = CrawlUnit.builder()
                .url("https://www.naver.com/news/test.hml")
                .anchorText(anchorText)
                .status(CrawlStatus.INIT)
                .build();

        crawlUnit.setTopSeeds(topSeeds);


        CrawlUnit save = this.crawlRepository.save(crawlUnit);
        System.out.println("Inserted Crawl No -> " + save.getCrawlNo());

        save.setPageText("This is Text message for test created at " + System.currentTimeMillis() + " #id:"
                + save.getCrawlNo()) ;
        save.setStatus(CrawlStatus.SUCC);

        this.crawlRepository.save(save);

        System.out.println("CrawlNo ->" + save.getCrawlNo());

        this.crawlRepository.findAll().forEach(c -> {
            try {
                System.out.println("CrawlNo :" + c.getCrawlNo() + ", SeedClass :" + c.getTopSeeds());
            } catch(Exception e) {
                System.out.println("ERR :" + e.getMessage());
                e.printStackTrace();
            }
        });

        assertTrue(true) ;
    }

}