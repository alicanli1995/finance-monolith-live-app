package com.bist.api.scheduler;

import com.bist.api.model.Share;
import com.bist.api.repository.BistsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentScheduler {

    private final BistsRepository bistsRepository;


    @Scheduled(cron = "0 0 20 * * MON-FRI",
            zone = "Europe/Istanbul")
    private void deleteIfCommentDatePassThreeMonth() {
        bistsRepository.findAll()
                .stream()
                .parallel()
                .peek(share -> {
                    var comments =  (List<Share.Comment>) (Objects.isNull(share.getComments()) ? new ArrayList<>(): share.getComments());
                    comments.removeIf(comment -> isCommentDatePassThreeMonth(comment.getTimestamp()));
                })
                .forEach(bistsRepository::save);
    }

    private boolean isCommentDatePassThreeMonth(LocalDateTime timestamp) {
        return timestamp.isBefore(LocalDateTime.now().minusMonths(3));
    }


}
