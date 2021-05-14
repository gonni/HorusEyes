package com.yg.horus.nlp.tokenizer;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.InputStream;

/**
 * Created by a1000074 on 14/05/2021.
 */
public class CustomKorTokenizerFactory implements TokenizerFactory {

    @Override
    public Tokenizer create(String s) {
        return null;
    }

    @Override
    public Tokenizer create(InputStream inputStream) {
        return null;
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess tokenPreProcess) {

    }

    @Override
    public TokenPreProcess getTokenPreProcessor() {
        return null;
    }
}
