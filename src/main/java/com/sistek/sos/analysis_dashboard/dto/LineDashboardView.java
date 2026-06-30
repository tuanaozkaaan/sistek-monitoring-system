package com.sistek.sos.analysis_dashboard.dto;

import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class LineDashboardView {
    private String lineId;
    private String lineName;
    private String status;
    private LocalDateTime statusDate;
    private long barcodeCount;
    private List<BarcodeData> barcodes = new ArrayList<>();
}
