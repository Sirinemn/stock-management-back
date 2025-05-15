package fr.sirine.stock_management_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StockChartSeriesDto {
    private String name; // "Entrées" ou "Sorties"
    private List<ChartPointDto> series;
}
