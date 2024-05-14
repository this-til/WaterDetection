package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.state.ResultType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public final class ReturnPackage {
    private ResultType returnState;
    private byte[] information;
}
