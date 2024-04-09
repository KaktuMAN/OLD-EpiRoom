package com.epiroom.api.controllers;

import com.epiroom.api.model.Permission;
import com.epiroom.api.model.User;
import com.epiroom.api.model.dto.user.ApiKey;
import com.epiroom.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/permissions")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Retrieve the permissions for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of permissions", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))),
            }),
            @ApiResponse(responseCode = "403", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<String[]> getPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByMail(authentication.getName());
        if (user == null)
            return ResponseEntity.status(403).build();
        List<Permission> permissions = user.getPermissions();
        return ResponseEntity.ok(permissions.stream().map(Permission::getPermission).toArray(String[]::new));
    }

    @GetMapping("/generate-key")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Generate a new API key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "API key generated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiKey.class))
        }),
        @ApiResponse(responseCode = "403", description = "Not authenticated", content = @Content),
    })
    public ResponseEntity<ApiKey> generateApiKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByMail(authentication.getName());
        String apiKey = UUID.randomUUID().toString();
        while (userRepository.findByApiKey(apiKey) != null) {
            apiKey = UUID.randomUUID().toString();
        }
        user.setApiKey(apiKey);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiKey(user));
    }
}
