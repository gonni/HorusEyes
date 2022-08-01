package com.yg.horus.api;

import com.yg.horus.data.NewsClickRepository;
import com.yg.horus.nlp.topic.TopicAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class NewsCtr {
    private final NewsClickRepository newsClickRepository ;
    private final TopicAnalyzer topicAnalyzer ;

    @Autowired
    public NewsCtr(TopicAnalyzer _topicAnalyzer, NewsClickRepository _newsClickRepository) {
        topicAnalyzer = _topicAnalyzer ;
        newsClickRepository = _newsClickRepository ;
    }

    @RequestMapping("/svc/news/clicked")
    public @ResponseBody String clickNews(@RequestBody NewsUserClickReq newsUser){
        log.info("Detected Click-log : {}", newsUser);
        NewsClickRepository.NewsClick newsClick = new NewsClickRepository.NewsClick();
        newsClick.setNewsId(newsUser.getNewsNo());
        newsClick.setUserId(newsUser.getUserCode());

        return "SUCC" ;
    }



    @RequestMapping("/svc/news/recommended")
    public @ResponseBody RecommendedNewsRes getRecommendedNews(@RequestParam String userCode){

        return null ;
    }

    @Getter
    class RecommendedNewsRes {
        private String userCode ;
        private long idxTurn ;
        private List<NewsUnitRes> newsRecommended ;
    }

}

@Data
class NewsUnitRes {
    private long crawlNo ;
    private String title ;
    private double score ;
    private Date createdAt ;
}

@Data
class NewsUserClickReq {
    private String userCode ;
    private long newsNo ;
    private boolean isRecommended ;
}
