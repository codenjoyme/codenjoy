package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 1:37 PM
 */
@Controller
@RequestMapping("/admin31415")
public class AdminController {

    @Autowired
    private TimerService timerService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private GameService gameService;

    public AdminController() {
    }

    //for unit test
    AdminController(TimerService timerService, PlayerService playerService) {
        this.timerService = timerService;
        this.playerService = playerService;
    }

    @RequestMapping(params = "save", method = RequestMethod.GET)
    public String savePlayerGame(@RequestParam("save") String name, Model model) {
        saveService.save(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "saveAll", method = RequestMethod.GET)
    public String saveAllGames(Model model) {
        saveService.saveAll();
        return getAdminPage(model);
    }

    @RequestMapping(params = "load", method = RequestMethod.GET)
    public String loadPlayerGame(@RequestParam("load") String name, Model model) {
        saveService.load(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "loadAll", method = RequestMethod.GET)
    public String loadAllGames(Model model) {
        saveService.loadAll();
        return getAdminPage(model);
    }

    @RequestMapping(params = "gameOver", method = RequestMethod.GET)
    public String removePlayer(@RequestParam("gameOver") String name, Model model) {
        playerService.remove(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "removeSave", method = RequestMethod.GET)
    public String removePlayerSave(@RequestParam("removeSave") String name, Model model) {
        saveService.removeSave(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "removeSaveAll", method = RequestMethod.GET)
    public String removePlayerSave(Model model) {
        saveService.removeAllSaves();
        return getAdminPage(model);
    }

    @RequestMapping(params = "gameOverAll", method = RequestMethod.GET)
    public String gameOverAllPlayers(Model model) {
        playerService.removeAll();
        return getAdminPage(model);
    }

    @RequestMapping(params = "pause", method = RequestMethod.GET)
    public String pauseGame(Model model) {
        timerService.pause();
        return getAdminPage(model);
    }

    private void checkGameStatus(Model model) {
        model.addAttribute("paused", timerService.isPaused());
    }

    @RequestMapping(params = "resume", method = RequestMethod.GET)
    public String resumeGame(Model model) {
        timerService.resume();
        return getAdminPage(model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveSettings(AdminSettings settings, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            // do nothing
        }
        playerService.updateAll(settings.getPlayers());

        Settings gameSettings = gameService.getSelectedGame().getGameSettings();
        List<Parameter> parameters = (List)gameSettings.getParameters();
        for (int index = 0; index < parameters.size(); index ++) {
            parameters.get(index).update(settings.getParameters().get(index));
        }


        return getAdminPage(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminPage(Model model) {
        Settings gameSettings = gameService.getSelectedGame().getGameSettings();
        List<Parameter<?>> parameters = gameSettings.getParameters();

        AdminSettings settings = new AdminSettings();

        settings.setParameters(new LinkedList<String>());
        for (Parameter p : parameters) {
            settings.getParameters().add(p.getValue().toString());
        }

        model.addAttribute("adminSettings", settings);
        model.addAttribute("parameters", parameters);
        model.addAttribute("games", gameService.getGameNames());

        checkGameStatus(model);
        prepareList(model, settings);
        return "admin";
    }

    private void prepareList(Model model, AdminSettings settings) {
        List<PlayerInfo> players = saveService.getSaves();
        if (!players.isEmpty()) {
            model.addAttribute("players", players);
        }
        settings.setPlayers(players);
    }

    @RequestMapping(params = "cleanAll", method = RequestMethod.GET)
    public String cleanAllPlayersScores(Model model) {
        playerService.cleanAllScores();
        return getAdminPage(model);
    }

    @RequestMapping(params = "game", method = RequestMethod.GET)
    public String selectGame(@RequestParam("game") String game, Model model) {
        gameService.selectGame(game);
        return getAdminPage(model);
    }

}
