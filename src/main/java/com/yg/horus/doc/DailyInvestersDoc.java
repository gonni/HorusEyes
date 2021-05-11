package com.yg.horus.doc;

import lombok.Builder;
import lombok.Data;

/**
 * Created by jeff on 21. 5. 10.
 */
@Data
@Builder
public class DailyInvestersDoc implements PageDoc {
    private String pageDatetime ;

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
