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
 * Created by a1000074 on 20/04/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CrawlJobTest {
    @Autowired
    private JobManager jobManager = null ;

    @Test
    public void testListCrawlJob() {
        Job newJob = this.jobManager.createSeedListCrawlJob(19);
        System.out.println("Job Creation Test Completed for " + newJob);

        newJob.start();

        System.out.println("Successfully Job Completed ..");
        assertTrue(newJob.getStatus().equals(JobStatus.COMPLETED));
//        newJob = this.jobManager.createSeedListCrawlJob(20);
//        System.out.println("Job Creation Test Completed for " + newJob);
    }

}
