package com.codenjoy.dojo.services.whatsnext;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ActionsParser {

    private static Pattern ACTION_PATTERN = Pattern.compile("(\\((\\d)\\)->\\[(.*)\\])", Pattern.CASE_INSENSITIVE);

    private String actions;

    public ActionsParser(String actions) {
        this.actions = actions;
    }

    public List<Map<Integer, String>> getTicksActions() {
        List<String> ticks = split(actions, ";", true);
        return ticks.stream()
                .map(tick -> command(tick))
                .collect(toList());
    }

    private Map<Integer, String> command(String tick) {
        List<String> actionsString = split(tick, "&", false);
        return actionsString.stream()
                .map(actionString -> {
                    Matcher matcher = ACTION_PATTERN.matcher(actionString);
                    if (matcher.matches()) {
                        String index = matcher.group(2);
                        String action = matcher.group(3);
                        return new HashMap.SimpleEntry<>(Integer.valueOf(index), action);
                    } else {
                        throw new RuntimeException("Unparsed action: " + actionString);
                    }
                })
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> split(String tick, String regex, boolean empty) {
        return Arrays.stream(tick.split(regex, -1))
                .filter(line -> empty || !StringUtils.isEmpty(line))
                .collect(toList());
    }
}
