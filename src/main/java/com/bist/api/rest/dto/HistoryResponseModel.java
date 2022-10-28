package com.bist.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class HistoryResponseModel {

    private String name;
    private String dailyChangePercentage;
    private String dailyVolume;
    private String poster;
    private String rank;
    private List<History> values;
    private List<CommentResponseDTO.CommentDto> comments;

}
