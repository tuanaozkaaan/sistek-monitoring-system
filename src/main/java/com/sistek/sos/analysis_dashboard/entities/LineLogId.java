package com.sistek.sos.analysis_dashboard.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class LineLogId implements Serializable {
    private String lineId;
    private Long seqNo;
}
