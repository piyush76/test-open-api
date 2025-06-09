package com.userops.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Customer Inventory Group representing inventory management configuration")
public class CustomerInventoryGroup {
    
    @Schema(description = "Company ID", example = "HostCompany")
    private String companyId;
    
    @Schema(description = "Inventory Group", example = "ELECTRONICS")
    private String inventoryGroup;
    
    @Schema(description = "Stocking Method", example = "FIFO")
    private String stockingMethod;
    
    @Schema(description = "Minimum Shelf Life", example = "30")
    private Integer minShelfLife;
    
    @Schema(description = "Minimum Shelf Life Method", example = "DAYS")
    private String minShelfLifeMethod;
    
    @Schema(description = "Source Hub", example = "HUB001")
    private String sourceHub;
    
    @Schema(description = "Dropship Flag", example = "N")
    private String dropship;
    
    @Schema(description = "Relax Shelf Life", example = "7")
    private Integer relaxShelfLife;
    
    @Schema(description = "Not Used Field 1")
    private String notused1;
    
    @Schema(description = "Short Shelf Life Days", example = "5")
    private Integer shortShelfLifeDays;
}
