package com.yg.horus.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Created by a1000074 on 15/03/2021.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="CRAWL_UNIT1")
public class CrawlUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long crawlNo;
    private String url ;
    private String anchorText ;
    private String anchorImg ;
    @Setter
    @Enumerated(EnumType.STRING)
    private CrawlStatus status ;
//    private long seedNo ;
    @CreationTimestamp
    private LocalDateTime regDate ;
    @CreationTimestamp
    private LocalDateTime updDate ;
    @Setter
    private String pageText ;
    @Setter
    private String pageDate ;
    private String pageTitle ;

    @Setter
    @ManyToOne(targetEntity = TopSeeds.class, fetch = FetchType.LAZY)
    @JoinColumn(name="SEED_NO")
    private TopSeeds topSeeds ;

    @Builder
    public CrawlUnit(String url, String anchorText, CrawlStatus status) {
        this.url = url ;
        this.anchorText = anchorText ;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s", crawlNo, url, anchorText, status, regDate, pageText);
    }
}
