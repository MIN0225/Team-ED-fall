package com.hapjusil.service;

import com.hapjusil.domain.PrHasBooking;
import com.hapjusil.repository.PrHasBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrHasBookingService {

    @Autowired
    PrHasBookingRepository prHasBookingRepository;

    public List<PrHasBooking> getPrHasBookingList() {
        return prHasBookingRepository.findAll();
    }
}
