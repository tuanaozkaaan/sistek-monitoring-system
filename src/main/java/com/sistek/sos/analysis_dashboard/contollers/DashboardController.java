package com.sistek.sos.analysis_dashboard.contollers;

import com.sistek.sos.analysis_dashboard.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
                    return "dashboard/line-detail";
                })
                .orElse("redirect:/dashboard/lines");
    }

    @GetMapping("/barcodes")
    public String barcodes(Model model) {
        model.addAttribute("barcodes", dashboardService.getAllBarcodes());
        return "dashboard/barcodes";
    }
}
