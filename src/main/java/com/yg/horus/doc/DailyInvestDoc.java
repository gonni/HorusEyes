package com.yg.horus.doc;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by jeff on 21. 5. 10.
 */
@Slf4j
@Entity
@Table(name = "CRAWL_KOSPI")
@Setter
@Getter
@ToString
@DynamicUpdate
public class DailyInvestDoc extends DailyIndexDoc implements PageDoc {
    @Id
    private String targetDt ;

    private int ant ;
    private int foreigner ;
    private int company ;
    private int investBank ;
    private int insurance ;
    private int investTrust ;
    private int bank ;
    private int etcBank ;
    private int pensionFund ;

    @PrePersist
    private void prePresistLog() {
        log.info("try to save kospi data : {}", toString()) ;
    }

    @PostPersist
    private void postPresistLog() {
        log.info("saved kospi data : {}", toString()) ;
    }
}
