package org.course_planner.utils.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Headers;
import org.course_planner.utils.configs.CustomObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseTemplate<T> {
    private String body;
    private HttpHeaders headers;
    private HttpStatus httpStatus;

    public GenericResponseTemplate(String body, Headers headers, HttpStatus status) {
        this.body = body;
        this.httpStatus = status;
        this.headers = new HttpHeaders();
        if (headers != null) {
            this.headers.putAll(headers.toMultimap());
        }
    }

    public T getBody(Class<T> returnType) {
        try {
            ObjectMapper objectMapper = CustomObjectMapper.getObjectMapper();
            return objectMapper.readValue(body, returnType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
