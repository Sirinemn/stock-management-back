package fr.sirine.stock_management_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChartPointDto {
    private String name; // ex: "2025-05-10"
    private int value;
}
