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


import com.codenjoy.dojo.services.classloader.GameLoader;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.utils.ReflectUtils;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component("gameService")
public class GameServiceImpl implements GameService {

    // TODO кажется это старый код комнат, его можно убрать после окончательной имплементации комнат
    public static final String ROOMS_SEPARATOR = "-";

    private Map<String, GameType> cache = new TreeMap<>();

    @Autowired
    protected RoomService roomService;

    @Value("${plugins.enable}")
    private boolean pluginsEnable;

    @Value("${plugins.path}")
    private String pluginsPath;

    @Value("${plugins.game.exclude}")
    protected String[] excludeGames;

    @Value("${plugins.game.package}")
    private String gamePackage;

    @PostConstruct
    public void init() {
        // TODO сделать перезагрузку этого всего контента по запросу админа, но только для тех игрушек, что обновились
        // TODO так же надо будет для новозагруженной игры всех юзеров перезапустить
        for (Class<? extends GameType> clazz : allGames()) {
            GameType gameType = loadGameType(clazz);
            String name = gameType.name();
            cache.put(name, gameType);
            // создаем комнаты для каждой игры сразу
            roomService.create(name, gameType);
        }
    }

    private List<Class> allGames() {
        List<Class> result = new LinkedList<>(
                findInPackage(gamePackage));

        result.sort(Comparator.comparing(Class::getName));

        result.remove(NullGameType.class);

        if (pluginsEnable) {
            loadFromPlugins(result);
        }

        // remove abstract and other stub/fake classes
        remove(result,
                it -> ConstructorUtils.getMatchingAccessibleConstructor(it) == null
                        || Modifier.isAbstract(it.getModifiers()));

        if (excludeGames != null) {
            remove(result, it -> Stream.of(excludeGames)
                    .anyMatch(name -> it.getPackage().toString().contains(name)));
        }

        return result;
    }

    private void loadFromPlugins(List<Class> result) {
        File directory = new File(pluginsPath);
        Map<String, Class> games = new GameLoader().loadGames(directory);
        result.addAll(games.values());
    }

    private void remove(List<Class> result, Predicate<Class> predicate) {
        result.removeAll(result.stream()
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    protected Collection<? extends Class> findInPackage(String packageName) {
        return ReflectUtils.findInPackage(packageName, GameType.class);
    }

    @Override
    public List<String> getGames() {
        return new LinkedList<>(cache.keySet());
    }

    @Override
    public List<String> getRooms() {
        return new LinkedList<>(roomService.names());
    }

    @Override
    public List<String> getOnlyGames() {
        return getGames().stream()
                .map(GameServiceImpl::removeNumbers)
                .collect(Collectors.toList());
    }

    public static String removeNumbers(String game) {
        return game.split(ROOMS_SEPARATOR)[0];
    }

    @Override
    public Map<String, List<String>> getSpritesNames() {
        return getStringListMap(plot -> plot.name().toLowerCase());
    }

    @Override
    public Map<String, List<String>> getSpritesValues() {
        return getStringListMap(plot -> String.valueOf(plot.ch()));
    }

    // TODO может сделать универсальную версию метода с CharElements и ну его два вурхних метода?
    @Override
    public Map<String, List<String>> getSprites() {
        return getStringListMap(plot -> plot.name().toLowerCase() + "=" + plot.ch());
    }

    private Map<String, List<String>> getStringListMap(Function<CharElements, String> mapper) {
        return cache.entrySet().stream()
                .map(entry -> 
                    new HashMap.SimpleEntry<>(
                            entry.getValue().name(),
                            Arrays.stream(entry.getValue().getPlots())
                                    .map(mapper)
                                    .collect(toList())
                    )
                )
                .collect(toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
    }

    @SneakyThrows
    private GameType loadGameType(Class<? extends GameType> gameType) {
        return gameType.newInstance();
    }

    @Override
    public GameType getGameType(String game) {
        if (!exists(game)) {
            return NullGameType.INSTANCE;
        }

        return cache.get(game);
    }

    @Override
    public GameType getGameType(String game, String room) {
        if (!exists(game) || Validator.isEmpty(room)) {
            return NullGameType.INSTANCE;
        }

        GameType gameType = cache.get(game);

        return roomService.create(room, gameType);
    }

    @Override
    public String getDefaultRoom() {
        return getRooms().iterator().next();
    }

    @Override
    public boolean exists(String game) {
        return !Validator.isEmpty(game)
                && cache.containsKey(game);
    }

    /**
     * @return Вовзращает сейв для этой игры по умолчанию с прогрессом
     * (если он предусмотрен) на первом уровне.
     */
    @Override // TODO test me
    public String getDefaultProgress(GameType gameType) {
        try {
            // TODO почему-то на проде тут NPE
            MultiplayerType type = gameType.getMultiplayerType(gameType.getSettings());
            JSONObject save = type.progress().saveTo(new JSONObject());
            return save.toString().replace('"', '\'');
        } catch (Exception e) {
            log.error("Something wrong while getDefaultProgress", e);
            return "{}";
        }
    }
}
