package com.yg.horus.nlp.topic;

import com.yg.horus.data.TermDist;
import com.yg.horus.data.TermDistRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopicAnalyzer {

    private final TermDistRepository termDistRepository ;
    private Map<String, Map<String, Double>> tdm ;

    private final Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

    public TopicAnalyzer(@Autowired TermDistRepository _termDistRepository) {
        termDistRepository = _termDistRepository;
        loadTdm();
    }

    private void loadTdm() {
        if(tdm == null) tdm = new HashMap<>();
        this.tdm.clear();

        List<String> topics = this.termDistRepository.getLatestValidTopics();

        topics.forEach(topic -> {
            List<TermDist> termVectors = termDistRepository.findLatestTermVectors(topic);
            Map<String, Double> compTerms = new HashMap<>();
            termVectors.forEach(termDist -> {
                compTerms.put(termDist.getCompTerm(), termDist.getDistVal());
            });
            compTerms.put(topic, 1.0);

            tdm.put(topic, compTerms);
        });

        log.info("TDM load completed .. {}", tdm.size());
    }

    public void displayLog() {
        this.tdm.forEach((k, v) -> {
            System.out.println("Topic: " + k + " -------");
            v.forEach((k1, v1) -> {
                System.out.println("\t" + k1 + "=" + v1);
            });
        });
    }

    public Map<String, Double> getAllScore(String sentence) {
//        List<Token> tokenList = komoran.analyze(sentence).getTokenList();
//        tokenList.forEach(System.out::println);
//
//        List<String> tokens = tokenList.stream()
//                .map(token -> token.getMorph())
//                .collect(Collectors.toList());

        Map<String, Double> mapRes = new HashMap<>();
        for (String topic : this.tdm.keySet()) {
            double score = this.getTopicScore(topic, sentence);
            mapRes.put(topic, score);
        }

        return mapRes ;
    }

    public double getTopicScore(String topic, String sentence) {
        List<Token> tokenList = komoran.analyze(sentence).getTokenList();
        List<String> tokens = tokenList.stream()
                .map(token -> token.getMorph())
                .collect(Collectors.toList());

        Map<String, Double> termScore = this.tdm.get(topic);
        double scoreSum = 0;

        for (String token : tokens) {
            if(token.equals(topic)) scoreSum += 1;

            if(termScore.containsKey(token)) {
                scoreSum += termScore.get(token);
            }
        }
        return scoreSum ;
    }
}
