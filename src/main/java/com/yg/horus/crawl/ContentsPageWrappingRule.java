package com.yg.horus.crawl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000074 on 25/03/2021.
 */
@Data
public class ContentsPageWrappingRule {
    private String titleOnTag ;
    private String titleOnContents ;
    private List<String> contents ;
    private String contDate ;

    public ContentsPageWrappingRule() {
        this.contents = new ArrayList<>() ;
    }
}
