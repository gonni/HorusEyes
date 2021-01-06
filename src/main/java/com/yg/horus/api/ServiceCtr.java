package com.yg.horus.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by a1000074 on 30/12/2020.
 */
@RestController
@Slf4j
public class ServiceCtr {

    public ServiceCtr() {
        log.info("Init Common Service Controller ..");
    }

    @RequestMapping("/yg/horus/hell")
    public @ResponseBody String hell() {
        log.info("Detencted Hell-Ha ..");

        return "{\"dt\": "+System.currentTimeMillis()+"}" ;
    }
}
