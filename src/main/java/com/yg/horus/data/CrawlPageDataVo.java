package com.yg.horus.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by a1000074 on 07/04/2021.
 */
@Deprecated
@Entity
@Getter
@NoArgsConstructor
@Table(name="CRAWL_PAGE_DATA")
public class CrawlPageDataVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pageDataNo ;
    private String pageText ;
    @CreationTimestamp
    private LocalDateTime regDate ;
    @Enumerated(EnumType.STRING)
    private CrawlStatus crawlStatus ;


    @Builder
    public CrawlPageDataVo(String pageText) {
        this.pageText = pageText;
    }
}
