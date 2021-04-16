package com.yg.horus.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by a1000074 on 08/04/2021.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="CRAWL_SEEDS")
public class TopSeeds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEED_NO")
    private long seedNo ;
    @Column(name = "URL_PATTERN")
    private String urlPattern ;
    @Column(name = "TITLE")
    private String title ;

    @Builder
    public TopSeeds(String urlPattern, String title) {
        this.urlPattern = urlPattern ;
        this.title = title ;
    }

    public String toString() {
        return null ;
    }
}
