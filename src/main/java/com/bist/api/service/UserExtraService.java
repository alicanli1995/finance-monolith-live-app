package com.bist.api.service;

import com.bist.api.model.UserExtra;
import com.bist.api.rest.dto.UserShareCreateRequest;
import com.bist.api.rest.dto.UserShareResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserExtraService {

    UserExtra validateAndGetUserExtra(String username);

    Optional<UserExtra> getUserExtra(String username);

    UserExtra saveUserExtra(UserExtra userExtra);

    void addShare(UserShareCreateRequest shareAddRequestDTO, String  username);

    BigDecimal getBalance(String name);

    List<UserShareResponseDTO> getUserShares(String name);

    void editShare(UserShareCreateRequest request, String name);

    void deleteShareOnUser(String name, String userName);
}
