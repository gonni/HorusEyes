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

/**
 * Created by a1000074 on 20/04/2021.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
//@DataJpaTest
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("local")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SeedRepositoryTest {

    @Autowired
    private SeedRepository seedRepository = null ;

    @Test
    public void testSaveAll() {

        TopSeeds topSeeds = TopSeeds.builder()
                .urlPattern("http://test.test3.com")
                .title("jpa test3").build() ;
        topSeeds.addWrapperRule(new WrapperRule(WrapType.CONT_MAIN_CONT, "TestJPA1", "Test Only JPA"));
        topSeeds.addWrapperRule(new WrapperRule(WrapType.CONT_DATE_ON_PAGE, "TestJPA2", "Test Only JPA2"));
        topSeeds.addWrapperRule(new WrapperRule(WrapType.LIST_URL_PATTERN_FILTER, "TestJPA3", "Test Only JPA3"));

        this.seedRepository.save(topSeeds);
    }
}
