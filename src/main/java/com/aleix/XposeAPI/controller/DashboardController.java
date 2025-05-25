package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.service.AssetService;
import com.aleix.XposeAPI.service.DashboardService;
import com.aleix.XposeAPI.service.FileUploadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Retrieves statistical data for the dashboard.
     *
     * @return a list of integers representing various dashboard statistics
     */
    @GetMapping
    public List<Integer> getDashboardStats() {
        return dashboardService.getDashboardStats();
    }

}
