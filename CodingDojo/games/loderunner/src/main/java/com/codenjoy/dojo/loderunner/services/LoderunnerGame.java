package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.loderunner.client.ai.ApofigDirectionSolver;
import com.codenjoy.dojo.loderunner.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * User: oleksandr.baglai
 * Date: 8/17/13
 * Time: 7:47 PM
 */
public class LoderunnerGame implements GameType {

    public final static boolean SINGLE = true;
    private final Settings settings;
    private final Level level;
    private Loderunner loderunner;

    public LoderunnerGame() {
        settings = new SettingsImpl();
        new LoderunnerPlayerScores(0, settings);  // TODO сеттринги разделены по разным классам, продумать архитектуру
        level = new LevelImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼          $      »           ~~~~~~~~~           ~~~~~~~☼" +
                "☼##H########################H#H $     H##########H       ☼" +
                "☼  H$              $        H######H  H     $    H#☼☼☼☼☼#☼" +
                "☼H☼☼#☼☼H    H#########H $   H#     H#####H#####H##  ~~~~~☼" +
                "☼H $   H    H         H#####H#  $  H ~   H     H  ~~     ☼" +
                "☼H#☼#☼#H    H         H  ~~~ #####H#     H     H    ~~   ☼" +
                "☼H  ~  H~~~~H~~~~~~   H           H   H######H##      ~~$☼" +
                "☼H     H    H  $  H###☼☼☼☼☼☼H☼    H~~~H      H          #☼" +
                "☼H     H $  H#####H         H     H      H#########H     ☼" +
                "☼☼###☼##☼##☼H         H###H##    H##     H#   $   ##     ☼" +
                "☼☼###☼~~~~  H         H   H######H######### H###H #####H#☼" +
                "☼☼$  ☼ »    H   ~~~~~~H   H $    H          H#$#H   $  H ☼" +
                "☼########H###☼☼☼☼     H  ############   ###### ##########☼" +
                "☼        H     $      H        $             $           ☼" +
                "☼H##########################H########~~~####H############☼" +
                "☼H   $             ~~~      H               H    $       ☼" +
                "☼#######H#######            H###~~~~      ############H  ☼" +
                "☼       H~~~~~~~~~~     $   H     »                   H  ☼" +
                "☼       H    ##H   #######H##########~~~~~~~H######## H  ☼" +
                "☼       H    ##H          H                 H   $$$$  H  ☼" +
                "☼##H#####    ########H#######~~~~  ~~~#########~~~~~  H  ☼" +
                "☼  H         $       H            $               ~~~~H  ☼" +
                "☼#########H##########H        #☼☼☼☼☼☼#   ☼☼☼☼☼☼☼      H  ☼" +
                "☼         H          H        ~      ~   $    $       H  ☼" +
                "☼☼☼     $ H~~~~~~~~~~H   $     ######   ###########   H  ☼" +
                "☼    H######         #######H     $     ~~~~~~~~~~~~~~H  ☼" +
                "☼H☼  H  ◄        $          H  H####H        $        H  ☼" +
                "☼H☼☼#☼☼☼☼☼☼☼☼☼☼☼☼###☼☼☼☼☼☼☼☼H☼☼☼☼☼☼☼☼#☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼#☼" +
                "☼H       $    ~~H~~~~☼☼☼☼☼☼☼H☼☼☼☼☼☼☼    $  H   ~~~~~~~~~H☼" +
                "☼H~~~~  ######  H    $    H☼H☼H $      ####H  ☼  $$$    H☼" +
                "☼H      $       ##H#######H☼H☼H######H     ###☼☼☼☼☼☼☼☼ ~H☼" +
                "☼H#########     $ H    ~~~H☼H☼H~~~$  H~~~~~ ##  $$$   ~ H☼" +
                "☼H        ###H####H##H     ☼H☼       H     ###☼☼☼☼☼☼ ~  H☼" +
                "☼H           H      #######☼H☼#####  H#####   ~~~~~~~ ~ H☼" +
                "☼~~~~~~~~~~~~H       H~~~~~☼H☼~~~~~  H    $        ~ ~  H☼" +
                "☼     H    »  $      H     ☼H☼     ##########H          H☼" +
                "☼ ### #############H H#####☼H☼    $          H ######## H☼" +
                "☼H                 H       ☼H☼#######        H   $      H☼" +
                "☼H#####    $    H##H####      $         ###H#########   H☼" +
                "☼H      H######### H   ############        H    $       H☼" +
                "☼H##    H  $       H~~~~~~$     $          H #######H## H☼" +
                "☼~~~~#####H#   ~~~~H         ########H     H        H   H☼" +
                "☼         H        H  $   ~~~~~~~~   H     H        H   H☼" +
                "☼   ########H    ######H##        ##############    H   H☼" +
                "☼$          H  $       H       $~~~~~           ##H#####H☼" +
                "☼H    ###########H     H#####H         H##H       H     H☼" +
                "☼H###            H     H     ###########  ##H###  H     H☼" +
                "☼H  ######  ##H######  H  $            $    H   ##H###  H☼" +
                "☼H     $      H ~~~~~##H###H »   #########H##           H☼" +
                "☼    H########H#       H   ######         H  $          H☼" +
                "☼ ###H  $     H         ~~~~~H      ##H###H####H###     H☼" +
                "☼    H########H#########     H        H        H        H☼" +
                "☼H   H      $                H        H        H  $     H☼" +
                "☼H  ####H######         #####H########H##      H#####   H☼" +
                "☼H      H      H#######H         $             H        H☼" +
                "☼##############H   $   H#################################☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    private Loderunner newGame() {
        return new Loderunner(level, new RandomDice());
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new LoderunnerPlayerScores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory) {
        if (!SINGLE || loderunner == null) {
            loderunner = newGame();
        }

        Game game = new SingleLoderunner(loderunner, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "loderunner";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isSingleBoard() {
        return SINGLE;
    }

    @Override
    public void newAI(String aiName) {
        ApofigDirectionSolver.start(aiName, WebSocketRunner.Host.REMOTE);
    }
}
