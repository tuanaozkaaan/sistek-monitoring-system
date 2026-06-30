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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    public static final int BARCODE_PAGE_SIZE = 20;

    private final LineInfoService lineInfoService;
    private final PlcInfoService plcInfoService;
    private final BarcodeDataRepository barcodeDataRepository;
    private final LineLogRepository lineLogRepository;
    private final PlcLogRepository plcLogRepository;

    @Autowired
    public DashboardService(
            LineInfoService lineInfoService,
            PlcInfoService plcInfoService,
            BarcodeDataRepository barcodeDataRepository,
            LineLogRepository lineLogRepository,
            PlcLogRepository plcLogRepository) {
        this.lineInfoService = lineInfoService;
        this.plcInfoService = plcInfoService;
        this.barcodeDataRepository = barcodeDataRepository;
        this.lineLogRepository = lineLogRepository;
        this.plcLogRepository = plcLogRepository;
    }

    public List<LineDashboardView> getLinesWithBarcodes() {
        return lineInfoService.getAllLineInfo().stream()
                .map(this::buildLineView)
                .toList();
    }

    public List<PlcDashboardView> getPlcsWithStatus() {
        return plcInfoService.getAllPlcInfo().stream()
                .map(this::buildPlcView)
                .toList();
    }

    public long getPlcCount() {
        return plcInfoService.getAllPlcInfo().size();
    }

    public long getLineCount() {
        return lineInfoService.getAllLineInfo().size();
    }

    public long getBarcodeCount() {
        return barcodeDataRepository.count();
    }

    public Optional<LineDashboardView> getLineById(String lineId) {
        return lineInfoService.getLineInfoById(lineId).map(line -> {
            LineDashboardView view = buildLineView(line);
            Page<BarcodeData> firstPage = barcodeDataRepository.findByLineIdOrderByCreDateDesc(
                    lineId, PageRequest.of(0, BARCODE_PAGE_SIZE));
            view.setBarcodes(firstPage.getContent());
            return view;
        });
    }

    public Optional<Page<BarcodeData>> getLineBarcodesPage(String lineId, int page, int size) {
        if (lineInfoService.getLineInfoById(lineId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(fetchBarcodePage(page, size, lineId));
    }

    public Page<BarcodeData> getAllBarcodesPage(int page, int size) {
        return fetchBarcodePage(page, size, null);
    }

    private Page<BarcodeData> fetchBarcodePage(int page, int size, String lineId) {
        int safeSize = Math.max(1, Math.min(size, 100));
        int safePage = Math.max(0, page);
        Pageable pageable = PageRequest.of(safePage, safeSize);
        if (lineId == null) {
            return barcodeDataRepository.findAllByOrderByCreDateDesc(pageable);
        }
        return barcodeDataRepository.findByLineIdOrderByCreDateDesc(lineId, pageable);
    }

    private LineDashboardView buildLineView(LineInfo line) {
        LineDashboardView view = new LineDashboardView();
        view.setLineId(line.getLineId());
        view.setLineName(line.getLineName());
        view.setBarcodeCount(barcodeDataRepository.countByLineId(line.getLineId()));

        lineLogRepository.findTopByLineIdOrderBySeqNoDesc(line.getLineId())
                .ifPresentOrElse(
                        log -> applyLineLogStatus(view, log),
                        () -> applyLineInfoStatus(view, line)
                );

        return view;
    }

    private PlcDashboardView buildPlcView(PlcInfo plc) {
        PlcDashboardView view = new PlcDashboardView();
        view.setPlcId(plc.getPlcId());
        view.setPlcIp(plc.getPlcIp());

        plcLogRepository.findTopByPlcIdOrderBySeqNoDesc(plc.getPlcId())
                .ifPresentOrElse(
                        log -> applyPlcLogStatus(view, log),
                        () -> applyPlcInfoStatus(view, plc)
                );

        return view;
    }

    private void applyLineLogStatus(LineDashboardView view, LineLog log) {
        view.setStatus(log.getStatus());
        view.setStatusDate(log.getProcDate());
    }

    private void applyLineInfoStatus(LineDashboardView view, LineInfo line) {
        view.setStatus(line.getStatus());
        view.setStatusDate(line.getLastStatusDate());
    }

    private void applyPlcLogStatus(PlcDashboardView view, PlcLog log) {
        view.setStatus(log.getStatus());
        view.setStatusDate(log.getProcDate());
    }

    private void applyPlcInfoStatus(PlcDashboardView view, PlcInfo plc) {
        view.setStatus(plc.getStatus());
        view.setStatusDate(plc.getLastStatusDate());
    }
}
