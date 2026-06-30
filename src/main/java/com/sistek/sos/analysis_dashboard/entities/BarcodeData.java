package com.sistek.sos.analysis_dashboard.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@IdClass(BarcodeDataId.class)
@Table(name = "barcode_data")
public class BarcodeData {
    @Id
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Id
    @Column(name = "line_id", nullable = false)
    private String lineId;

    @Column(name = "api_result")
    private String apiResult;

    @Column(name = "cre_date", nullable = false)
    private LocalDateTime creDate;

    @Column(name = "last_status_date")
    private LocalDateTime lastStatusDate;

    @Column(name = "status", nullable = false)
    private String status;
}
