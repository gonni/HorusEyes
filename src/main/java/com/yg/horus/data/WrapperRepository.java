package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by a1000074 on 20/04/2021.
 */
public interface WrapperRepository extends JpaRepository<WrapperRule, Long> {
    List<WrapperRule> findBySeedNo(long seedNo) ;
}
