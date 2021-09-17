package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class PlayersCache {

    @Autowired
    private Registration registration;

    private final Map<String, String> names = new ConcurrentHashMap<>();

    public void removeAll() {
        names.clear();
    }

    public String name(String id) {
        if (!names.containsKey(id)) {
            String name = registration.getNameById(id);
            name = isEmpty(name) ? "player[" + id + "]" : name;
            names.put(id, name);
        }
        return names.get(id);
    }
}
