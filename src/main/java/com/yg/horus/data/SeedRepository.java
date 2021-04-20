package com.yg.horus.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 * Created by a1000074 on 12/04/2021.
 */
@Repository
public interface SeedRepository extends JpaRepository<TopSeeds, Iterator> {
    TopSeeds findBySeedNo(long seedNo);
}
