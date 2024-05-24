package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.state.ResultType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@AllArgsConstructor
public final class ReturnPackage {
    private ResultType returnState;
    @Nullable
    private ByteBuf information;
}
