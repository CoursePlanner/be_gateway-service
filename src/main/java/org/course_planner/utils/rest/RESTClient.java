package org.course_planner.utils.rest;

import okhttp3.*;
import org.course_planner.utils.configs.CustomObjectMapper;
import org.course_planner.utils.exceptions.APIExecutionException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RESTClient {
    @Autowired
    private APIPropertyHelper apiPropertyHelper;

    public <T> GenericResponseTemplate<T> execute(final String hostReference, final String endpointReference,
                                                  HttpHeaders headerMap, Object request,
                                                  Map<String, String> queryParams, List<String> pathParams) {
        Headers headers = constructHeaders(headerMap);
        HttpMethod httpMethod = apiPropertyHelper.getMethod(hostReference, endpointReference);
        OkHttpClient okHttpClient = new CustomRESTClientBuilder(apiPropertyHelper)
                .getOkHttpClient(hostReference, endpointReference);
        String url = apiPropertyHelper.getHostBaseUrl(hostReference) +
                apiPropertyHelper.getEndpoint(hostReference, endpointReference);

        StringBuilder pathVariables = new StringBuilder();
        if (pathParams != null) {
            pathParams.forEach(pathVariable -> {
                pathVariables.append("/").append(pathVariable);
            });
        }

        Request okHttpRequest = httpMethod.equals(HttpMethod.GET) || httpMethod.equals(HttpMethod.DELETE)
                ? constructGetOrDeleteRequest(httpMethod, headers, url + pathVariables)
                : constructPutOrPostRequest(httpMethod, headers, request, url + pathVariables);

        try (Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute()) {
            if (okHttpResponse.isSuccessful()) {
                String body = okHttpResponse.body() != null
                        ? okHttpResponse.body().string() : "";
                return new GenericResponseTemplate<>(body, okHttpResponse.headers(), HttpStatus.valueOf(okHttpResponse.code()));
            }

            return new GenericResponseTemplate<>(null, okHttpResponse.headers(), HttpStatus.valueOf(okHttpResponse.code()));
        } catch (IOException e) {
            throw new APIExecutionException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Headers constructHeaders(HttpHeaders headers) {
        Headers.Builder builder = new Headers.Builder();

        if (headers != null) {
            if (headers.get(HttpHeaders.ACCEPT) == null) {
                builder.add(HttpHeaders.ACCEPT, org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
            }
            if (headers.get(HttpHeaders.CONTENT_TYPE) == null) {
                builder.add(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
            }

            headers.forEach((key, values) -> {
                builder.add(key, values.toString().substring(1, values.toString().length() - 1));
            });
        }
        return builder.build();
    }

    @NotNull
    private Request constructPutOrPostRequest(HttpMethod httpMethod, Headers headers, Object request, String url) {
        return new Request.Builder()
                .url(url)
                .method(httpMethod.toString(), RequestBody.create(getJsonBody(request),
                        MediaType.parse(Objects.requireNonNull(headers.get(HttpHeaders.CONTENT_TYPE)))))
                .headers(headers)
                .build();
    }

    @NotNull
    private Request constructGetOrDeleteRequest(HttpMethod httpMethod, Headers headers, String url) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(headers);
        if (httpMethod.equals(HttpMethod.DELETE)) {
            return builder.delete().build();
        }
        return builder.get().build();
    }

    public String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    private String getJsonBody(Object request) {
        try {
            return CustomObjectMapper.getObjectMapper().writeValueAsString(request);
        } catch (Exception ex) {
            return "";
        }
    }
}
