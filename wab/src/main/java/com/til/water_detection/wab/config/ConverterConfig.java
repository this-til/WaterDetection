package com.til.water_detection.wab.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.til.water_detection.data.state.DataState;
import com.til.water_detection.wab.component.IntToDataStateConverter;
import com.til.water_detection.wab.component.StringToTimestampConverter;
import com.til.water_detection.wab.json_serializer.DataStateToIntSerializer;
import com.til.water_detection.wab.json_serializer.DateToLongSerializer;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {


    @Resource
    private StringToTimestampConverter stringToTimestampConverter;

    @Resource
    private IntToDataStateConverter intToDataStateConverter;

    @Resource
    private DateToLongSerializer dateToLongSerializer;

    @Resource
    private DataStateToIntSerializer dataStateToIntSerializer;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToTimestampConverter);
        registry.addConverter(intToDataStateConverter);
    }

    @Bean
    public SimpleModule customModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, dateToLongSerializer);
        module.addSerializer(DataState.class, dataStateToIntSerializer);
        return module;
    }


}
