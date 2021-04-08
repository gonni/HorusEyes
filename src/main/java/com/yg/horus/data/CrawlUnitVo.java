package com.yg.horus.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Created by a1000074 on 15/03/2021.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name="CRAWL_UNIT")
public class CrawlUnitVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crawlNo;
    private String url ;
    private String anchorText ;
    @Setter
    @Enumerated(EnumType.STRING)
    private CrawlStatus status ;
    @CreationTimestamp
    private LocalDateTime regDate ;
    @CreationTimestamp
    private LocalDateTime updDate ;
    @Setter
    private String pageText ;

    @Builder
    public CrawlUnitVo(String url, String anchorText, CrawlStatus status) {
        this.url = url ;
        this.anchorText = anchorText ;
        this.status = status;
    }
}
