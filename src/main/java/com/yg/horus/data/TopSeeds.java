package com.yg.horus.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="SEED_NO")
    private List<WrapperRule> wrapperRules ;

    @Builder
    public TopSeeds(String urlPattern, String title) {
        this.urlPattern = urlPattern ;
        this.title = title ;
    }

    public void addWrapperRule(WrapperRule wrapperRule) {
        if(this.wrapperRules == null) {
            this.wrapperRules = new ArrayList<>();
        }
        this.wrapperRules.add(wrapperRule) ;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.seedNo).append("|").append(this.title).append("|").append(this.urlPattern);
        return sb.toString() ;
    }
}
