package com.yg.horus.doc;

import lombok.Builder;
import lombok.Data;

/**
 * Created by a1000074 on 11/05/2021.
 */
@Data
@Builder
public class DailyIndexDoc implements PageDoc {
    private String pageDatetime ;
    private int indexValue ;
    private int upDown ;
    private int upDownPer ;
    private int totalEa ;
    private int totalAmount ;

}
