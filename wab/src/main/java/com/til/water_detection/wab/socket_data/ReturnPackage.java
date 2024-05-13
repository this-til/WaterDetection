package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.state.ResultType;
import lombok.Getter;

@Getter
public record ReturnPackage(ResultType returnState, byte[] information) {


}
