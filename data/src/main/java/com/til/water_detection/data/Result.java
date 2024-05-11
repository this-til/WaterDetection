package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private ResultType resultType;
    private String message;
    @Nullable
    private T data;

    public static <T> Result<T> successful(@Nullable String message) {
        return new Result<>(ResultType.SUCCESSFUL, message == null ? "" : message, null);
    }

    public static <T> Result<T> fail(@Nullable String message) {
        return new Result<>(ResultType.FAIL, message == null ? "" : message, null);
    }

    public static <T> Result<T> error(@Nullable String message) {
        return new Result<>(ResultType.ERROR, message == null ? "" : message, null);
    }

}
