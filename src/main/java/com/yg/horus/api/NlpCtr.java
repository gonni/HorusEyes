package com.yg.horus.api;


import com.yg.horus.nlp.topic.TopicAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class NlpCtr {

    private final TopicAnalyzer topicAnalyzer ;

    @Autowired
    public NlpCtr(TopicAnalyzer _topicAnalyzer) {
        this.topicAnalyzer = _topicAnalyzer;
    }

    @RequestMapping("/nlp/topic/score")
    public @ResponseBody TopicScoreResponse getTopicScore(@RequestBody TopicScoreRequest request) {
        log.info("Request with {}", request);
        Map<String, Double> allScore = this.topicAnalyzer.getAllScore(request.getSentence());

        final TopicScoreResponse response = new TopicScoreResponse();
        response.sentence = request.getSentence();

        allScore.forEach((k, v) -> {
            TermScore ts = new TermScore(k, v);
            response.getTermScores().add(ts);
        });

        return response ;
    }

    @Getter
    class TopicScoreResponse {
        private String sentence ;
        private List<TermScore> termScores = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    class TermScore {
        private String topic ;
        private Double score ;
    }
}

@Data
class TopicScoreRequest {
    private String sentence ;
}
