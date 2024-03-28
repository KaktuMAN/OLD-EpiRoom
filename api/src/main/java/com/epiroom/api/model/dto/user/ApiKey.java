package com.epiroom.api.model.dto.user;

import com.epiroom.api.model.User;

public class ApiKey {
    private final String apiKey;

    public ApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ApiKey(User user) {
        this.apiKey = user.getApiKey();
    }

    public String getApiKey() {
        return apiKey;
    }
}
