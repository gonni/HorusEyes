package com.yg.horus.api;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import com.wealthfront.thompsonsampling.BanditPerformance;
import com.wealthfront.thompsonsampling.BanditStatistics;
import com.wealthfront.thompsonsampling.BatchedThompsonSampling;
import com.wealthfront.thompsonsampling.ObservedArmPerformance;
import com.yg.horus.data.ClickCount;
import com.yg.horus.data.NewsClickRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class MabCtr {
    private final NewsClickRepository newsClickRepository ;
    @Autowired
    public MabCtr(NewsClickRepository _newsClickRepository) {
        newsClickRepository = _newsClickRepository ;
    }

    @RequestMapping("/mab/runtime/score")
    public @ResponseBody MabResponse getNewsMabScore() {
        List<ClickCount> ropn = this.newsClickRepository.getClickCount("ROPN");
        List<ClickCount> rclk = this.newsClickRepository.getClickCount("RCLK");

        Map<Long, long[]> stat = new HashMap<>();
        List<ObservedArmPerformance> armPerformances = new ArrayList<>();
        ropn.forEach(cc -> {
            long newsId = cc.getNewsId();
            long cntOpnAll = getCount(newsId, ropn);
            long cntSuc = getCount(newsId, rclk);
            long[] res = {(cntOpnAll-cntSuc), cntSuc};
            stat.put(newsId, res);
            armPerformances.add(new ObservedArmPerformance("N_"+ newsId, cntSuc, (cntOpnAll- cntSuc)));
        });

        BanditPerformance banditPerformance = new BanditPerformance(armPerformances);
        BanditStatistics banditStatistics =
                getBandit(
                        new MersenneTwister(1),
                        100,
                        0.90,
                        0.01)
                        .getBanditStatistics(banditPerformance);

        MabResponse response = new MabResponse();
        response.setClick(stat);
        response.setMabWeight(banditStatistics.getWeightsByVariant());

        return response ;
    }

    private static BatchedThompsonSampling getBandit(
            RandomEngine randomEngine,
            int numberOfDraws,
            double confidenceLevel,
            double experimentValueQuitLevel) {
        return new BatchedThompsonSampling() {
            public RandomEngine getRandomEngine() {
                return randomEngine;
            }

            public int getNumberOfDraws() {
                return numberOfDraws;
            }

            public double getConfidenceLevel() {
                return confidenceLevel;
            }

            public double getExperimentValueQuitLevel() {
                return experimentValueQuitLevel;
            }
        };
    }

    private long getCount(long newsId, List<ClickCount> lst) {
        Optional<ClickCount> first = lst.stream().filter(a -> a.getNewsId() == newsId).findFirst();
        if(first.isPresent())
            return first.get().getCntClick();
        return 0;
    }
}

@Data
class MabResponse {
    Map<Long, long[]> click ;
    Map<String, Double> mabWeight ;
}