package test.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/**")
/**
 *
 * на https://jsonplaceholder.typicode.com/ запросы к /api/*** всегда возвращают "{}"
 * без /api/ запросы возвращают данные
 */
public class UserController {
    private final WebClient webClient;

    @GetMapping
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        log.info("Получен запрос на получение списка всех пользователей");
        String uri = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();

        ClientResponse response = webClient
                .get()
                .uri(uriBuilder -> {
                    UriBuilder path = uriBuilder.path(uri);
                    for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                        path.queryParam(stringEntry.getKey(), Arrays.stream(stringEntry.getValue()).findFirst().get());
                    }
                    return uriBuilder.build();
                }).accept(MediaType.APPLICATION_JSON).exchange().block();
        String body = response.bodyToMono(String.class).block();
        HttpStatus httpStatus = response.statusCode();
        return new ResponseEntity(body, httpStatus);
    }

    /*@PostMapping
    public UserDto saveNewUser(@Valid @RequestBody HttpServletRequest request) {
        log.info("Получен запрос на добавление пользователя '{}'", request);
        Mono<UserDto> userDtoMono = webClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserDto.class);
        return userDtoMono.block();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId, @RequestBody HttpServletRequest request) {
        log.info("Получен запрос на обновление данных пользователя '{}'", userId);
        if (dto.getId() == null) {
            dto.setId(userId);
        }
        Mono<UserDto> userDtoMono = webClient.patch()
                .uri("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserDto.class);
        return userDtoMono.block();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        log.info("Получен запрос - показать данные пользователя '{}'", userId);
        Mono<UserDto> userDtoMono = webClient.get()
                .uri("/users/{userId}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
        return userDtoMono.block();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Получен запрос - удалить данные пользователя '{}'", userId);
        webClient.delete()
                .uri("/users/{userId}", userId)
                .retrieve()
                .toBodilessEntity();
    }*/
}
