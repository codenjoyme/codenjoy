package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.hash.Hash;
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
