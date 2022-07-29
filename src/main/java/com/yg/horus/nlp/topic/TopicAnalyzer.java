package com.yg.horus.nlp.topic;

import com.yg.horus.data.TermDist;
import com.yg.horus.data.TermDistRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicAnalyzer {

    private final TermDistRepository termDistRepository ;
    private Map<String, Map<String, Double>> tdm ;

    private Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

    public TopicAnalyzer(@Autowired TermDistRepository _termDistRepository) {
        termDistRepository = _termDistRepository;
        loadTdm();
    }

    private void loadTdm() {
        if(tdm == null) tdm = new HashMap<>();
        this.tdm.clear();

        List<String> topics = this.termDistRepository.getLatestValidTopics();
        Map<String, Double> compTerms = new HashMap<>();
        topics.forEach(topic -> {
            List<TermDist> termVectors = termDistRepository.findLatestTermVectors(topic);
            compTerms.clear();
            termVectors.forEach(termDist -> {
                compTerms.put(termDist.getCompTerm(), termDist.getDistVal());
            });
            compTerms.put(topic, 1.0);

            tdm.put(topic, compTerms);
        });
    }

    public void displayLog() {
        this.tdm.forEach((k, v) -> {
            System.out.println("Topic: " + k + " -------");
            v.forEach((k1, v1) -> {
                System.out.println("\t" + k1 + "=" + v1);
            });
        });
    }

    public Map<String, Double> getScore(String sentence) {
        List<Token> tokenList = komoran.analyze(sentence).getTokenList();
        tokenList.forEach(System.out::println);

        tokenList.forEach(token -> {
            this.tdm.forEach((base, compMap) -> {

            });
        });

        return null ;
    }


}
