package com.yg.horus.doc;

import lombok.Data;

/**
 * Created by jeff on 21. 5. 10.
 */
@Data
public class DailyInvestersDoc implements PageDoc {
    private String pageDatetime ;
    private float ant ;
    private float foreigner ;
    private float company ;
    private float investBank ;
    private float insurance ;
    private float bank ;
    private float etcBank ;
    private float pensionFund ;

}
