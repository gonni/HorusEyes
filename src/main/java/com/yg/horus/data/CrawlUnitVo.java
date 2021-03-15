package com.yg.horus.data;

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
    private String status ;
    @CreationTimestamp
    private LocalDateTime regDate ;

    @Builder
    public CrawlUnitVo(String url, String anchorText, String status) {
        this.url = url ;
        this.anchorText = anchorText ;
        this.status = status;
    }
}
