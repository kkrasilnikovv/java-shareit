package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                         @RequestBody @Valid RequestDto requestDto) {
        log.info("create request");
        return requestClient.create(sharerUserId, requestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> findByRequester(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("get requests by user {}", sharerUserId);
        return requestClient.getByRequester(sharerUserId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                          @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("get requests all");
        return requestClient.getAll(sharerUserId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                           @PathVariable Long requestId) {
        log.info("get request id = {}", requestId);
        return requestClient.getById(sharerUserId, requestId);
    }
}