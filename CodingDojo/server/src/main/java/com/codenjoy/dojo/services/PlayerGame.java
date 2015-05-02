package com.codenjoy.dojo.services;

public class PlayerGame implements Tickable {

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
        if (this == NullPlayerGame.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

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
