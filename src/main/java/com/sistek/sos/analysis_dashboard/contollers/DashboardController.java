package com.sistek.sos.analysis_dashboard.contollers;

import com.sistek.sos.analysis_dashboard.dto.BarcodePageResponse;
import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import com.sistek.sos.analysis_dashboard.services.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboardHome(Model model) {
        model.addAttribute("plcCount", dashboardService.getPlcCount());
        model.addAttribute("lineCount", dashboardService.getLineCount());
        model.addAttribute("barcodeCount", dashboardService.getBarcodeCount());
        return "dashboard/home";
    }

    @GetMapping("/plcs")
    public String plcs(Model model) {
        model.addAttribute("plcs", dashboardService.getPlcsWithStatus());
        return "dashboard/plcs";
    }

    @GetMapping("/lines")
    public String lines(Model model) {
        model.addAttribute("lines", dashboardService.getLinesWithBarcodes());
        return "dashboard/lines";
    }

    @GetMapping("/lines/{lineId}")
    public String lineDetail(@PathVariable String lineId, Model model) {
        return dashboardService.getLineById(lineId)
                .map(line -> {
                    model.addAttribute("line", line);
                    int pageSize = DashboardService.BARCODE_PAGE_SIZE;
                    model.addAttribute("barcodePageSize", pageSize);
                    long totalBarcodes = line.getBarcodeCount();
                    int totalPages = totalBarcodes == 0
                            ? 0
                            : (int) Math.ceil((double) totalBarcodes / pageSize);
                    model.addAttribute("totalPages", totalPages);
                    model.addAttribute("currentPage", 0);
                    return "dashboard/line-detail";
                })
                .orElse("redirect:/dashboard/lines");
    }

    @GetMapping("/lines/{lineId}/barcodes")
    @ResponseBody
    public ResponseEntity<BarcodePageResponse> lineBarcodes(
            @PathVariable String lineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return dashboardService.getLineBarcodesPage(lineId, page, size)
                .map(result -> ResponseEntity.ok(BarcodePageResponse.from(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcodes")
    public String barcodes(Model model) {
        int pageSize = DashboardService.BARCODE_PAGE_SIZE;
        long totalBarcodes = dashboardService.getBarcodeCount();
        int totalPages = totalBarcodes == 0
                ? 0
                : (int) Math.ceil((double) totalBarcodes / pageSize);
        Page<BarcodeData> firstPage = dashboardService.getAllBarcodesPage(0, pageSize);

        model.addAttribute("barcodes", firstPage.getContent());
        model.addAttribute("barcodePageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalBarcodes", totalBarcodes);
        return "dashboard/barcodes";
    }

    @GetMapping("/barcodes/data")
    @ResponseBody
    public BarcodePageResponse allBarcodesData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return BarcodePageResponse.from(dashboardService.getAllBarcodesPage(page, size));
    }
}
