package com.yg.horus.nlp.tokenizer;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by a1000074 on 16/03/2021.
 */
public class CoreaTokenizer implements Tokenizer {
    private Iterator<String> tokenIter;
    private List<String> tokenList;
    private TokenPreProcess preProcess;

    private Komoran komoran = null ;
    private int tokenIdx = 0;
    public CoreaTokenizer(Komoran komoranTokenizer, String targetSentence) {
        this.komoran = komoranTokenizer;
        if(this.komoran == null) {
            komoran = new Komoran(DEFAULT_MODEL.LIGHT);
            komoran.setUserDic("/home/jeff/dev/temp/user.dic");
        }

        KomoranResult result = komoran.analyze(targetSentence);
        List<Token> tokenList = result.getTokenList();

        List<String> strTokens = tokenList.stream().map(token -> token.getMorph()).collect(Collectors.toList());
//        strTokens.forEach(System.out::println);
        this.tokenList = strTokens ;
        this.tokenIdx = 0;
    }

    @Override
    public boolean hasMoreTokens() {
        if(tokenIdx < this.tokenList.size()) return true ;
        return false;
    }

    @Override
    public int countTokens() {
        return this.tokenList.size();
    }

    @Override
    public String nextToken() {
        return this.tokenList.get(this.tokenIdx++);
    }

    public List<String> getTokens() {
        return this.tokenList;
    }

    public void setTokenPreProcessor(TokenPreProcess tokenPreProcess) {
        this.preProcess = tokenPreProcess;
    }

    public static void main(String ... v) {
        System.out.println("Active System ..") ;

//        String strToAnalyze = "최순실은 모르토바가 호갱국의 방사선사였던 그는 미래가 보장된 한국에서의 삶을 뒤로 하고 오무아무아는 2000년 초 미얀마에 정착했다";
//        CoreaTokenizer test = new CoreaTokenizer(strToAnalyze) ;


        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        komoran.setUserDic("/home/jeff/dev/temp/user.dic");
//        komoran.setUserDic("cfg/kor_dic.user");
//        komoran.setUserDic("/Users/a1000074/dev/dic/kor_dic.user");
        String strToAnalyze = "대한민국은 민주공화국이다.";
        strToAnalyze = "최순실은 행복한 모르토바가 호갱국의 방사선사였던 그는 미래가 보장된 " +
                "한국에서의 삶을 뒤로 하고 오무아무아는 2000년 초 미얀마에 정착 하지 않았다";

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

