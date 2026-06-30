package com.sistek.sos.analysis_dashboard.contollers;

import com.sistek.sos.analysis_dashboard.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final DashboardService dashboardService;

    public IndexController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("plcCount", dashboardService.getPlcCount());
        model.addAttribute("lineCount", dashboardService.getLineCount());
        model.addAttribute("barcodeCount", dashboardService.getBarcodeCount());
        return "index";
    }
}
