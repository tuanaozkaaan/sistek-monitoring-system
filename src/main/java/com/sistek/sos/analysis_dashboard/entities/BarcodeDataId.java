package com.sistek.sos.analysis_dashboard.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class BarcodeDataId implements Serializable {
    private String barcode;
    private String lineId;
}
