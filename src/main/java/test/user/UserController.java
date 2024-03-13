package test.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping
    public ResponseEntity saveNewUser(HttpServletRequest request) throws Exception {
        log.info("Получен запрос на добавление пользователя '{}'", request);
        String uri = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String data = "";
        data = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Mono<String> mono = Mono.just(data);
        ClientResponse response = webClient.post()
                .uri(uriBuilder -> {
                    UriBuilder path = uriBuilder.path(uri);
                    for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                        path.queryParam(stringEntry.getKey(), Arrays.stream(stringEntry.getValue()).findFirst().get());
                    }
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .body(mono, String.class)
                .exchange()
                .block();
        String body = response.bodyToMono(String.class).block();
        HttpStatus httpStatus = response.statusCode();
        return new ResponseEntity(body, httpStatus);
    }

    @PutMapping()
    public ResponseEntity updateUser(HttpServletRequest request) throws Exception {
        log.info("Получен запрос на обновление данных пользователя '{}'", request);

        String uri = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String data = "";
        data = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Mono<String> mono = Mono.just(data);
        ClientResponse response = webClient.put()
                .uri(uriBuilder -> {
                    UriBuilder path = uriBuilder.path(uri);
                    for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                        path.queryParam(stringEntry.getKey(), Arrays.stream(stringEntry.getValue()).findFirst().get());
                    }
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .body(mono, String.class)
                .exchange()
                .block();
        String body = response.bodyToMono(String.class).block();
        HttpStatus httpStatus = response.statusCode();
        return new ResponseEntity(body, httpStatus);
    }

    @DeleteMapping()
    public void deleteUser(HttpServletRequest request) {
        log.info("Получен запрос - удалить данные пользователя '{}'", request);
        String uri = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();

        ClientResponse response = webClient
                .delete()
                .uri(uriBuilder -> {
                    UriBuilder path = uriBuilder.path(uri);
                    for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                        path.queryParam(stringEntry.getKey(), Arrays.stream(stringEntry.getValue()).findFirst().get());
                    }
                    return uriBuilder.build();
                }).accept(MediaType.APPLICATION_JSON).exchange().block();
        response.bodyToMono(String.class).block();
    }
}
