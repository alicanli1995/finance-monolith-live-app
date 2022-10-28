package com.bist.api.service;

import com.bist.api.rest.dto.AddCommentRequest;
import com.bist.api.rest.dto.CommentResponseDTO;
import com.bist.api.rest.dto.HistoryResponseModel;
import com.bist.api.rest.dto.ShareGenericModel;

import java.util.List;

public interface BistService {

    List<ShareGenericModel> getBists();

    void editBist(ShareGenericModel financeApiDTO);

    List<CommentResponseDTO> getComments(String name);

    HistoryResponseModel getBistHistory(String name, int hours);

    CommentResponseDTO addCommentBist(String name, AddCommentRequest comment, String username);
}
