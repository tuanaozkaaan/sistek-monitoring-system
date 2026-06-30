package com.sistek.sos.analysis_dashboard.contollers;



import com.sistek.sos.analysis_dashboard.entities.PlcInfo;

import com.sistek.sos.analysis_dashboard.services.PlcInfoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;



import java.util.List;



@Controller

public class ExampleController {

    @Autowired

    private PlcInfoService plcInfoService;



    @GetMapping("/example")

    public String examplePage(Model model) {

        // Fetch the all exist plc information

        List<PlcInfo> plcInfoList = plcInfoService.getAllPlcInfo();



        model.addAttribute("message", "We have " + plcInfoList.size() + " PLCs in the system.");

        return "example";

    }

}





