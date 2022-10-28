package com.bist.api.service.impl;

import com.bist.api.model.UserFavShares;
import com.bist.api.repository.UserFavRepository;
import com.bist.api.rest.dto.UserFavDTO;
import com.bist.api.service.UserFavService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavServiceImpl implements UserFavService {

    private final UserFavRepository userFavRepository;

    @Override
    @Transactional
    public void addFav(String shareName, String name) {
        log.info("Adding fav for user {} and share {}", name, shareName);
        userFavRepository.findById(name)
                .map(userFavShares -> {
                    var isInList = userFavShares.getShares().stream()
                            .anyMatch(userFavDTO -> userFavDTO.getName().equals(shareName));
                    if (!isInList) {
                        userFavShares.getShares().add(UserFavDTO.builder().name(shareName).build());
                    }
                    else {
                        userFavShares.getShares().removeIf(userFavDTO -> userFavDTO.getName().equals(shareName));
                    }
                    return userFavRepository.save(userFavShares);
                })
                .orElseGet(() -> userFavRepository.save(UserFavShares.builder()
                        .username(name)
                        .shares(Collections.singletonList(UserFavDTO.builder().name(shareName).build()))
                        .build()));
    }

    @Override
    public List<UserFavDTO> getAllFavOnUser(String name) {
        log.info("Getting all fav for user {}", name);
        return userFavRepository.findById(name)
                .map(UserFavShares::getShares)
                .orElse(Collections.emptyList());
    }
}

