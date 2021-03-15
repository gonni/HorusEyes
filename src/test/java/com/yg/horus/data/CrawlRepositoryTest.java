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

import java.util.List;

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

    @Test
    public void inOutCrawlTest() {
        String anchorText = "Test Title Anchor at " + System.currentTimeMillis();


        CrawlUnitVo crawlUnitVo = CrawlUnitVo.builder()
                .url("https://www.naver.com/news/test.hml")
                .anchorText(anchorText)
                .status("INIT")
                .build();


        CrawlUnitVo save = this.crawlRepository.save(crawlUnitVo);
        System.out.println("Inserted Crawl No -> " + save.getCrawlNo());

        assertTrue(true) ;
    }

}