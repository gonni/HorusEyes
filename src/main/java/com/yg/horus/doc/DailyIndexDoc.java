package com.yg.horus.doc;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * Created by a1000074 on 11/05/2021.
 */
//@Entity
//@Table(name = "CRAWL_KOSPI")
@MappedSuperclass
@Data
public class DailyIndexDoc implements PageDoc {
    @Id
    protected String targetDt ;

    protected float indexValue ;
    protected boolean indexUp;
    protected float diffAmount ;
    protected float upDownPer ;
    protected int totalEa ;
    protected int totalVolume ;

}
