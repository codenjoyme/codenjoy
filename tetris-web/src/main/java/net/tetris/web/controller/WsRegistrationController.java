package net.tetris.web.controller;

import net.tetris.services.NullPlayer;
import net.tetris.services.Player;
import net.tetris.services.TetrisPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 5/18/12
 * Time: 6:55 PM
 */
@Controller
@RequestMapping("/wsregister")
public class WsRegistrationController {
    @Autowired
    private TetrisPlayerService playerService;

    public WsRegistrationController() {
    }

    //for unit test
    WsRegistrationController(TetrisPlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(HttpServletRequest request, Model model) {
        String ip = getIp(request);

        Player playerByIp = playerService.findPlayerByIp(ip);
        if (playerByIp instanceof NullPlayer) {
            Player player = new Player();
            model.addAttribute("player", player);
            return "wsregister";
        }
        model.addAttribute("user", playerByIp.getName());

        return "already_registered";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    @RequestMapping(params = "remove_me", method = RequestMethod.GET)
    public String removeUserFromGame(HttpServletRequest request, Model model) {
        String ip = request.getRemoteAddr();
        playerService.removePlayerByIp(ip);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitRegistrationForm(Player player, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "register";
        }
        if (playerService.alreadyRegistered(player.getName())) {
            playerService.updatePlayer(player);
            return "redirect:/board/" + player.getName();
        }
        playerService.addNewPlayer(player.getName(), request.getRemoteAddr(), TetrisPlayerService.WS_PROTOCOL);

//        return "redirect:/board/" + player.getName();
        return "redirect:/wsregister/" + player.getName();
    }

    @RequestMapping(value = "{playerName}", method = RequestMethod.GET)
    public String board(@PathVariable("playerName") String playerName, HttpServletRequest request) {
        request.setAttribute("user", playerName);
        return "wsjsclient";
    }
}
