package net.tetris.web.controller;

import net.tetris.services.Player;
import net.tetris.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(Model model) {
        model.addAttribute("player", new Player());
        return "register";
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
