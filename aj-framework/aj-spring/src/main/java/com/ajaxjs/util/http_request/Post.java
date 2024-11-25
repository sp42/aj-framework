package com.ajaxjs.util.http_request;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

@Slf4j
public class Post {
    public static String postForm(String url, String body) {

        return postForm(url, body, null);
    }

    public static String postForm(String url, String body, Consumer<HttpRequest.Builder> init) {
        return post(url, body, req -> {
            req.header("Content-Type", "application/x-www-form-urlencoded");

            if (init != null)
                init.accept(req);
        });
    }

    public static String postJson(String url, String body, Consumer<HttpRequest.Builder> init) {
        return post(url, body, req -> {
            req.header("Content-Type", "application/json");

            if (init != null)
                init.accept(req);
        });
    }

    public static String post(String url, String body, Consumer<HttpRequest.Builder> init) {
        HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.ofString(body));

        if (init != null)
            init.accept(request);

        HttpResponse<String> response;

        //设置建立连接超时 connect timeout
        var httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(3000)).build();

        try {
            response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            String msg = "HTTP POST error. The url is: " + url;
            log.error(msg, e);
            throw new RequestException(msg);
        }

        return response.body();
    }
}