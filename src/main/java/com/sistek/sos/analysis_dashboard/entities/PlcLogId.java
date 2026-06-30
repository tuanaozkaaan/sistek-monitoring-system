package com.sistek.sos.analysis_dashboard.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlcLogId implements Serializable {
    private String plcId;
    private Long seqNo;
}
