package com.incora.api.iam.controller;

import com.incora.api.iam.model.UserOpsEntity;
import com.incora.api.iam.service.UserOpsEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-ops-entities")
@Tag(name = "User Operations Entity", description = "User Operations Entity management APIs")
public class UserOpsEntityController {

    private final UserOpsEntityService userOpsEntityService;

    @Autowired
    public UserOpsEntityController(UserOpsEntityService userOpsEntityService) {
        this.userOpsEntityService = userOpsEntityService;
    }

    @GetMapping
    @Operation(summary = "Get all user operations entities", description = "Retrieve a list of all user operations entities")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<UserOpsEntity>> getAllUserOpsEntities() {
        List<UserOpsEntity> entities = userOpsEntityService.getAllUserOpsEntities();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{personnelId}/{companyId}/{opsEntityId}")
    @Operation(summary = "Get user operations entity by composite key", description = "Retrieve a specific user operations entity by its composite primary key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user operations entity"),
            @ApiResponse(responseCode = "404", description = "User operations entity not found")
    })
    public ResponseEntity<UserOpsEntity> getUserOpsEntity(
            @Parameter(description = "Personnel ID") @PathVariable Long personnelId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Operations Entity ID") @PathVariable String opsEntityId) {
        UserOpsEntity entity = userOpsEntityService.getUserOpsEntity(personnelId, companyId, opsEntityId);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    @Operation(summary = "Create new user operations entity", description = "Create a new user operations entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User operations entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User operations entity already exists")
    })
    public ResponseEntity<UserOpsEntity> createUserOpsEntity(@Valid @RequestBody UserOpsEntity userOpsEntity) {
        UserOpsEntity createdEntity = userOpsEntityService.createUserOpsEntity(userOpsEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntity);
    }

    @PutMapping("/{personnelId}/{companyId}/{opsEntityId}")
    @Operation(summary = "Update user operations entity", description = "Update an existing user operations entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User operations entity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User operations entity not found")
    })
    public ResponseEntity<UserOpsEntity> updateUserOpsEntity(
            @Parameter(description = "Personnel ID") @PathVariable Long personnelId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Operations Entity ID") @PathVariable String opsEntityId,
            @Valid @RequestBody UserOpsEntity userOpsEntity) {
        UserOpsEntity updatedEntity = userOpsEntityService.updateUserOpsEntity(personnelId, companyId, opsEntityId, userOpsEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{personnelId}/{companyId}/{opsEntityId}")
    @Operation(summary = "Delete user operations entity", description = "Delete a user operations entity by its composite primary key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User operations entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User operations entity not found")
    })
    public ResponseEntity<Void> deleteUserOpsEntity(
            @Parameter(description = "Personnel ID") @PathVariable Long personnelId,
            @Parameter(description = "Company ID") @PathVariable String companyId,
            @Parameter(description = "Operations Entity ID") @PathVariable String opsEntityId) {
        userOpsEntityService.deleteUserOpsEntity(personnelId, companyId, opsEntityId);
        return ResponseEntity.noContent().build();
    }
}
