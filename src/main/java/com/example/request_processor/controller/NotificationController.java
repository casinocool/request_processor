package com.example.request_processor.controller;

import com.example.request_processor.dto.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.request_processor.strategy.NotificationStrategyFactory;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Уведомления", description = "API для отправки уведомлений через различные каналы")
public class NotificationController {

    private final NotificationStrategyFactory strategyFactory;

    @PostMapping
    @Operation(
            summary = "Отправить уведомление",
            description = "Отправляет уведомление выбранным способом (SMS, Email, Push, Telegram). " +
                    "Сообщение сохраняется в outbox и будет отправлено асинхронно."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Уведомление принято к отправке",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"status\": \"accepted\", \"messageId\": \"550e8400-e29b-41d4-a716-446655440000\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Validation failed\", \"field\": \"type\", \"message\": \"must not be null\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    public ResponseEntity<Void> sendNotification(
            @Parameter(
                    description = "Данные уведомления",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "SMS уведомление",
                                            summary = "Пример SMS уведомления",
                                            value = "{\"type\": \"SMS\", \"message\": \"Ваш код подтверждения: 123456\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Email уведомление",
                                            summary = "Пример Email уведомления",
                                            value = "{\"type\": \"EMAIL\", \"message\": \"Добро пожаловать в наш сервис!\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Push уведомление",
                                            summary = "Пример Push уведомления",
                                            value = "{\"type\": \"PUSH\", \"message\": \"У вас новое сообщение\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Telegram уведомление",
                                            summary = "Пример Telegram уведомления",
                                            value = "{\"type\": \"TG_MESSAGE\", \"message\": \"Привет из бота!\"}"
                                    )
                            }
                    )
            )
            @Valid @RequestBody NotificationRequest request) {

        var strategy = strategyFactory.getStrategy(request.getType());
        strategy.process(request);
        return ResponseEntity.accepted().build();
    }
}