package com.codenjoy.dojo.snake.web.controller;

import com.codenjoy.dojo.services.NullPlayer;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.snake.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 5/18/12
 * Time: 6:55 PM
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private PlayerService playerService;

    public RegistrationController() {
    }

    //for unit test
    RegistrationController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(HttpServletRequest request, Model model) {
        String ip = request.getRemoteAddr();

        Player playerByIp = playerService.findPlayerByIp(ip);
        if (playerByIp instanceof NullPlayer || "127.0.0.1".equals(ip)) {
            Player player = new Player();
            model.addAttribute("player", player);

            player.setCallbackUrl("http://" + ip + ":8888");

            return "register";
        }
        model.addAttribute("user", playerByIp.getName());
        model.addAttribute("url", playerByIp.getCallbackUrl());

        return "already_registered";
    }

    @RequestMapping(params = "remove_me", method = RequestMethod.GET)
    public String removeUserFromGame(HttpServletRequest request, Model model) {
        String ip = request.getRemoteAddr();
        playerService.removePlayer(ip);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitRegistrationForm(Player player, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        if (playerService.alreadyRegistered(player.getName())) {
            playerService.updatePlayer(player);
            return "redirect:/board/" + player.getName();
        }
        playerService.addNewPlayer(player.getName(), player.getCallbackUrl());
        return "redirect:/board/" + player.getName();
    }

}
