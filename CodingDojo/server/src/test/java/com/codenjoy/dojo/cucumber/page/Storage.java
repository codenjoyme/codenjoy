package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.client.Closeable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Storage implements Closeable {

    private Map<String, String> data = new HashMap<>();

    public void save(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public Set<Map.Entry<String, String>> all() {
        return data.entrySet();
    }

    @Override
    public void close() {
        data.clear();
    }
}
