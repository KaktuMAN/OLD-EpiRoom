package com.epiroom.api.controllers;

import com.epiroom.api.model.User;
import com.epiroom.api.model.dto.user.ApiKey;
import com.epiroom.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Api", description = "API")
@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRepository userRepository;

    public ApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/generate_key")
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
        String apiKey = RandomStringUtils.random(32, true, true);
        user.setApiKey(apiKey);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiKey(user));
    }
}
