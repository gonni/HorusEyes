package com.yg.horus.nlp.topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

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
        String sentence = "한라산 탐방로 통제...제주 일부 여객선 운항 중단";
        Map<String, Double> score = this.topicAnalyzer.getAllScore(sentence);
        score.forEach((k, v) -> {
            System.out.println(k + "=>" + v);
        });

        System.out.println("-----------");
        double topicScore = this.topicAnalyzer.getTopicScore("대통령", sentence);
        System.out.println("topicScore :" + topicScore);

        assert(true);
    }
}
