package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 21:35
 */
public class PlayerGame implements Tickable {

    public static final PlayerGame NULL = new NullPlayerGame();

    private Player player;
    private Game game;
    private PlayerController controller;
    private Tickable lazyJoystick;

    public PlayerGame(Player player, Game game, PlayerController controller, Tickable lazyJoystick) {
        this.player = player;
        this.game = game;
        this.controller = controller;
        this.lazyJoystick = lazyJoystick;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == PlayerGame.NULL && (o != Player.NULL && o != PlayerGame.NULL)) return false;

        if (o instanceof Player) {
            Player p = (Player)o;

            return p.equals(player);
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            return pg.player.equals(player);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    public void remove() {
        controller.unregisterPlayerTransport(player);
        game.destroy();
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public PlayerController getController() {
        return controller;
    }

    @Override
    public String toString() {
        return String.format("PlayerGame[player=%s, game=%s, controller=%s]",
                player,
                game.getClass().getSimpleName(),
                controller.getClass().getSimpleName());
    }

    @Override
    public void tick() {
        lazyJoystick.tick();
    }
}
