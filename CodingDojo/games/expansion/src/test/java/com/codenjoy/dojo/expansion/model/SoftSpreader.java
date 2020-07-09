package com.codenjoy.dojo.expansion.model;

import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Single;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Oleksandr_Baglai on 2019-09-14.
 */
public class SoftSpreader {

    private List<EventListener> listeners;
    private List<Single> games;
    private List<Hero> heroes;

    private GameRunner gameRunner;
    private List<Expansion> currents;
    private List<Integer> levelNumbers;
    private Map<String, Integer> fullness;

    public SoftSpreader(GameRunner gameRunner) {
        this.gameRunner = gameRunner;

        levelNumbers = new LinkedList<>();
        fullness = new HashMap<>();
        currents = new LinkedList<>();

        listeners = new LinkedList<>();
        games = new LinkedList<>();
        heroes = new LinkedList<>();
    }

    public void reloadLevelForWinner(int player) {
        Single game = games.get(player);
        assertEquals(true, game.isWin());

        reloadLevel(player);
    }

    public void reloadLevel(int player) {
        createNewLevelIfNeeded(player);
        removeFromBoard(player);

        Single game = games.get(player);
        game.on(currents.get(player));
        game.newGame();

        heroes.set(player, (Hero) game.getPlayer().getHero());
    }

    protected void createOneMorePlayer() {
        int playerIndex = games.size();
        createNewLevelIfNeeded(playerIndex);

        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        String id = String.format("demo%s", playerIndex + 1);
        Player player = (Player) gameRunner.createPlayer(listener, id);
        Single game = new Single(player, gameRunner.getPrinterFactory());
        game.on(currents.get(playerIndex));
        game.newGame();
        games.add(game);

        heroes.add((Hero) game.getPlayer().getHero());
    }

    protected void destroy(int player) {
        removeFromBoard(player);
        games.set(player, null);
    }

    private void removeFromBoard(int player) {
        if (player == games.size()) {
            return;
        }
        Game game = games.get(player);
        game.getField().remove(game.getPlayer());
    }

    // TODO этот ужас надо забыть как страшный сон
    // но пока что он эмулирует работу фреймфорка по принципу:
    // все начинают со своих независимых single уровней пока не доберутся до multiple (последний)
    // тогда они объединяются на одном поле, но если случится переполнение и баз свободных нет
    // тогда новому игроку ничего не останется, кроме как занять новое свободное multiple поле
    // и быть там первым
    private void createNewLevelIfNeeded(int player) {
        // это нового плеера мы пытаемся сейчас создать или игрок уже играл?
        boolean newPlayer = (player == currents.size());

        // если это новый пользователь или базы на текущем multiple уровне не свободны (или были заняты в прошлом)
        // то надо потрудиться и разместить его как-то
        if (newPlayer || fullness.get(currents.get(player).id()) == currents.get(player).allBases()) {
            // это у нас индекс уровня на котором уже multiple
            // он = 0 если нет single уровней
            int multipleIndex = multipleIndex();
            // текущий юзер будет переходить на single или уже на multiple
            boolean willGoOnSingle = (newPlayer  || levelNumbers.get(player) + 1 < multipleIndex)
                                    && multipleIndex != 0;
            // есть ли хоть один другой юзер который уже перешел на multiple?
            boolean hasAnotherMultiple = multipleIndex == levelNumbers.stream().max(Integer::compareTo).orElseGet(() -> 0);

            // надо ли создавать новую комнату?
            boolean createNew = false;
            if (willGoOnSingle || !hasAnotherMultiple) {
                // надо если мы будем переходить на single
                // или переход будет на multiple но ни одного игрока добравшегося сюда нет
                createNew = true;
            } else { // а тут у нас переход на multiple и по идее надо найти комнату и попасть в нее
                // мы самовыпилимся из комнаты где мы сейчас, чтобы если мы на multiple то не мешать другим туда попасть
                removeFromBoard(player);

                // мы пытаемся найти комнату multiple со свободными базами
                boolean found = false;
                for (int i = 0; i < levelNumbers.size(); i++) {
                    // одна из комнат, мы не знаем свободна ли, на multiple ли
                    Expansion current = currents.get(i);
                    // сколько там юзеров было максимально
                    Integer max = fullness.get(current.id());
                    // она и свободная и multiple - нам подходит
                    if (levelNumbers.get(i) == multipleIndex && max < 4) {
                        if (newPlayer) {
                            currents.add(current);
                            levelNumbers.add(levelNumbers.get(i));
                        } else {
                            currents.set(player, current);
                        }
                        // в комнату куда пришел игрок, стало на 1 больше
                        fullness.put(current.id(), max + 1);
                        found = true;
                        break;
                    }
                }
                // если не нашли
                if (!found) {
                    // то создаем
                    createNew = true;
                    // и если мы переходим из multiple на multiple
                    // но это не первая комната в кейзе без single
                    if (!newPlayer && levelNumbers.get(player) == multipleIndex) {
                        // нам надо сделать вид, будьто бы мы с single на multiple перешли
                        // потому что скрипт ниже так заточен что переходит на n+1 уровнень
                        levelNumbers.set(player, levelNumbers.get(player) - 1);
                    }
                }
            }
            // если надо создать новую комнату - создаем
            if (createNew) {
                if (newPlayer) {
                    // для нового игрока просто доабвляем level = 0
                    levelNumbers.add(0);
                } else {
                    // для существущего level++
                    levelNumbers.set(player, levelNumbers.get(player) + 1);
                }

                // создаем комнату с этим уровнем
                Expansion current = (Expansion) gameRunner.createGame(levelNumbers.get(player));

                if (newPlayer) {
                    // для нового игрока добавляем его комнату
                    currents.add(current);
                } else {
                    // для существующего заменяем старую
                    currents.set(player, current);
                }
                // он в своей комнате один
                fullness.put(current.id(), 1);
            }
        }
    }

    private int multipleIndex() {
        MultiplayerType type = gameRunner.getMultiplayerType();
        if (type.isTraining()) {
            return type.getLevelsCount();
        } else {
            return 0;
        }
    }

    public void tickAll() {
        games.stream()
                .filter(single -> single != null)
                .map(Single::getField)
                .distinct()
                .filter(field -> field != null)
                .forEach(GameField::tick);
        gameRunner.tick();
    }

    public IField field(int player) {
        return (IField) games.get(player).getField();
    }

    public List<Hero> heroes() {
        return heroes;
    }

    public Single single(int index) {
        return games.get(index);
    }

    public EventListener listener(int index) {
        return listeners.get(index);
    }

    public int count() {
        return games.size();
    }

    public void clear() {
        games.clear();
        heroes.clear();
        listeners.clear();
        currents.clear();
        levelNumbers.clear();
        fullness.clear();
    }

    public String gameId(int index) {
        return player(index).getField().id();
    }

    private Player player(int index) {
        return (Player)single(index).getPlayer();
    }

    public String playerId(int index) {
        return player(index).lg.id();
    }

    public Hero hero(int index) {
        return (Hero)single(index).getJoystick();
    }

    // если игрок походил то в его комнате считаем, что уже все заполнено
    public void roomIsBusy(int index) {
        if (!data.waitingOthers()) {
            Expansion current = currents.get(index);
            fullness.put(current.id(), current.allBases());
        }
    }
}
