package com.yg.horus.data;

import com.yg.horus.crawl.WrappingRule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by a1000074 on 20/04/2021.
 */
public interface WrapperRepository extends JpaRepository<WrapperRule, Long> {
}
