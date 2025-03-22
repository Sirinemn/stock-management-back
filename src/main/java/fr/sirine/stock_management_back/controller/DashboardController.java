package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.DashboardOverviewDto;
import fr.sirine.stock_management_back.service.impl.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDto> getDashboardOverview(@RequestParam Integer groupId) {
        return ResponseEntity.ok(dashboardService.getDashboardOverview(groupId));
    }
}
