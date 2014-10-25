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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired private TimerService timerService;
    @Autowired private PlayerService playerService;
    @Autowired private SaveService saveService;
    @Autowired private GameService gameService;

    public AdminController() {
    }

    //for unit test
    AdminController(TimerService timerService, PlayerService playerService) {
        this.timerService = timerService;
        this.playerService = playerService;
    }

    @RequestMapping(params = "save", method = RequestMethod.GET)
    public String savePlayerGame(@RequestParam("save") String name, Model model, HttpServletRequest request) {
        saveService.save(name);
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "saveAll", method = RequestMethod.GET)
    public String saveAllGames(Model model, HttpServletRequest request) {
        saveService.saveAll();
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "load", method = RequestMethod.GET)
    public String loadPlayerGame(@RequestParam("load") String name, Model model, HttpServletRequest request) {
        saveService.load(name);
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "loadAll", method = RequestMethod.GET)
    public String loadAllGames(Model model, HttpServletRequest request) {
        saveService.loadAll();
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "gameOver", method = RequestMethod.GET)
    public String removePlayer(@RequestParam("gameOver") String name, Model model, HttpServletRequest request) {
        playerService.remove(name);
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "removeSave", method = RequestMethod.GET)
    public String removePlayerSave(@RequestParam("removeSave") String name, Model model, HttpServletRequest request) {
        saveService.removeSave(name);
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "removeSaveAll", method = RequestMethod.GET)
    public String removePlayerSave(Model model, HttpServletRequest request) {
        saveService.removeAllSaves();
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "gameOverAll", method = RequestMethod.GET)
    public String gameOverAllPlayers(Model model, HttpServletRequest request) {
        playerService.removeAll();
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "pause", method = RequestMethod.GET)
    public String pauseGame(Model model, HttpServletRequest request) {
        timerService.pause();
        return getAdminPage(model, request);
    }

    private void checkGameStatus(Model model) {
        model.addAttribute("paused", timerService.isPaused());
    }

    @RequestMapping(params = "resume", method = RequestMethod.GET)
    public String resumeGame(Model model, HttpServletRequest request) {
        timerService.resume();
        return getAdminPage(model, request);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveSettings(AdminSettings settings, BindingResult result, Model model, HttpServletRequest request) {
        if (!result.hasErrors()) {
            // do nothing
        }
        if (settings.getPlayers() != null) {
            playerService.updateAll(settings.getPlayers());
        }

        if (settings.getParameters() != null) {
            Settings gameSettings = gameService.getGame(settings.getGameName()).getGameSettings();
            List<Parameter> parameters = (List) gameSettings.getParameters();
            for (int index = 0; index < parameters.size(); index++) {
                parameters.get(index).update(settings.getParameters().get(index));
            }
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = Integer.valueOf(settings.getGenerateCount());

            int created = 0;
            int index = 0;
            while (created != count) {
                String name = mask.replaceAll("%", String.valueOf(++index));

                if (playerService.contains(name) && index < playerService.getAll().size()) {
                    continue;
                }

                created++;
                playerService.register(name, "", "127.0.0.1", settings.getGameName());
            }
        }

        request.setAttribute("gameName", settings.getGameName());
        return getAdminPage(model, request);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminPage(Model model, HttpServletRequest request) {
        String gameName = getGameName(request);
        
        if (gameName == null) {
            return "redirect:/admin31415?gameName=sample";
        }

        Settings gameSettings = gameService.getGame(gameName).getGameSettings();
        List<Parameter<?>> parameters = gameSettings.getParameters();

        AdminSettings settings = new AdminSettings();

        settings.setParameters(new LinkedList<String>());
        for (Parameter p : parameters) {
            settings.getParameters().add(p.getValue().toString());
        }

        model.addAttribute("adminSettings", settings);
        model.addAttribute("parameters", parameters);
        model.addAttribute("games", gameService.getGameNames());
        model.addAttribute("gameName", gameName);
        model.addAttribute("generateNameMask", "apofig%");
        model.addAttribute("generateCount", "30");

        checkGameStatus(model);
        checkRegistrationClosed(model);
        prepareList(model, settings);
        return "admin";
    }

    private String getGameName(HttpServletRequest request) {
        String gameName = request.getParameter("gameName");
        if (gameName == null) {
            gameName = (String) request.getAttribute("gameName");
        }
        return gameName;
    }

    private void prepareList(Model model, AdminSettings settings) {
        List<PlayerInfo> players = saveService.getSaves();
        if (!players.isEmpty()) {
            model.addAttribute("players", players);
        }
        settings.setPlayers(players);
    }

    @RequestMapping(params = "cleanAll", method = RequestMethod.GET)
    public String cleanAllPlayersScores(Model model, HttpServletRequest request) {
        playerService.cleanAllScores();
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "game", method = RequestMethod.GET)
    public String selectGame(HttpServletRequest request, Model model, @RequestParam("game") String gameName) {
        request.setAttribute("gameName", gameName);
        return getAdminPage(model, request);
    }

    @RequestMapping(params = "close", method = RequestMethod.GET)
    public String close(Model model, HttpServletRequest request) {
        playerService.closeRegistration();
        return getAdminPage(model, request);
    }

    private void checkRegistrationClosed(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
    }

    @RequestMapping(params = "open", method = RequestMethod.GET)
    public String open(Model model, HttpServletRequest request) {
        playerService.openRegistration();
        return getAdminPage(model, request);
    }

}
