package com.userops.api.controller;

import com.userops.api.model.AdminRole;
import com.userops.api.model.UserOpsEntity;
import com.userops.api.service.UserOpsEntityService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/iam")
@Tag(name = "User Operations Entity", description = "API for managing user operations entity assignments")
public class UserOpsEntityController {

    private final UserOpsEntityService service;

    public UserOpsEntityController(UserOpsEntityService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}/companies/{companyId}/ops-entity")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get User Operations Entity", description = "Retrieves the operations entities for a given user and company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user operations entities",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOpsEntity.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserOpsEntity>> getUserOpsEntitybyUserAndCompany(
        @Parameter(description = "User ID", required = true) @PathVariable("userId") final Long userId,
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId) {
        
        final List<UserOpsEntity> userOpsEntities = this.service.getUserOpsEntity(userId, companyId);
        return ResponseEntity.ok(userOpsEntities);
    }

    @PutMapping("/users/{userId}/companies/{companyId}/ops-entity")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update User Operations Entity Admin Role", description = "Updates the admin role for a user operations entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated user operations entity admin role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOpsEntity.class))),
        @ApiResponse(responseCode = "404", description = "User operations entity not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<UserOpsEntity> updateUserOpsEntityAdminRole(
        @Parameter(description = "User ID", required = true) @PathVariable("userId") final Long userId,
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Admin role to update", required = true) @RequestBody final AdminRole adminRole) {
        
        final UserOpsEntity userOpsEntity = this.service.updateUserOpsEntityAdminRole(userId, companyId, adminRole);
        if (userOpsEntity != null) {
            return ResponseEntity.ok(userOpsEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
