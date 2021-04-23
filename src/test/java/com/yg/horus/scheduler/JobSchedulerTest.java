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
import java.util.Date;

/**
 * Created by a1000074 on 22/04/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
//@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobSchedulerTest {

    @Autowired
    private JobScheduler jobScheduler ;

    public JobSchedulerTest() {
        ;
    }

    @Test
    public void testRunningJob() {
        for(int i=0;i<10;i++) {
            this.jobScheduler.execute(new Job(){

                @Override
                public void start() {
                    long ts = System.currentTimeMillis();
                    System.out.println(ts + "# Started at " + new Date(System.currentTimeMillis()));
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ts + "# Stopped at " + new Date(System.currentTimeMillis()));
                }

                @Override
                public JobStatus getStatus() {
                    return null;
                }
            });

            System.out.println("Schedulered Job #" + i);
        }

        try {
            Thread.sleep(20000);
            System.out.println("Sleep Completed ..");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}
