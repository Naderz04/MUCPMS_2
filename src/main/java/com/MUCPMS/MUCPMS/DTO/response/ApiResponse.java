package com.MUCPMS.MUCPMS.DTO.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private int code;
    private LocalDateTime date;

    public ApiResponse(boolean success, T data, String message, int code) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
        this.date = LocalDateTime.now(); // Set the current date and time
    }

}