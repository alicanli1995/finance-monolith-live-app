package com.bist.api.service;

import com.bist.api.rest.dto.UserFavDTO;

import java.util.List;

public interface UserFavService {
    void addFav(String shareName, String name);

    List<UserFavDTO> getAllFavOnUser(String name);

}
