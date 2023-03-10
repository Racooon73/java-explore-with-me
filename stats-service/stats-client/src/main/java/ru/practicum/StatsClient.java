package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class StatsClient {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String application;
    private final String statsServiceUri;
    private final ObjectMapper json;
    private final HttpClient httpClient;

    public StatsClient(@Value("${spring.application.name}") String application,
                       @Value("${services.stats-service.uri:http://stats-server:9090}") String statsServiceUri,
                       ObjectMapper json) {
        this.application = application;
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void hit(HttpServletRequest userRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HitRequestDto hit = HitRequestDto.builder()
                .app(application)
                .ip(userRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .uri(userRequest.getRequestURI())
                .build();
        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers.ofString(json.writeValueAsString(hit));
            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();
            HttpResponse<Void> response = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
            log.debug("?????????? ???? ?????????????? ????????????????????: {}", response);

        } catch (Exception e) {
            log.error("???????????????????? ???? ????????????????????: ????????????", e);
        }

    }

    public List<HitResponseDto> getStats(ViewsStatsRequest request) {
        try {

            String queryString = toQueryString(request.toBuilder().application(application).build());

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/stats" + queryString))
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return json.readValue(response.body(), new TypeReference<>() {
                });
            }
            log.debug("?????????? ???? ?????????????? ????????????????????: {}", response);
        } catch (Exception e) {
            log.error("???????????????????? ???????????????? ???????????????????? ?????? ??????????????: " + request, e);
        }
        return Collections.emptyList();
    }

    private String toQueryString(ViewsStatsRequest request) {
        String start = encode(DATE_TIME_FORMATTER.format(request.getStart()));
        String end = encode(DATE_TIME_FORMATTER.format(request.getEnd()));

        String queryString = String.format("?start=%s&end=%s&unique=%b&application=%s",
                start, end, request.isUnique(), application);

        if (request.hasUriCondition()) {
            queryString += "&uris=" + String.join(",", request.getUris());
        }
        if (request.hasLimitCondition()) {
            queryString += "&limit=" + request.getLimit();
        }
        return queryString;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
