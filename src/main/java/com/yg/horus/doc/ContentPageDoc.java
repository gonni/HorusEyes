package com.yg.horus.doc;

import lombok.Data;

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
    private String dateOnContent ;
    private String title ;
    private String titleOnContent ;
    private String content ;

    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        sb.append("title :").append(this.title).append("\n");
        sb.append("title on content :").append(this.titleOnContent).append("\n");
        sb.append("doc timeStamp :").append(this.dateOnContent).append("\n");
        sb.append("content :").append(this.content).append("\n") ;
        return sb.toString() ;
    }
}
