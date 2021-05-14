package com.yg.horus.doc;

import lombok.Data;

import javax.persistence.Column;
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
    @Column(updatable = false)
    protected float indexValue ;
    @Column(updatable = false)
    protected boolean indexUp;
    @Column(updatable = false)
    protected float diffAmount ;
    @Column(updatable = false)
    protected float upDownPer ;
    @Column(updatable = false)
    protected int totalEa ;
    @Column(updatable = false)
    protected int totalVolume ;

}
