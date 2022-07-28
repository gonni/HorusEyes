package com.wealthfront.thompsonsampling;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.wealthfront.thompsonsampling.BatchedThompsonSampling;
import com.wealthfront.thompsonsampling.ObservedArmPerformance;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class HellThompson {
    public static void main(String ... v) {
        System.out.println("Active Thompson !!");

        List<ObservedArmPerformance> armPerformances = ImmutableList.of(
                new ObservedArmPerformance("a", 120, 100),
                new ObservedArmPerformance("b", 90, 101),
                new ObservedArmPerformance("c", 150, 120)
        );

        BanditPerformance banditPerformance = new BanditPerformance(armPerformances);
        BanditStatistics banditStatistics =
                getBandit(
                        new MersenneTwister(1),
                        100,
                        0.90,
                        0.01)
                .getBanditStatistics(banditPerformance);
        System.out.println("Result -> " + banditStatistics.getWeightsByVariant());



//        BanditPerformance banditPerformance = new BanditPerformance(ImmutableList.of(
//                new ObservedArmPerformance("a", 100, 100),
//                new ObservedArmPerformance("b", 99, 101)
//        ));
//        BanditStatistics banditStatistics = getBandit(new MersenneTwister(1), 10, 0.90, 0.01)
//                .getBanditStatistics(banditPerformance);
//        System.out.println("result ->" + banditStatistics.getVictoriousVariant());

//        double cumulativeRegret = banditPerformance.getCumulativeRegret();
//        System.out.println("Cum -> " + cumulativeRegret);

//        List<ObservedArmPerformance> armPerformances = ImmutableList.of(
//                new ObservedArmPerformance("a", 20, 1),
//                new ObservedArmPerformance("b", 10, 10),
//                new ObservedArmPerformance("c", 2, 10));
////                new ObservedArmPerformance("a", 100, 30),
////                new ObservedArmPerformance("b", 1000, 100),
////                new ObservedArmPerformance("c", 30, 10));
//
//        BanditPerformance banditPerformance = new BanditPerformance(armPerformances);
//        BanditStatistics banditStatistics = getBandit(new MersenneTwister(50), 53, 0.90, 0.01)
//                .getBanditStatistics(banditPerformance);
//
//        Map<String, Double> res = banditStatistics.getWeightsByVariant();
//        res.forEach((key, val) -> {
//            System.out.println("k:v=" + key + ":" + val);
//        } );

//        System.out.println("Update ..");
//        Map<String, ObservedArmPerformance> updated = ImmutableMap.of(
//                "a", new ObservedArmPerformance("a", 18L, 1L),
//                "b", new ObservedArmPerformance("b", 1000L, 1L),
//                "c", new ObservedArmPerformance("c", 1L, 10L));

//        banditPerformance = new BanditPerformance(updated);
//        BanditStatistics banditStatistics = getBandit(new MersenneTwister(1), 10, 0.90, 0.01)
//                .getBanditStatistics(banditPerformance);
//        res = banditStatistics.getWeightsByVariant();
//        res.forEach((key, val) -> {
//            System.out.println("k:v=" + key + ":" + val);
//        } );

//        WeightUpdate weightUpdate = new WeightUpdate(res, Optional.of("a"), updated);
//        System.out.println("Updated -> " + weightUpdate.getMaybeWinningVariant().get());
//        weightUpdate = new WeightUpdate(res, Optional.of("b"), updated);
//        System.out.println("Updated -> " + weightUpdate.getMaybeWinningVariant().get());
//        weightUpdate = new WeightUpdate(res, Optional.of("c"), updated);
//        System.out.println("Updated -> " + weightUpdate.getMaybeWinningVariant().get());
    }

    private static BatchedThompsonSampling getBandit(
            RandomEngine randomEngine,
            int numberOfDraws,
            double confidenceLevel,
            double experimentValueQuitLevel) {
        return new BatchedThompsonSampling() {
            @Override
            public RandomEngine getRandomEngine() {
                return randomEngine;
            }

            @Override
            public int getNumberOfDraws() {
                return numberOfDraws;
            }

            @Override
            public double getConfidenceLevel() {
                return confidenceLevel;
            }

            @Override
            public double getExperimentValueQuitLevel() {
                return experimentValueQuitLevel;
            }
        };
    }
}
