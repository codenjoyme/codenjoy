package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
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
        String ip = getIp(request);

        Player player = new Player();
        player.setName(request.getParameter("name"));
        model.addAttribute("player", player);

        player.setCallbackUrl("http://" + ip + ":8888");

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
        String playerName = playerService.getByCode(code);
        playerService.remove(playerName);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitRegistrationForm(Player player, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "register";
        }

        if (playerService.contains(player.getName())) {
            if (!playerService.login(player.getName(), player.getPassword())) {
                request.setAttribute("bad_pass", true);
                return "register";
            }
        }

        player = playerService.register(player.getName(), player.getPassword(), request.getRemoteAddr());
        return "redirect:/board/" + player.getName() + "?code=" + player.getCode();
    }
}
