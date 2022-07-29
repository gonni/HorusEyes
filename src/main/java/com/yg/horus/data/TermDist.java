package com.yg.horus.data;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="TERM_DIST")
public class TermDist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERM_NO")
    private long termNo ;
    @Column(name = "BASE_TERM")
    private String baseTerm ;
    @Column(name = "COMP_TERM")
    private String compTerm ;
    @Column(name = "DIST_VAL")
    private double distVal ;
    @Column(name = "GRP_TS")
    private long grpTs ;

}
