package com.bist.api.service;

import com.bist.api.rest.dto.*;

import java.util.List;

public interface BistService {

    List<ShareGenericModel> getBists();

    void editBist(ShareGenericModel financeApiDTO);

    List<CommentResponseDTO> getComments(String name);

    HistoryResponseModel getBistHistory(String name, int hours);

    CommentResponseDTO addCommentBist(String name, AddCommentRequest comment, String username);

    void deleteComment(String name, String username, CommentDeleteRequestDTO comment) ;
}
