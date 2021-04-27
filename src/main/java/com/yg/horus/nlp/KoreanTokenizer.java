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
        komoran.setUserDic("cfg/kor_dic.user");
//        komoran.setUserDic("/Users/a1000074/dev/dic/kor_dic.user");
        String strToAnalyze = "대한민국은 민주공화국이다.";
        strToAnalyze = "최순실은 모르토바가 호갱국의 방사선사였던 그는 미래가 보장된 한국에서의 삶을 뒤로 하고 오무아무아는 2000년 초 미얀마에 정착했다";

        KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);

        System.out.println(analyzeResultList.getPlainText());

        List<Token> tokenList = analyzeResultList.getTokenList();
        for (Token token : tokenList) {
            System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());

//            // Only NN
//            String pos = token.getPos();
//            String tk = token.getMorph();
//
//            if(pos != null && pos.startsWith("NN")) {
//                System.out.println(pos + " -> " + tk) ;
//            }


        }

    }
}

