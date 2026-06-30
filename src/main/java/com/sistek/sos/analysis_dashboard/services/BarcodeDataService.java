package com.sistek.sos.analysis_dashboard.services;

import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import com.sistek.sos.analysis_dashboard.repositories.BarcodeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarcodeDataService {

    private final BarcodeDataRepository barcodeDataRepository;

    @Autowired
    public BarcodeDataService(BarcodeDataRepository barcodeDataRepository) {
        this.barcodeDataRepository = barcodeDataRepository;
    }

    public List<BarcodeData> getAllBarcodeData() {
        return barcodeDataRepository.findAll();
    }
}