package com.example.palianytsia.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private Status status;
    private T payload;
    private Object errors;

    public enum Status {
        OK, BAD_REQUEST, UNAUTHORIZED, VALIDATION_EXCEPTION, EXCEPTION, WRONG_CREDENTIALS, ACCESS_DENIED,
        NOT_FOUND, DUPLICATE_ENTITY, NOT_MATCHING
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        response.setStatus(Status.OK);
        return response;
    }

    public static <T> Response<T> notMatching() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_MATCHING);
        return response;
    }

}
