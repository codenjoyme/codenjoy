package net.tetris.web.controller;

import net.tetris.services.*;
import net.tetris.services.PlayerInfo;
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
    private GameSettings gameSettingsService;

    @Autowired
    private PlayerService playerService;

    public AdminController() {
    }

    //for unit test
    AdminController(TimerService timerService, GameSettingsService gameSettingsService, PlayerService playerService) {
        this.timerService = timerService;
        this.gameSettingsService = gameSettingsService;
        this.playerService = playerService;
    }

    @RequestMapping(params = "save", method = RequestMethod.GET)
    public String savePlayerGame(@RequestParam("save") String name, Model model) {
        playerService.savePlayerGame(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "load", method = RequestMethod.GET)
    public String loadPlayerGame(@RequestParam("load") String name, Model model) {
        playerService.loadPlayerGame(name);
        return getAdminPage(model);
    }

    @RequestMapping(params = "remove", method = RequestMethod.GET)
    public String removePlayer(@RequestParam("remove") String name, Model model) {
        playerService.removePlayerByName(name);
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
            gameSettingsService.setGameLevels(settings.getSelectedLevels());
            gameSettingsService.setCurrentProtocol(settings.getSelectedProtocol());
        }
        playerService.updatePlayers(settings.getPlayers());
        return getAdminPage(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminPage(Model model) {
        AdminSettings settings = new AdminSettings();
        model.addAttribute("adminSettings", settings);

        checkGameStatus(model);
        buildGameLevelsOptions(model, settings);
        buildProtocolsOptions(model, settings);
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

    private void buildGameLevelsOptions(Model model, AdminSettings settings) {
        List<String> list = gameSettingsService.getGameLevelsList();
        model.addAttribute("levelsList", list);

        settings.setSelectedLevels(gameSettingsService.getCurrentGameLevels());
    }

    private void buildProtocolsOptions(Model model, AdminSettings settings) {
        List<String> list = gameSettingsService.getProtocols();
        model.addAttribute("protocolsList", list);

        settings.setSelectedProtocol(gameSettingsService.getCurentProtocol());
    }

}
