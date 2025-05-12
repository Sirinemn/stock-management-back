package fr.sirine.stock_management_back.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockMovementFilter {
    private Long userId;
    private Long productId;
    private Long groupId;
    private String startDate;
    private String endDate;

}

