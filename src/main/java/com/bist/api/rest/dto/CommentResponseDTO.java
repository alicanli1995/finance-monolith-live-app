package com.bist.api.rest.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentResponseDTO(String name,
                                    List<CommentDto> comments) {

    @Builder
    public record CommentDto(String username, String avatar, String text, LocalDateTime timestamp) {
    }
}