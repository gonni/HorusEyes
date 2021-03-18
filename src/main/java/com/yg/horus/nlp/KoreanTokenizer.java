package com.yg.horus.nlp;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.List;


/**
 * Created by a1000074 on 16/03/2021.
 */
public class KoreanTokenizer {

    public static void main(String ... v) {
        System.out.println("Active System ..") ;

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        String strToAnalyze = "대한민국은 민주공화국이다.";
        strToAnalyze = "방사선사였던 그는 미래가 보장된 한국에서의 삶을 뒤로 하고 2000년 초 미얀마에 정착했다";

        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);

        System.out.println(analyzeResultList.getPlainText());

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
//            System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());


            String pos = token.getPos();
            String tk = token.getMorph();

            if(pos != null && pos.startsWith("NN")) {
                System.out.println(pos + " -> " + tk) ;
            }

        }

    }
}

