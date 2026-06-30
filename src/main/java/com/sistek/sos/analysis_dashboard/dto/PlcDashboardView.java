package com.sistek.sos.analysis_dashboard.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlcDashboardView {
    private String plcId;
    private String plcIp;
    private String status;
    private LocalDateTime statusDate;
}
