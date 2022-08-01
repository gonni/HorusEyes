package com.yg.horus.data;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.time.LocalDateTime;

public interface NewsClickRepository extends JpaRepository<NewsClickRepository.NewsClick, Long> {

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
        @CreationTimestamp
        private LocalDateTime clickDt ;
    }

}

