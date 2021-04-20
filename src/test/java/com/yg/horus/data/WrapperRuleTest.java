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
public class WrapperRuleTest {
    @Autowired
    private WrapperRepository wrapperRepository = null ;

    @Test
    public void testInsertSample() {
        WrapperRule wrapperRule = WrapperRule.builder()
                .seedNo(19)
                .wrapName("ListFiltering")
                .wrapType(WrapType.LIST_URL_PATTERN_FILTER)
                .wrapVal("^(https:\\/\\/finance.naver.com\\/news\\/news_read.nhn\\?article_id=).*$")
                .build();

        this.wrapperRepository.save(wrapperRule) ;
    }

}
