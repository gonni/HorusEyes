package com.yg.horus.data;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public interface NewsClickRepository extends JpaRepository<NewsClickRepository.NewsClick, Long> {

    @Query(value = "SELECT n.NEWS_ID as newsId, COUNT(n.NEWS_ID) as cntClick " +
            "FROM NEWS_CLICK n WHERE n.PAGE_CD = :pageCd " +
            "GROUP BY n.NEWS_ID"
            , nativeQuery = true)
    List<ClickCount> getClickCount(@Param("pageCd") String pageCd);

    @Query(value = "SELECT n.NEWS_ID as newsId, COUNT(n.NEWS_ID) as cntClick " +
            "FROM NEWS_CLICK n WHERE n.PAGE_CD = :pageCd AND n.NEWS_ID IN (:targetItems)" +
            "GROUP BY n.NEWS_ID"
            , nativeQuery = true)
    List<ClickCount> getClickCount(
            @Param("pageCd") String pageCd,
            @Param("targetItems") List<Long> targets);


    @Data
    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @Table(name="NEWS_CLICK")
    class NewsClick {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long clickId ;
//        @Column(name = "CLICK_ID")
        private String userId ;
//        @Column(name = "CLICK_ID")
        private long newsId ;
        private String pageCd ;
        @CreationTimestamp
        private LocalDateTime clickDt ;
    }

}



