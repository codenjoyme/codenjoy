package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 5/18/12
 * Time: 6:55 PM
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private GameService gameService;

    public RegistrationController() {
    }

    //for unit test
    RegistrationController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(HttpServletRequest request, Model model) {
        String ip = getIp(request);

        Player player = new Player();
        player.setName(request.getParameter("name"));
        player.setGameName(request.getParameter(AdminController.GAME_NAME));
        model.addAttribute("player", player);

        player.setCallbackUrl("http://" + ip + ":8888");

        return getRegister(model);
    }

    private String getRegister(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gameNames", gameService.getGameNames());

        return "register";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    @RequestMapping(params = "remove_me", method = RequestMethod.GET)
    public String removeUserFromGame(@RequestParam("code") String code) {
        String name = registration.getEmail(code);
        Player player = playerService.get(name);
        playerService.remove(player.getName());
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitRegistrationForm(Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return openRegistrationForm(request, model);
        }

        String code = "";
        if (registration.registered(player.getName())) {
            code = registration.login(player.getName(), player.getPassword());
            if (code == null) {
                model.addAttribute("bad_pass", true);

                return openRegistrationForm(request, model);
            }
        } else {
            code = registration.register(player.getName(), player.getPassword());
        }

        player = playerService.register(player.getName(), request.getRemoteAddr(), player.getGameName());

        if (player.getGameType().isSingleBoard()) {
            return "redirect:/board/" + player.getName() + "?code=" + code;
        } else {
            return "redirect:/board/?code=" + code;
        }
    }
}
