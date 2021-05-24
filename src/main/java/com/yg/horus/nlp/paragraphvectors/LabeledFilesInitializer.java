package com.yg.horus.nlp.paragraphvectors;

import com.yg.horus.data.KospiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jeff on 21. 5. 24.
 */
@Service
public class LabeledFilesInitializer {

    @Autowired
    private KospiRepository kospiRepository = null ;

    public LabeledFilesInitializer() {
        ;
    }



}
