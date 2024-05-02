package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Result<T> {
    private ResultType resultType;
    private String message;
    @Nullable
    private T data;

    public Result(ResultType resultType, String message, @Nullable T data) {
        this.resultType = resultType;
        this.message = message == null ? "" : message;
        this.data = data;
    }


    public static <T> Result<T> successful(@Nullable String message) {
        return new Result<>(ResultType.SUCCESSFUL, message == null ? "" : message, null);
    }

    public static <T> Result<T> fail(@Nullable String message) {
        return new Result<>(ResultType.FAIL, message == null ? "" : message, null);
    }

    public static <T> Result<T> error(@Nullable String message) {
        return new Result<>(ResultType.ERROR, message == null ? "" : message, null);
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return resultType == result.resultType && Objects.equals(message, result.message) && Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultType, message, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultType=" + resultType +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
