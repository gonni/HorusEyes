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
@Setter
@Getter
@NoArgsConstructor
@Table(name="CRAWL_UNIT")
public class CrawlUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long crawlNo;
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
    @Setter
    private String pageDate ;

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
