package com.bist.api.rest;

import com.bist.api.model.UserExtra;
import com.bist.api.rest.dto.UserExtraRequest;
import com.bist.api.rest.dto.UserShareCreateRequest;
import com.bist.api.rest.dto.UserShareResponseDTO;
import com.bist.api.service.UserExtraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.bist.api.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userextras")
public class UserController {

    private final UserExtraService userExtraService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping(path = "/me",produces = "application/json")
    public UserExtra getUserExtra(Principal principal) {
        return userExtraService.validateAndGetUserExtra(principal.getName());
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping(path = "/me",produces = "application/json")
    public UserExtra saveUserExtra(@Valid @RequestBody UserExtraRequest updateUserExtraRequest, Principal principal) {
        Optional<UserExtra> userExtraOptional = userExtraService.getUserExtra(principal.getName());
        UserExtra userExtra = userExtraOptional.orElseGet(() -> new UserExtra(principal.getName()));
        userExtra.setAvatar(updateUserExtraRequest.getAvatar());
        return userExtraService.saveUserExtra(userExtra);
    }
    @PostMapping(path = "/me/share", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void userAddShare(@RequestBody UserShareCreateRequest request, Principal principal) {
        userExtraService.addShare(request, principal.getName());
    }

    @GetMapping(path = "/me/shares",produces = "application/json")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public List<UserShareResponseDTO> getUserShares(Principal principal) {
        return userExtraService.getUserShares(principal.getName());
    }

    @PostMapping(path = "/edit",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void editUserShare(@RequestBody UserShareCreateRequest request, Principal principal) {
        userExtraService.editShare(request, principal.getName());
    }

    @PostMapping(path = "/deleteShareOnUser/{shareName}",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void deleteShareOnUser(@PathVariable String  shareName, Principal principal) {
        userExtraService.deleteShareOnUser(shareName, principal.getName());
    }

    @GetMapping(path = "/me/balance",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public BigDecimal getBalance(Principal principal) {
        return userExtraService.getBalance(principal.getName());
    }
}
