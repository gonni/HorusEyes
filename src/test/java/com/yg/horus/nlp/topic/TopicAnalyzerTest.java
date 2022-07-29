package com.yg.horus.nlp.topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = "com.yg.horus.data")
@WebAppConfiguration
@PropertySource("classpath:application.properties")
public class TopicAnalyzerTest {
    @Autowired TopicAnalyzer topicAnalyzer ;

    @Test
    public void testDataLoad() {
        this.topicAnalyzer.displayLog();
        assert(true);
    }

    @Test
    public void testParse() {
        this.topicAnalyzer.getScore("사전예약/베타게임 시스템 고도화 관련 해서  사용자 PoC에서 검색/추천 기능 지원이 있습니다.");
        assert(true);
    }
}
