package com.userops.api.controller;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserInventoryGroup;
import com.userops.api.service.UserInventoryGroupService;
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
@Tag(name = "User Inventory Group", description = "API for managing user inventory group assignments")
public class UserInventoryGroupController {

    private final UserInventoryGroupService service;

    public UserInventoryGroupController(UserInventoryGroupService service) {
        this.service = service;
    }

    @GetMapping("/users/{personnelId}/companies/{companyId}/inventory-groups/{inventoryGroup}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get User Inventory Group", description = "Retrieves the inventory group assignment for a given user, company, and inventory group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user inventory group",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInventoryGroup.class))),
        @ApiResponse(responseCode = "404", description = "User inventory group not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<UserInventoryGroup> getUserInventoryGroup(
        @Parameter(description = "Personnel ID", required = true) @PathVariable("personnelId") final Long personnelId,
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Inventory Group", required = true) @PathVariable("inventoryGroup") final String inventoryGroup) {
        
        final UserInventoryGroup userInventoryGroup = this.service.getUserInventoryGroup(companyId, personnelId, inventoryGroup);
        if (userInventoryGroup != null) {
            return ResponseEntity.ok(userInventoryGroup);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/users/{personnelId}/companies/{companyId}/inventory-groups/{inventoryGroup}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update User Inventory Group Admin Role", description = "Updates the admin role for a user inventory group assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated user inventory group admin role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInventoryGroup.class))),
        @ApiResponse(responseCode = "404", description = "User inventory group not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<UserInventoryGroup> updateUserInventoryGroupAdminRole(
        @Parameter(description = "Personnel ID", required = true) @PathVariable("personnelId") final Long personnelId,
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Inventory Group", required = true) @PathVariable("inventoryGroup") final String inventoryGroup,
        @Parameter(description = "Admin role to update", required = true) @RequestBody final AdminRole adminRole) {
        
        final UserInventoryGroup userInventoryGroup = this.service.updateUserInventoryGroupAdminRole(companyId, personnelId, inventoryGroup, adminRole);
        if (userInventoryGroup != null) {
            return ResponseEntity.ok(userInventoryGroup);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
