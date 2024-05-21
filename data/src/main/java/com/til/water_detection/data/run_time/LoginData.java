package com.til.water_detection.data.run_time;

import lombok.Data;

import java.util.List;

@Data
public class LoginData {
    private String username;
    private String password;
    private String equipment;
    private List<String> dataNameList;
    private List<String> actuatorNameList;

}
