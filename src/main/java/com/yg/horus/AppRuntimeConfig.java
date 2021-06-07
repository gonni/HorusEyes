package com.yg.horus;

import com.yg.horus.scheduler.JobProducer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * Created by a1000074 on 05/05/2021.
 */
@Slf4j
@Configuration
//@EnableAutoConfiguration
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



//    @Bean
//    public SessionFactory sessionFactory(@Autowired EntityManagerFactory factory) {
//        log.info("Init SessionFactory ..");
//        if (factory.unwrap(SessionFactory.class) == null) {
//            throw new NullPointerException("factory is not a hibernate factory");
//        }
//        return factory.unwrap(SessionFactory.class);
//    }

}
