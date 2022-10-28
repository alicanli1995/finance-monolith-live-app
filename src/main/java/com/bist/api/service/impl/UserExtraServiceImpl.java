package com.bist.api.service.impl;

import com.bist.api.exception.UserExtraNotFoundException;
import com.bist.api.model.UserExtra;
import com.bist.api.model.UserShares;
import com.bist.api.repository.BistsRepository;
import com.bist.api.repository.UserExtraRepository;
import com.bist.api.repository.UserShareRepository;
import com.bist.api.rest.dto.UserShareCreateRequest;
import com.bist.api.rest.dto.UserShareResponseDTO;
import com.bist.api.rest.dto.UserSharesDTO;
import com.bist.api.service.UserExtraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserExtraServiceImpl implements UserExtraService {

    private final BistsRepository bistsRepository;
    private final UserExtraRepository userExtraRepository;
    private final UserShareRepository userShareRepository;

    @Override
    public UserExtra validateAndGetUserExtra(String username) {
        return getUserExtra(username).orElseThrow(() -> new UserExtraNotFoundException(username));
    }

    @Override
    public Optional<UserExtra> getUserExtra(String username) {
        return userExtraRepository.findById(username);
    }

    @Override
    public UserExtra saveUserExtra(UserExtra userExtra) {
        return userExtraRepository.save(userExtra);
    }

    @Override
    public void addShare(UserShareCreateRequest shareAddRequestDTO, String username) {
        var bistInfo = bistsRepository.findById(shareAddRequestDTO.name())
                .orElseThrow(() -> new RuntimeException("Share not found"));
        var userShare = userShareRepository.findById(username);

        if(userShare.isEmpty()) {
            var userShareNew = UserShares.builder()
                    .username(username)
                    .shares(List.of(UserSharesDTO.builder()
                                            .name(shareAddRequestDTO.name())
                                            .amount(shareAddRequestDTO.amount())
                                            .totalAmount(calcDailyTotalAmount(bistInfo.getValue(), shareAddRequestDTO.amount()))
                                            .build()))
                    .build();
            userShareNew.setTotalValue(userShareNew.getShares().stream()
                    .map(UserSharesDTO::getTotalAmount)
                    .map(BigDecimal::new)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            userShareRepository.save(userShareNew);
        }
        else {
            var userShareOld = userShare.get();
            var userShareOldShares = userShareOld.getShares();
            var userShareOldSharesFiltered = userShareOldShares.stream()
                    .filter(share -> share.getName().equals(shareAddRequestDTO.name()))
                    .findFirst();
            if(userShareOldSharesFiltered.isPresent()) {
                var userShareOldSharesFilteredValue = userShareOldSharesFiltered.get();
                userShareOldSharesFilteredValue.setAmount(calcAmount(userShareOldSharesFilteredValue.getAmount(), shareAddRequestDTO.amount()));
                userShareOldSharesFilteredValue.setTotalAmount(calcDailyTotalAmount(bistInfo.getValue(), userShareOldSharesFilteredValue.getAmount()));
            }
            else {
                userShareOldShares.add(UserSharesDTO.builder()
                        .name(shareAddRequestDTO.name())
                        .amount(shareAddRequestDTO.amount())
                        .totalAmount(calcDailyTotalAmount(bistInfo.getValue(), shareAddRequestDTO.amount()))
                        .build());
            }
            userShare.get().setTotalValue(userShare.get().getShares().stream()
                    .map(UserSharesDTO::getTotalAmount)
                    .map(BigDecimal::new)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            userShareRepository.save(userShareOld);
        }
    }

    @Override
    public BigDecimal getBalance(String name) {
        return userShareRepository.findById(name)
                        .map(userShares -> userShares.getShares()
                        .stream()
                        .peek(userSharesDTO -> {
                            var bistInfo = bistsRepository.findById(userSharesDTO.getName())
                                    .orElseThrow(() -> new RuntimeException("Share not found"));
                            userSharesDTO.setTotalAmount( calcDailyTotalAmount(bistInfo.getValue(), userSharesDTO.getAmount()));
                        })
                        .map(UserSharesDTO::getTotalAmount)
                        .map(BigDecimal::new)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<UserShareResponseDTO> getUserShares(String name) {
        return userShareRepository.findById(name)
                .map(userShares -> userShares.getShares()
                        .stream()
                        .peek(userSharesDTO -> {
                            var bistInfo = bistsRepository.findById(userSharesDTO.getName())
                                    .orElseThrow(() -> new RuntimeException("Share not found"));
                            userSharesDTO.setTotalAmount( calcDailyTotalAmount(bistInfo.getValue(), userSharesDTO.getAmount()));
                        })
                        .map(userSharesDTO -> UserShareResponseDTO.builder()
                                .name(userSharesDTO.getName())
                                .amount(userSharesDTO.getAmount())
                                .totalAmount(userSharesDTO.getTotalAmount())
                                .poster(bistsRepository.findById(userSharesDTO.getName())
                                        .orElseThrow(() -> new RuntimeException("Share not found"))
                                        .getPoster())
                                .price(bistsRepository.findById(userSharesDTO.getName())
                                        .orElseThrow(() -> new RuntimeException("Share not found"))
                                        .getValue())
                                .build())
                        .toList())
                .orElse(List.of());
    }

    @Override
    public void editShare(UserShareCreateRequest request, String name) {
        var userShare = userShareRepository.findById(name)
                .orElseThrow(() -> new RuntimeException("Share not found"));
        var userShareShares = userShare.getShares();
        var userShareSharesFiltered = userShareShares.stream()
                .filter(share -> share.getName().equals(request.name()))
                .findFirst();
        if(userShareSharesFiltered.isPresent()) {
            var userShareSharesFilteredValue = userShareSharesFiltered.get();
            userShareSharesFilteredValue.setAmount(request.amount());
            var bistInfo = bistsRepository.findById(userShareSharesFilteredValue.getName())
                    .orElseThrow(() -> new RuntimeException("Share not found"));
            userShareSharesFilteredValue.setTotalAmount(calcDailyTotalAmount(bistInfo.getValue(), userShareSharesFilteredValue.getAmount()));
        }
        userShare.setTotalValue(userShare.getShares().stream()
                .map(UserSharesDTO::getTotalAmount)
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        userShareRepository.save(userShare);
    }

    @Override
    public void deleteShareOnUser(String name, String userName) {
        var userShare = userShareRepository.findById(userName)
                .orElseThrow(() -> new RuntimeException("Share not found"));
        var userShareShares = userShare.getShares();
        var userShareSharesFiltered = userShareShares.stream()
                .filter(share -> share.getName().equalsIgnoreCase(name))
                .findFirst();
        userShareSharesFiltered.ifPresent(userShareShares::remove);
        userShare.setTotalValue(userShare.getShares().stream()
                .map(UserSharesDTO::getTotalAmount)
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        userShareRepository.save(userShare);
    }


    private String  calcAmount(String  value, String  amount) {
        return new BigDecimal(value).add(new BigDecimal(amount)).toString();
    }
    private String calcDailyTotalAmount(String amount, String value) {
        return String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(value)));
    }
}
