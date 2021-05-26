package com.yg.horus.nlp.paragraphvectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by a1000074 on 26/05/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
public class LabeledFilesInitializerTest {
    @Autowired
    private LabeledFilesInitializer labeledFilesInitializer = null ;

    @Test
    public void createLabeledFileTest() {
        this.labeledFilesInitializer.initLabeledFiles();
        assert(true);
    }

}
