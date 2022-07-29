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

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
//@DataJpaTest
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("localhomeMac")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TermDistTest {
    @Autowired
    private TermDistRepository termDistRepository ;

    @Test
    public void testGetTermDists() {
        long latestTs = this.termDistRepository.getLatestGrpId();
        System.out.println("Latest Distance : " + latestTs) ;
        assertTrue(latestTs > 0);
        // ----
        List<TermDist> lstTerm = this.termDistRepository.findGrpTsTermVectors("경제", latestTs);
        lstTerm.forEach(System.out::println);
        assertTrue(lstTerm.size() == 200);
        // ----
        List<TermDist> president = this.termDistRepository.findLatestTermVectors("대통령");
        president.forEach(System.out::println);
        assertTrue(president.size() == 200);
        // ----

        List<String> latestValidTopics = this.termDistRepository.getLatestValidTopics();
        System.out.println("------------------------");
        latestValidTopics.forEach(System.out::println);
        assertTrue(latestValidTopics.size() > 15);

    }
}
