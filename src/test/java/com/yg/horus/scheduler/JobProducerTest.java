package com.yg.horus.scheduler;

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

import static org.junit.Assert.assertTrue;

/**
 * Created by a1000074 on 29/04/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobProducerTest {
    @Autowired
    private JobProducer jobProducer = null ;
    @Autowired
    private JobScheduler jobScheduler = null ;
    @Test
    public void testCrawlContentJob() {
//        this.jobScheduler.start();

        int cntScheduled = this.jobProducer.crawlContents(19L, 10);
        System.out.println("Successfully Scheduled .. " + cntScheduled);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}
