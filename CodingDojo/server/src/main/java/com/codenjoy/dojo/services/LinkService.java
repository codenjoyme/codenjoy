package com.codenjoy.dojo.services;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LinkService {
    private Map<String, Map<String, Object>> data = new ConcurrentHashMap<>();
    private long count = 0;

    public class LinkStorage {
        private String link;
        private Map<String, Object> map;

        public LinkStorage(String link, Map<String, Object> map) {
            this.link = link;
            this.map = map;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public String getLink() {
            return link;
        }
    }

    public LinkStorage forLink(){
        String link = Hash.md5("soul" + count++);
        Map<String, Object> map = data.get(link);
        if (map == null) {
            map = new HashMap<>();
            data.put(link, map);
        }
        return new LinkStorage(link, map);
    }

    public Map<String, Object> getData(String link) {
        return data.get(link);
    }
}
