package com.dojo.common.channel;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChannelRPCConfig {
    private static ChannelRPCConfig instance;
    private final Map<String, ?> serviceConfig;

    private ChannelRPCConfig() {
        serviceConfig = new Gson().fromJson(
                new JsonReader(
                        new InputStreamReader(
                                ChannelRPCConfig.class.getResourceAsStream(
                                        "/retry-service-config.json"),
                                UTF_8)),
                Map.class);
    }

    public static ChannelRPCConfig getInstance() {

        if (instance == null) {
            instance = new ChannelRPCConfig();
        }

        return instance;

    }

    public Map<String, ?> getServiceConfig() {
        return serviceConfig;
    }
}
