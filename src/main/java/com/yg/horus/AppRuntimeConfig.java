package com.yg.horus;

import com.yg.horus.scheduler.JobProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Created by a1000074 on 05/05/2021.
 */
@Slf4j
@Configuration
public class AppRuntimeConfig implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {
    @Autowired
    private JobProducer jobProducer = null;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Init System ..");
//        this.jobProducer.startWorker();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.info("Stop JobProducer Workers ..");
        this.jobProducer.stopWorker();
    }
}
