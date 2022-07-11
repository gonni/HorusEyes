package com.yg.horus.dto;

import lombok.Data;

@Data
public class Consumer {
    long cntWorkers ;
    long cntAlives ;
    long cntPendings ;

    public Consumer(long _cntAlives, long _cntWorkers, long _pending) {
        cntWorkers = _cntWorkers ;
        cntAlives = _cntAlives ;
        cntPendings = _pending ;
    }

}