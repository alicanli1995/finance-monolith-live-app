package com.bist.api.rest;

import com.bist.api.rest.dto.AddCommentRequest;
import com.bist.api.rest.dto.CommentResponseDTO;
import com.bist.api.rest.dto.HistoryResponseModel;
import com.bist.api.rest.dto.ShareGenericModel;
import com.bist.api.service.BistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.bist.api.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@Slf4j
@RequestScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bists")
public class BistController {

    private final BistService bistService;

    @GetMapping
    public List<ShareGenericModel> getBists() {
        return bistService.getBists();
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public void saveBists(@RequestBody ShareGenericModel financeApiDTO) {
        bistService.editBist(financeApiDTO);
    }

    @GetMapping(path = "/{name}/history/{hours}",produces = "application/json")
    public HistoryResponseModel getBistHistory(@PathVariable String name, @PathVariable int hours) {
        return bistService.getBistHistory(name, hours);
    }

    @GetMapping(path = "/{name}/comments",produces = "application/json")
    public List<CommentResponseDTO> getComments(@PathVariable String name) {
        return bistService.getComments(name);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{name}/comments", produces = "application/json")
    public CommentResponseDTO addCommentOnBist(@PathVariable String name, @Valid @RequestBody AddCommentRequest addCommentRequest, Principal principal) {
       return bistService.addCommentBist(name, addCommentRequest, principal.getName());
    }
}
