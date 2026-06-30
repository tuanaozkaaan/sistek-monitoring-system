package com.sistek.sos.analysis_dashboard.services;

import com.sistek.sos.analysis_dashboard.entities.LineInfo;
import com.sistek.sos.analysis_dashboard.repositories.LineInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LineInfoService {
    private final LineInfoRepository lineInfoRepository;

    @Autowired
    public LineInfoService(LineInfoRepository lineInfoRepository) {
        this.lineInfoRepository = lineInfoRepository;
    }

    public List<LineInfo> getAllLineInfo() {
        return lineInfoRepository.findAll();
    }

    public Optional<LineInfo> getLineInfoById(String lineId) {
        return lineInfoRepository.findById(lineId);
    }
}
