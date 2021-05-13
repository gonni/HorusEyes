package com.yg.horus.doc;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jeff on 21. 5. 10.
 */
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

}
