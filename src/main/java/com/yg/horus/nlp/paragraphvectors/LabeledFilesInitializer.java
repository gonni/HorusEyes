package com.yg.horus.nlp.paragraphvectors;

import com.yg.horus.data.CrawlRepository;
import com.yg.horus.data.CrawlUnit;
import com.yg.horus.data.KospiRepository;
import com.yg.horus.doc.DailyIndexDoc;
import com.yg.horus.doc.DailyInvestDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jeff on 21. 5. 24.
 */
@Slf4j
@Service
public class LabeledFilesInitializer {
    private final DateFormat dateFormatKospi = new SimpleDateFormat("yyyy.MM.dd") ;
    private final DateFormat dateFormatNews = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
    private final DateFormat dateFormatNewsKey = new SimpleDateFormat("yyyy-MM-dd") ;
    private final DateFormat dateFormatNewsFs = new SimpleDateFormat("yyyyMMdd");

    @Value("${horus.nlp.pv.train.up-dir}")
    private String upNewsDir = null ;
    @Value("${horus.nlp.pv.train.down-dir}")
    private String downNewsDir = null ;

    @Autowired
    private KospiRepository kospiRepository = null ;

    @Autowired
    private CrawlRepository crawlRepository = null ;

    public LabeledFilesInitializer() {
        ;
    }

    public void initLabeledFiles() {
        List<DailyInvestDoc> rangedIndexes = this.kospiRepository.getRangedIndexes(3f, 15f);
        log.info("Count of Target Days : {}", rangedIndexes.size());
        this.createFiles(rangedIndexes, this.upNewsDir);

        rangedIndexes = this.kospiRepository.getRangedIndexes(-15f, -3f);
        log.info("Count of Target Days : {}", rangedIndexes.size());
        this.createFiles(rangedIndexes, this.downNewsDir);

    }

    private void createFiles(List<DailyInvestDoc> rangedIndexes, String targetDir) {

        File dir = new File(targetDir);
//        log.info("delete files : {}", dir.listFiles().length);
        for(File file : dir.listFiles()) {
            file.delete();
        }

        for(DailyIndexDoc index : rangedIndexes) {
            try {
                Date kospiDate = dateFormatKospi.parse(index.getTargetDt());
                String strNewsDate = this.dateFormatNewsKey.format(kospiDate);
                String strNewsDateFs = this.dateFormatNewsFs.format(kospiDate);

                List<CrawlUnit> targetNews = this.crawlRepository.findByPageDateStartsWith(strNewsDate);
                log.info(strNewsDate + " = " + targetNews.size());
                int i = 0, j = 0;
                for(CrawlUnit cu : targetNews) {
                    String pageText = cu.getPageText();
                    if(pageText == null || pageText.length() <= 0) continue;

                    String fileContents = targetDir + strNewsDateFs + "_cont_" + i++;
                    log.info("Write File : {}", fileContents);
                    Files.write(Paths.get(fileContents), pageText.getBytes());
                }
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
