package com.hapjusil.controller;

import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.service.PrHasBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrHasBookingController {
    @Autowired
    PrHasBookingService prHasBookingService;


    @GetMapping("/pr-has-booking") // 테스트
    public List<PrHasBooking> getPrHasBookingList() {
        return prHasBookingService.getPrHasBookingList();
    }

}