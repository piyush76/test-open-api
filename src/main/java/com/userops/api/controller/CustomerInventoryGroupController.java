package com.userops.api.controller;

import com.userops.api.model.CustomerInventoryGroup;
import com.userops.api.service.CustomerInventoryGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/iam")
@Tag(name = "Customer Inventory Group", description = "API for managing customer inventory group configurations")
public class CustomerInventoryGroupController {

    private final CustomerInventoryGroupService service;

    public CustomerInventoryGroupController(CustomerInventoryGroupService service) {
        this.service = service;
    }

    @GetMapping("/inventory-groups/{companyId}/{inventoryGroup}/{stockingMethod}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get Customer Inventory Group", description = "Retrieves the inventory group configuration for a given company, inventory group, and stocking method")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved customer inventory group",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerInventoryGroup.class))),
        @ApiResponse(responseCode = "404", description = "Customer inventory group not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<CustomerInventoryGroup> getCustomerInventoryGroup(
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Inventory Group", required = true) @PathVariable("inventoryGroup") final String inventoryGroup,
        @Parameter(description = "Stocking Method", required = true) @PathVariable("stockingMethod") final String stockingMethod) {
        
        final CustomerInventoryGroup customerInventoryGroup = this.service.getCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod);
        if (customerInventoryGroup != null) {
            return ResponseEntity.ok(customerInventoryGroup);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/inventory-groups/{companyId}/{inventoryGroup}/{stockingMethod}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update Customer Inventory Group", description = "Updates the inventory group configuration for a given company, inventory group, and stocking method")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated customer inventory group",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerInventoryGroup.class))),
        @ApiResponse(responseCode = "404", description = "Customer inventory group not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<CustomerInventoryGroup> updateCustomerInventoryGroup(
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Inventory Group", required = true) @PathVariable("inventoryGroup") final String inventoryGroup,
        @Parameter(description = "Stocking Method", required = true) @PathVariable("stockingMethod") final String stockingMethod,
        @Parameter(description = "Customer inventory group data to update", required = true) @RequestBody final CustomerInventoryGroup entity) {
        
        final CustomerInventoryGroup customerInventoryGroup = this.service.updateCustomerInventoryGroup(companyId, inventoryGroup, stockingMethod, entity);
        if (customerInventoryGroup != null) {
            return ResponseEntity.ok(customerInventoryGroup);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
