package com.yg.horus.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by a1000074 on 08/04/2021.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name="CRAWL_SEEDS")
public class TopSeeds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seedNo ;
    private String urlPattern ;
    private String title ;

    @Builder
    public TopSeeds(String urlPattern, String title) {
        this.urlPattern = urlPattern ;
        this.title = title ;
    }
}
