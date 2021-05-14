package com.yg.horus.nlp.paragraphvectors.word2vec;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlUnit;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.KoreanTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by jeff on 21. 5. 3.
 */
@Slf4j
@Service
public class Word2vecModeler {

    @Autowired
    private CrawlRepository crawlRepository = null;

    public Word2vecModeler() {
        ;
    }

    public void buildW2vFile(@NonNull String startDt, @NonNull String endDt, String toFilePath) {

        SentenceIterator si = new SentenceIterator() {
            int cursorIndex = 0 ;
            List<CrawlUnit> targetUnits =  null;
            SentencePreProcessor preProcessor = null ;

//            public SentenceIterator() {
//                this.targetUnits = crawlRepository.getDateRangedUnits(startDt, endDt);
//            }
            @Override
            public String nextSentence() {
                if(targetUnits == null)
                    this.targetUnits = crawlRepository.getDateRangedUnits(startDt, endDt);

                if(targetUnits.size() > cursorIndex)
                    return this.targetUnits.get(cursorIndex++).getPageText();
                else
                    return null ;
            }

            @Override
            public boolean hasNext() {
                if(targetUnits == null)
                    this.targetUnits = crawlRepository.getDateRangedUnits(startDt, endDt);

                if(targetUnits.size() > cursorIndex) return true ;
                return false;
            }

            @Override
            public void reset() {
                targetUnits = crawlRepository.getDateRangedUnits(startDt, endDt);
            }

            @Override
            public void finish() {
                this.cursorIndex = 0;
                this.targetUnits = null ;
            }

            @Override
            public SentencePreProcessor getPreProcessor() {
                return this.preProcessor;
            }

            @Override
            public void setPreProcessor(SentencePreProcessor preProcessor) {
                this.preProcessor = preProcessor;
            }
        };





    }


    public static void main(String ... v) throws Exception {
        System.out.println("Active Service ..");

        // Strip white space before and after for each line
        org.deeplearning4j.text.sentenceiterator.SentenceIterator iter = new BasicLineIterator(
                new File("/Users/a1000074/dev/temp-comment/latest_comment_5e6_20200107.txt"));
        // Split on white spaces in the line to get words
//        TokenizerFactory t = new DefaultTokenizerFactory();
//        t.setTokenPreProcessor(new CommonPreprocessor());

        TokenizerFactory t = new KoreanTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        // manual creation of VocabCache and WeightLookupTable usually isn't necessary
        // but in this case we'll need them
        InMemoryLookupCache cache = new InMemoryLookupCache();
        WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>()
                .vectorLength(100)
                .useAdaGrad(false)
                .cache(cache)
                .lr(0.025f).build();

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .epochs(1)
                .layerSize(100)
                .seed(42)
                .windowSize(6)
                .iterate(iter)
                .tokenizerFactory(t)
                .lookupTable(table)
                .vocabCache(cache)

                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();


        Collection<String> lst = vec.wordsNearest("설치", 20);
        System.out.println("Closest words to 'day' on 1st run: " + lst);

        lst = vec.wordsNearest("쿠폰", 20);
        System.out.println("Closest words to 'day' on 1st run: " + lst);

        WordVectorSerializer.writeWord2VecModel(vec, new File("/Users/a1000074/dev/temp-comment/model/w2v_comment_5e6.txt"));

        System.out.println("Job Completed");
    }
}
