package com.yg.horus.data;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by a1000074 on 20/04/2021.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="WRAPPER_RULE")
public class WrapperRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long wrapperNo ;
    @Column(name="SEED_NO")
    private long seedNo ;
    @Enumerated(EnumType.STRING)
    private WrapType wrapType ;
    private String wrapVal ;
    private String wrapName ;
    @CreationTimestamp
    @Column(name = "REG_DT")
    private LocalDateTime regDate ;

    @Builder
    WrapperRule(long seedNo, WrapType wrapType, String wrapVal, String wrapName) {
        this.seedNo = seedNo ;
        this.wrapType = wrapType ;
        this.wrapVal = wrapVal ;
        this.wrapName = wrapName ;
    }

    public WrapperRule(WrapType wrapType, String wrapVal, String wrapName) {

        this.wrapType = wrapType ;
        this.wrapVal = wrapVal ;
        this.wrapName = wrapName ;
    }

    public String toString() {
        return String.format("WrapperRule : %s | %s | %s | %s | %s | %s", wrapperNo, seedNo, wrapType, wrapVal, wrapName, regDate);
    }
}
