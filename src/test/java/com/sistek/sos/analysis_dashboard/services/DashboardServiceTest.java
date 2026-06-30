package com.sistek.sos.analysis_dashboard.services;

import com.sistek.sos.analysis_dashboard.dto.LineDashboardView;
import com.sistek.sos.analysis_dashboard.dto.PlcDashboardView;
import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import com.sistek.sos.analysis_dashboard.entities.LineInfo;
import com.sistek.sos.analysis_dashboard.entities.LineLog;
import com.sistek.sos.analysis_dashboard.entities.PlcInfo;
import com.sistek.sos.analysis_dashboard.entities.PlcLog;
import com.sistek.sos.analysis_dashboard.repositories.BarcodeDataRepository;
import com.sistek.sos.analysis_dashboard.repositories.LineLogRepository;
import com.sistek.sos.analysis_dashboard.repositories.PlcLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    private static final LocalDateTime LOG_DATE = LocalDateTime.of(2025, 7, 31, 12, 0);
    private static final LocalDateTime INFO_DATE = LocalDateTime.of(2025, 7, 30, 8, 0);

    @Mock
    private LineInfoService lineInfoService;

    @Mock
    private PlcInfoService plcInfoService;

    @Mock
    private BarcodeDataRepository barcodeDataRepository;

    @Mock
    private LineLogRepository lineLogRepository;

    @Mock
    private PlcLogRepository plcLogRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getLinesWithBarcodes_usesLatestLogStatusAndLoadsBarcodes() {
        LineInfo line = line("L1", "Line-1", "STOP", INFO_DATE);
        LineLog log = lineLog("L1", 2L, "RUN", LOG_DATE);
        BarcodeData barcode = barcode("BC-001", "L1");

        when(lineInfoService.getAllLineInfo()).thenReturn(List.of(line));
        when(lineLogRepository.findTopByLineIdOrderBySeqNoDesc("L1")).thenReturn(Optional.of(log));
        when(barcodeDataRepository.findByLineIdOrderByCreDateDesc("L1")).thenReturn(List.of(barcode));

        List<LineDashboardView> lines = dashboardService.getLinesWithBarcodes();

        assertThat(lines).hasSize(1);
        assertThat(lines.get(0).getLineId()).isEqualTo("L1");
        assertThat(lines.get(0).getLineName()).isEqualTo("Line-1");
        assertThat(lines.get(0).getStatus()).isEqualTo("RUN");
        assertThat(lines.get(0).getStatusDate()).isEqualTo(LOG_DATE);
        assertThat(lines.get(0).getBarcodes()).containsExactly(barcode);
    }

    @Test
    void getLinesWithBarcodes_fallsBackToLineInfoWhenLogMissing() {
        LineInfo line = line("L2", "Line-2", "PASSIVE", INFO_DATE);

        when(lineInfoService.getAllLineInfo()).thenReturn(List.of(line));
        when(lineLogRepository.findTopByLineIdOrderBySeqNoDesc("L2")).thenReturn(Optional.empty());
        when(barcodeDataRepository.findByLineIdOrderByCreDateDesc("L2")).thenReturn(List.of());

        LineDashboardView view = dashboardService.getLinesWithBarcodes().get(0);

        assertThat(view.getStatus()).isEqualTo("PASSIVE");
        assertThat(view.getStatusDate()).isEqualTo(INFO_DATE);
        assertThat(view.getBarcodes()).isEmpty();
    }

    @Test
    void getPlcsWithStatus_usesLatestLogStatus() {
        PlcInfo plc = plc("PLC-1", "10.0.0.1", "PASSIVE", INFO_DATE);
        PlcLog log = plcLog("PLC-1", 5L, "ACTIVE", LOG_DATE);

        when(plcInfoService.getAllPlcInfo()).thenReturn(List.of(plc));
        when(plcLogRepository.findTopByPlcIdOrderBySeqNoDesc("PLC-1")).thenReturn(Optional.of(log));

        PlcDashboardView view = dashboardService.getPlcsWithStatus().get(0);

        assertThat(view.getPlcId()).isEqualTo("PLC-1");
        assertThat(view.getPlcIp()).isEqualTo("10.0.0.1");
        assertThat(view.getStatus()).isEqualTo("ACTIVE");
        assertThat(view.getStatusDate()).isEqualTo(LOG_DATE);
    }

    private LineInfo line(String id, String name, String status, LocalDateTime date) {
        LineInfo line = new LineInfo();
        line.setLineId(id);
        line.setLineName(name);
        line.setStatus(status);
        line.setLastStatusDate(date);
        return line;
    }

    private LineLog lineLog(String lineId, long seqNo, String status, LocalDateTime date) {
        LineLog log = new LineLog();
        log.setLineId(lineId);
        log.setSeqNo(seqNo);
        log.setStatus(status);
        log.setProcDate(date);
        return log;
    }

    private PlcInfo plc(String id, String ip, String status, LocalDateTime date) {
        PlcInfo plc = new PlcInfo();
        plc.setPlcId(id);
        plc.setPlcIp(ip);
        plc.setStatus(status);
        plc.setLastStatusDate(date);
        return plc;
    }

    private PlcLog plcLog(String plcId, long seqNo, String status, LocalDateTime date) {
        PlcLog log = new PlcLog();
        log.setPlcId(plcId);
        log.setSeqNo(seqNo);
        log.setStatus(status);
        log.setProcDate(date);
        return log;
    }

    private BarcodeData barcode(String barcode, String lineId) {
        BarcodeData data = new BarcodeData();
        data.setBarcode(barcode);
        data.setLineId(lineId);
        data.setStatus("SENT");
        data.setCreDate(LOG_DATE);
        return data;
    }
}
