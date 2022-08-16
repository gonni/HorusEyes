package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

/**
 * Created by a1000074 on 12/04/2021.
 */
@Repository
public interface SeedRepository extends JpaRepository<TopSeeds, Long> {
    TopSeeds findBySeedNo(long seedNo);
    List<TopSeeds> findByStatus(SeedStatus status);
}
