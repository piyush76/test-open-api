package com.userops.api.controller;

import com.userops.api.model.*;
import com.userops.api.service.*;
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
@RequestMapping("/chemicals/api/iam")
@Tag(name = "IAM API", description = "Identity and Access Management API")
public class IamController {

    private final UserService userService;
    private final UserOpsEntityService userOpsEntityService;
    private final UserPageService userPageService;

    public IamController(UserService userService, 
                        UserOpsEntityService userOpsEntityService,
                        UserPageService userPageService) {
        this.userService = userService;
        this.userOpsEntityService = userOpsEntityService;
        this.userPageService = userPageService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get User Work Context", 
              description = "Get work context for authorized user. Incora users will have Hub/Inventory Group context while customer users will have Facility/WorkArea context.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<User> getUser() {
        User user = userService.getCurrentUserContext();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get Authorized Users for Roles", 
              description = "Get users by role. If more than one role is provided then the users will be the union of all the roles (role1 OR role2)")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<UserGroupBy>> getUsers(
        @Parameter(description = "The role to find users by work context", required = true) 
        @RequestParam String role) {
        List<UserGroupBy> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}/companies/{companyId}/pages")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get User Pages by User and Company", 
              description = "Retrieve pages assigned to a user for a specific company")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<UserPage>> getUserPages(
        @Parameter(description = "User ID", required = true) @PathVariable String userId,
        @Parameter(description = "Company ID", required = true) @PathVariable String companyId) {
        List<UserPage> pages = userPageService.getUserPages(userId, companyId);
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/users/{userId}/companies/{companyId}/ops-entity")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Your GET endpoint", 
              description = "GET Operation for USER OPS Entity")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<UserOpsEntity> getUserOpsEntity(
        @Parameter(description = "User ID", required = true) @PathVariable String userId,
        @Parameter(description = "Company ID", required = true) @PathVariable String companyId) {
        UserOpsEntity entity = userOpsEntityService.getUserOpsEntity(userId, companyId);
        if (entity != null) {
            return ResponseEntity.ok(entity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{userId}/companies/{companyId}/ops-entity/{opsEntityId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Your PUT endpoint", 
              description = "CREATE or UPDATE Admin Role Operation for USER OPS Entity. Create will insert a new record only if it doesn't exist.\r\nUpdate will just update the admin role to \"Admin\" \"Grant Admin\" or null ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserOpsEntity> updateUserOpsEntityAdminRole(
        @Parameter(description = "User ID", required = true) @PathVariable("userId") final Long userId,
        @Parameter(description = "Company ID", required = true) @PathVariable("companyId") final String companyId,
        @Parameter(description = "Operations Entity ID", required = true) @PathVariable("opsEntityId") final String opsEntityId,
        @RequestBody(required = false) final UserOpsEntity opsEntity) {
        try {
            if (opsEntity == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            List<UserOpsEntity> existingEntities = userOpsEntityService.getAllUserOpsEntities(userId, companyId);

            if (existingEntities.isEmpty()) {
                opsEntity.setPersonnelId(userId);
                opsEntity.setCompanyId(companyId);
                opsEntity.setOpsEntityId(opsEntityId);
                UserOpsEntity createdEntity = userOpsEntityService.createOrUpdateUserOpsEntity(userId.toString(), companyId, opsEntityId, opsEntity);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEntity);
            } else {
                UserOpsEntity existingEntity = existingEntities.get(0);

                if (opsEntity.getAdminRole() == null ||
                    (opsEntity.getAdminRole().getValue() != null && opsEntity.getAdminRole().getValue().trim().isEmpty())) {
                    existingEntity.setAdminRole(null);
                } else if ("Admin".equalsIgnoreCase(opsEntity.getAdminRole().getValue()) ||
                    "Grant Admin".equalsIgnoreCase(opsEntity.getAdminRole().getValue())) {
                    existingEntity.setAdminRole(opsEntity.getAdminRole());
                }

                UserOpsEntity updatedEntity = userOpsEntityService.createOrUpdateUserOpsEntity(userId.toString(), companyId, opsEntityId, existingEntity);
                return ResponseEntity.ok(updatedEntity);
            }
        } catch (com.userops.api.exception.EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/users/{userId}/companies/{companyId}/pages/{pageId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Your PUT endpoint", 
              description = "Create or update a user page assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "201", description = "Created")
    })
    public ResponseEntity<UserPage> createOrUpdateUserPage(
        @Parameter(description = "User ID", required = true) @PathVariable String userId,
        @Parameter(description = "Company ID", required = true) @PathVariable String companyId,
        @Parameter(description = "Page ID", required = true) @PathVariable String pageId,
        @RequestBody UserPage userPage) {
        
        List<UserPage> existingPages = userPageService.getUserPages(userId, companyId);
        boolean exists = existingPages.stream().anyMatch(p -> p.getPageId().equals(pageId));
        
        UserPage savedPage = userPageService.createOrUpdateUserPage(userId, companyId, pageId, userPage);
        
        if (!exists) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPage);
        } else {
            return ResponseEntity.ok(savedPage);
        }
    }

    @DeleteMapping("/users/{userId}/companies/{companyId}/pages/{pageId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Your DELETE endpoint", 
              description = "Delete a user page assignment")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> deleteUserPage(
        @Parameter(description = "User ID", required = true) @PathVariable String userId,
        @Parameter(description = "Company ID", required = true) @PathVariable String companyId,
        @Parameter(description = "Page ID", required = true) @PathVariable String pageId) {
        
        userPageService.deleteUserPage(userId, companyId, pageId);
        return ResponseEntity.noContent().build();
    }
}
