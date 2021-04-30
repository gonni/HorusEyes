package com.yg.horus.api;

//import com.yg.horus.data.MemberRepository;
import com.yg.horus.scheduler.JobProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JobProducer jobProducer = null ;

    @RequestMapping("/system/version")
    public String getVersion() {
        return "NA" ;
    }

    @RequestMapping("/crawl/unit")
    public String execJob(long seedNo, int maxJobCnt) {
        log.info("detected unit crawl for #{} limit {}", seedNo, maxJobCnt);
        int scheduled = this.jobProducer.crawlContents(seedNo, maxJobCnt) ;

        return String.format("Job linked with seedNo:%s scheduled : %d", seedNo, scheduled);
    }
//    @Autowired
//    private MemberRepository memberRepository = null;
//
//    public ServiceCtr() {
//        log.info("Init Common Service Controller ..");
//    }
//
//    @RequestMapping("/yg/horus/hell")
//    public @ResponseBody String hell() {
//        log.info("Detencted Hell-Ha ..");
//
//
//        this.memberRepository.findAll().forEach(System.out::println);
//
//
//        return "{\"dt\": "+System.currentTimeMillis()+"}" ;
//    }
}
