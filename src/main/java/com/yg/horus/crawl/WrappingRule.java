package com.yg.horus.crawl;

import lombok.Data;

import java.util.List;

/**
 * Created by a1000074 on 25/03/2021.
 */
@Data
public class WrappingRule {
    private String titleOnTag ;
    private String titleOnContents ;
    private List<String> contents ;

}
