package com.bist.api.rest;

import com.bist.api.rest.dto.UserFinanceHistoryDTO;
import com.bist.api.service.UserFinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.security.Principal;

import static com.bist.api.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@Validated
@RequestScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finance")
public class UserFinanceController {

    private final UserFinanceService userFinanceService;

    @GetMapping(produces = "application/json")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<UserFinanceHistoryDTO> getUserFinance(Principal username) {
        return ResponseEntity.ok(userFinanceService.getUserFinance(username.getName()));
    }

}
