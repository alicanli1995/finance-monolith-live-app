package com.bist.api.rest;

import com.bist.api.rest.dto.UserFavDTO;
import com.bist.api.service.UserFavService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

import static com.bist.api.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@Validated
@RequestScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fav")
public class UserFavController {

    private final UserFavService userFavService;

    @PostMapping(path = "/{shareName}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void addFav(@PathVariable @NotBlank @Validated String shareName, Principal principal) {
        userFavService.addFav(shareName,principal.getName());
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public List<UserFavDTO> getAllFavOnUser(Principal principal) {
        return userFavService.getAllFavOnUser(principal.getName());
    }


}
