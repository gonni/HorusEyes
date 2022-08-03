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

@SpringBootTest
@RunWith(SpringRunner.class)
//@DataJpaTest
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@ActiveProfiles("localhomeMac")
@PropertySource("classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NewClickRepoTest {
    @Autowired
    private NewsClickRepository newsClickRepository ;

    @Test
    public void getNewsClickStatistics() {
        List<ClickCount> ropn = this.newsClickRepository.getClickCount("ROPN");
        ropn.forEach(a -> System.out.println(a));

        assert(ropn != null && ropn.size() > 0);
    }
//    @Test
//    public void testSaveData() {
//        NewsClickRepository.NewsClick testCL = new NewsClickRepository.NewsClick();
//        testCL.setNewsId(1);
//        testCL.setUserId("127.0.0.1");
//        testCL.setPageCd("TEST");
//        newsClickRepository.save(testCL);
//
//        assert(true);
//    }
}
