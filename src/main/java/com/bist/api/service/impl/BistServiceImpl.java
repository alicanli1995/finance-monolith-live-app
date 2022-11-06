package com.bist.api.service.impl;

import com.bist.api.model.BistModel;
import com.bist.api.model.Share;
import com.bist.api.model.UserExtra;
import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.repository.UserExtraRepository;
import com.bist.api.rest.dto.*;
import com.bist.api.service.BistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BistServiceImpl implements BistService {

    private static final String BIST_NOT_FOUND = "Bist not found";

    private final BistsRepository bistsRepository;

    private final BistValueRepository bistValueRepository;
    private final UserExtraRepository userExtraRepository;

    @Override
    public List<ShareGenericModel> getBists() {
        return bistsRepository.findAll()
                .stream()
                .parallel()
                .map(bist -> ShareGenericModel.builder()
                        .poster(bist.getPoster())
                        .name(bist.getName())
                        .value(bist.getValue())
                        .dailyVolume(bist.getDailyVolume())
                        .dailyChangePercentage(bist.getDailyChangePercentage())
                        .description(bist.getDescription())
                        .build())
                .sorted(Comparator.comparing(ShareGenericModel::getName))
                .toList();
    }

    @Override
    public void editBist(ShareGenericModel shareGenericModel) {

        bistsRepository.findById(shareGenericModel.getName())
                .ifPresentOrElse(bist -> {
                    bist.setValue(shareGenericModel.getValue());
                    bist.setDailyChange(shareGenericModel.getDailyVolume());
                    bist.setDailyChangePercentage(shareGenericModel.getDailyChangePercentage());
                    bist.setDailyVolume(shareGenericModel.getDailyVolume());
                    bist.setPoster(shareGenericModel.getPoster());
                    bistsRepository.save(bist);
                }, () -> log.error(BIST_NOT_FOUND));

        log.info("Bist edited");
    }

    @Override
    public List<CommentResponseDTO> getComments(String name) {
        return bistsRepository.findById(name)
                .map(Share::getComments)
                .orElseThrow(() -> new RuntimeException(BIST_NOT_FOUND))
                .stream()
                .map(comment -> CommentResponseDTO.builder()
                        .name(name)
                        .comments(List.of(CommentResponseDTO.CommentDto.builder()
                                .username(comment.getUsername())
                                .avatar(userExtraRepository.findById(comment.getUsername())
                                        .map(UserExtra::getAvatar)
                                        .orElse(""))
                                .text(comment.getText())
                                .timestamp(comment.getTimestamp())
                                .build()))
                        .build())
                .toList();
    }

    @Override
    public HistoryResponseModel getBistHistory(String name, int hours) {
        var values = bistValueRepository.findAllByName(name)
                .stream()
                .parallel()
                .filter(history -> getHistoryForHours(history, hours))
                .map(x -> new History(new BigDecimal(x.getValue()), x.getCreatedAt()
                        .toInstant(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()))))
                .toList();
        return bistsRepository.findById(name)
                .map(bist -> HistoryResponseModel.builder()
                        .name(bist.getName())
                        .dailyChangePercentage(bist.getDailyChangePercentage())
                        .dailyVolume(bist.getDailyVolume())
                        .poster(bist.getPoster())
                        .rank(getRankListOfShare(bist.getName()))
                        .values(values.stream().sorted(Comparator.comparing(History::date)).toList())
                        .comments(bist.getComments() == null ? new ArrayList<>() : getComments(bist))
                        .build())
                .orElseThrow(() -> new RuntimeException(BIST_NOT_FOUND));
    }

    private boolean getHistoryForHours(BistModel history, int hours) {
        var isHoliday = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(isHoliday);
        var now = LocalDateTime.now();
        var createdAt = LocalDateTime.ofInstant(history.getCreatedAt().toInstant(
                ZoneId.systemDefault().getRules().getOffset(now)), ZoneId.systemDefault());
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && now.getHour() > 0 && now.getHour() < 10) {
            return createdAt.isAfter(now.minusHours(hours + 72L));
        }
        return switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY -> now.minusHours(hours + 24L).isBefore(createdAt);
            case Calendar.SUNDAY -> now.minusHours(hours + 48L).isBefore(createdAt);
            default -> now.minusHours(hours).isBefore(createdAt);
        };
    }


    private String getRankListOfShare(String name) {
        // TODO: customize exception implementation needed
        var share = bistsRepository.findById(name).orElseThrow(() -> new RuntimeException(BIST_NOT_FOUND));
        var allShares = bistsRepository.findAll()
                .stream()
                .parallel()
                .sorted(Comparator.comparing(Share::getDailyVolume).reversed())
                .toList();
        var rank = allShares.stream()
                .parallel()
                .filter(x -> x.getValue().compareTo(share.getValue()) > 0)
                .count();
        return String.valueOf(rank + 1);
    }


    @Override
    public CommentResponseDTO addCommentBist(String name, AddCommentRequest addingComment, String username) {
        var bist = bistsRepository.findById(name)
                .orElseThrow(() -> new RuntimeException(BIST_NOT_FOUND));

        if (Objects.isNull(bist.getComments())) {
            bist.setComments(List.of(new Share.Comment(username, addingComment.getText(), LocalDateTime.now())));
        }else bist.getComments().add(0, new Share.Comment(username, addingComment.getText(), LocalDateTime.now()));
        bistsRepository.save(bist);
        return CommentResponseDTO.builder()
                .name(bist.getName())
                .comments(getComments(bist))
                .build();
    }

    @Override
    public void deleteComment(String name, String username, CommentDeleteRequestDTO comment)  {
        var bist = bistsRepository.findById(name)
                .orElseThrow(() -> new RuntimeException(BIST_NOT_FOUND));
        bist.getComments().removeIf(x -> x.getUsername().equals(username) && x.getText().equals(comment.comment()));
        bistsRepository.save(bist);
    }

    private List<CommentResponseDTO.CommentDto> getComments(Share bist) {
        return bist.getComments()
                .stream()
                .map(x -> new CommentResponseDTO.CommentDto(x.getUsername(), getAvatar(x.getUsername()), x.getText(), x.getTimestamp()))
                .toList();
    }

    private String getAvatar(String username) {
        return userExtraRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getAvatar();
    }


}
