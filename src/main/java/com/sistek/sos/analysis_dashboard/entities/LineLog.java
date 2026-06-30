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
@IdClass(LineLogId.class)
@Table(name = "line_log")
public class LineLog {
    @Id
    @Column(name = "line_id", nullable = false)
    private String lineId;

    @Id
    @Column(name = "seq_no", nullable = false)
    private Long seqNo;

    @Column(name = "proc_date", nullable = false)
    private LocalDateTime procDate;

    @Column(name = "status", nullable = false)
    private String status;
}
