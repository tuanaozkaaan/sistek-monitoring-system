package com.sistek.sos.analysis_dashboard.services;

import com.sistek.sos.analysis_dashboard.entities.PlcInfo;
import com.sistek.sos.analysis_dashboard.repositories.PlcInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlcInfoService {
    private final PlcInfoRepository plcInfoRepository;

    @Autowired
    public PlcInfoService(PlcInfoRepository plcInfoRepository) {
        this.plcInfoRepository = plcInfoRepository;
    }

    public List<PlcInfo> getAllPlcInfo() {
        return plcInfoRepository.findAll();
    }

    public PlcInfo getPlcInfoById(String plcId) {
        return plcInfoRepository.findById(plcId).orElse(null);
    }
}

