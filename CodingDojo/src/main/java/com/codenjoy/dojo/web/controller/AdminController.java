package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.PlayerInfo;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    public AdminController() {
    }

    //for unit test
    AdminController(TimerService timerService, PlayerService playerService) {
        this.timerService = timerService;
        this.playerService = playerService;
    }

    @RequestMapping(params = "save", method = RequestMethod.GET)
    public String savePlayerGame(@RequestParam("save") String name, Model model) {
        playerService.savePlayerGame(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "saveall", method = RequestMethod.GET)
    public String saveAllGames(Model model) {
        playerService.saveAllGames();
        return getAdminPage(model);
    }

    @RequestMapping(params = "load", method = RequestMethod.GET)
    public String loadPlayerGame(@RequestParam("load") String name, Model model) {
        playerService.loadPlayerGame(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "loadall", method = RequestMethod.GET)
    public String loadAllGames(Model model) {
        playerService.loadAllGames();
        return getAdminPage(model);
    }

    @RequestMapping(params = "remove", method = RequestMethod.GET)
    public String removePlayer(@RequestParam("remove") String name, Model model) {
        playerService.removePlayerByName(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "removeall", method = RequestMethod.GET)
    public String removeAllPlayers(Model model) {
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
        playerService.updatePlayers(settings.getPlayers());
        return getAdminPage(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminPage(Model model) {
        AdminSettings settings = new AdminSettings();
        model.addAttribute("adminSettings", settings);

        checkGameStatus(model);
        prepareList(model, settings);
        return "admin";
    }

    private void prepareList(Model model, AdminSettings settings) {
        List<PlayerInfo> players = playerService.getPlayersGames();
        if (!players.isEmpty()) {
            model.addAttribute("players", players);
        }
        settings.setPlayers(players);
    }

}
