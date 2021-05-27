package com.yg.horus.nlp.paragraphvectors;

import com.yg.horus.nlp.tokenizer.CustomKorTokenizerFactory;
import lombok.extern.slf4j.Slf4j;
import org.datavec.api.util.ClassPathResource;
import com.yg.horus.nlp.paragraphvectors.tools.Pair;
import com.yg.horus.nlp.paragraphvectors.tools.LabelSeeker;
import com.yg.horus.nlp.paragraphvectors.tools.MeansBuilder;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class TopicCluster {

    private ParagraphVectors paragraphVectors;
    private LabelAwareIterator iterator;
    private TokenizerFactory tokenizerFactory;

    @Value("${horus.nlp.pv.train.root}")
    private String trainDir = null ;
    @Value("${horus.nlp.pv.test.root}")
    private String testDir = null ;

    public static void main(String[] args) throws Exception {
        long ts = System.currentTimeMillis() ;
        TopicCluster app = new TopicCluster();
        app.makeParagraphVectors();
        app.checkUnlabeledData();


        log.info("Build time spent : {}", (System.currentTimeMillis() - ts));
        /*
                Your output should be like this:

                Document 'health' falls into the following categories:
                    health: 0.29721372296220205
                    science: 0.011684473733853906
                    finance: -0.14755302887323793

                Document 'finance' falls into the following categories:
                    health: -0.17290237675941766
                    science: -0.09579267574606627
                    finance: 0.4460859189453788

                    so,now we know categories for yet unseen documents
         */
    }

    public void makeParagraphVectors()  throws Exception {
//        ClassPathResource resource = new ClassPathResource("paravec/labeled");
//        ClassPathResource resource = new ClassPathResource("/home/jeff/dev/temp-dl/train");

        // build a iterator for our dataset
        iterator = new FileLabelAwareIterator.Builder()
//                .addSourceFolder(resource.getFile())
                .addSourceFolder(new File(this.trainDir))
                .build();

//        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory = new CustomKorTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());


        // ParagraphVectors training configuration
        paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(1)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();

        WordVectorSerializer.writeParagraphVectors(paragraphVectors, new File("pvec_" + System.currentTimeMillis() + ".mdl"));
    }

    public void checkUnlabeledData() throws IOException {
      /*
      At this point we assume that we have model built and we can check
      which categories our unlabeled document falls into.
      So we'll start loading our unlabeled documents and checking them
     */
//        ClassPathResource unClassifiedResource = new ClassPathResource("/home/jeff/dev/temp-dl/test");
        FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(new File(this.testDir))
                .build();

     /*
      Now we'll iterate over unlabeled data, and check which label it could be assigned to
      Please note: for many domains it's normal to have 1 document fall into few labels at once,
      with different "weight" for each.
     */
        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>)paragraphVectors.getLookupTable(),
                tokenizerFactory);
        LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

        while (unClassifiedIterator.hasNextDocument()) {
            LabelledDocument document = unClassifiedIterator.nextDocument();
            INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
            List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

         /*
          please note, document.getLabel() is used just to show which document we're looking at now,
          as a substitute for printing out the whole document name.
          So, labels on these two documents are used like titles,
          just to visualize our classification done properly
         */
            log.info("Document '" + document.getLabel() + "' falls into the following categories: ");
            for (Pair<String, Double> score: scores) {
                log.info("        " + score.getFirst() + ": " + score.getSecond());
            }
        }

    }
}