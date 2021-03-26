package com.yg.horus.doc;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by a1000074 on 27/01/2021.
 */
@Data
public class ContentPageDoc implements PageDoc {

    public enum PageDocStatus {
        INIT,
        FAIL,
        SUCC
    }

    private PageDocStatus status = PageDocStatus.INIT;
    private Date issueAt = new Date(System.currentTimeMillis()) ;
    private String title ;
    private String titleOnContent ;
    private String content ;

}
