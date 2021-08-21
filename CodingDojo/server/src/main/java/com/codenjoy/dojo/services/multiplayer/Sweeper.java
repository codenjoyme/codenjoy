package com.codenjoy.dojo.services.multiplayer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiPredicate;

import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.RELOAD_ALONE;

@Getter
@RequiredArgsConstructor
public class Sweeper {

    private MultiplayerType type;
    private final boolean resetOther;
    private BiPredicate<GamePlayer, List<GamePlayer>> applicants;

    /**
     * Любая работа с юзером в комнате (удаление или перезапуск) происходит
     * с перегрузкой оставшихся на том же поле других участников.
     * Критерий отбора кандидатов задается отдельно.
     */
    public static Sweeper on() {
        return new Sweeper(RELOAD_ALONE);
    }

    /**
     * Любая работа с юзером в комнате (удаление или перезапуск) происходит
     * с игнорированием оставшихся на том же поле других участников.
     */
    public static Sweeper off() {
        return new Sweeper(!RELOAD_ALONE).noOne();
    }

    /**
     * Никто из оставшихся на поле не будет обрабатываться.
     */
    public Sweeper noOne() {
        applicants = (player, players) -> false;
        return this;
    }

    /**
     * Работа проводится только с последним оставшимся на поле,
     * если на то будет воля MultiplayerType.
     */
    public Sweeper lastAlone() {
        applicants = (player, players) -> type.shouldReloadAlone() && players.size() == 1;
        return this;
    }

    /**
     * Работа проводится со всеми оставшимся на пооле независимо от
     * MultiplayerType.
     */
    public Sweeper allRemaining() {
        applicants = (player, players) -> true;
        return this;
    }

    /**
     * Задаем MultiplayerType для последующего анализа.
     */
    public Sweeper of(MultiplayerType type) {
        this.type = type;
        return this;
    }
}
