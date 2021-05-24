package com.yg.horus.nlp.paragraphvectors;

import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;

/**
 * Created by a1000074 on 24/05/2021.
 */
public class CustomLabelAwareIterator  implements LabelAwareIterator {
    @Override
    public boolean hasNextDocument() {
        return false;
    }

    @Override
    public LabelledDocument nextDocument() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public LabelsSource getLabelsSource() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public LabelledDocument next() {
        return null;
    }
}
