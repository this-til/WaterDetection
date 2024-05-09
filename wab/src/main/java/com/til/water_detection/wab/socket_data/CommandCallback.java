package com.til.water_detection.wab.socket_data;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class CommandCallback<S extends SocketContext<?>> {

    public final String command;

    public CommandCallback(String... strings) {
        assert strings.length > 0;
        String command = String.join(" ", strings);
        assert !command.isEmpty();
        if (command.charAt(0) != '/') {
            command = "/" + command;
        }
        this.command = command;
    }

    public void successCallback(String[] strings, S socketContext) {
    }

    public void failCallback(String[] strings, S socketContext) {
    }

    public void outTime(S socketContext) {
    }

}
