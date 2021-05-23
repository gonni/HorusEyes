package com.yg.horus.nlp.tokenizer;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by a1000074 on 14/05/2021.
 */
public class CustomKorTokenizerFactory implements TokenizerFactory {
    private static Komoran komoran = null ;

    private TokenPreProcess tokenPreProcess = null ;

    public CustomKorTokenizerFactory() {
        if(komoran == null) {
            komoran = new Komoran(DEFAULT_MODEL.LIGHT);
            komoran.setUserDic("/home/jeff/dev/temp/user.dic");
        }
    }

    @Override
    public Tokenizer create(String s) {
        return new CoreaTokenizer(komoran, s);
    }

    @Override
    public Tokenizer create(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null ;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CoreaTokenizer(komoran, sb.toString());
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess tokenPreProcess) {
        this.tokenPreProcess = tokenPreProcess ;
    }

    @Override
    public TokenPreProcess getTokenPreProcessor() {
        return this.tokenPreProcess ;
    }
}
