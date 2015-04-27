package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.Registration;
import com.codenjoy.dojo.services.mail.MailService;
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
    @Autowired private MailService mailService;

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

    @RequestMapping(params = "approve_email", method = RequestMethod.GET)
    public String approveEmail(@RequestParam("approve_email") String code) {
        String email = registration.getEmail(code);
        if (email != null) {
            registration.approve(code);
            return "redirect:board/" + email + "?code=" + code;
        } else {
            return "redirect:register";
        }
    }

    @RequestMapping(params = "approved", method = RequestMethod.GET)
    public String isApprovedEmail(@RequestParam("approved") String email) throws InterruptedException {
        while (!registration.approved(email)) {
            Thread.sleep(2000);
        }
        return "ok";
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
        boolean registered = registration.registered(player.getName());
        boolean approved = registration.approved(player.getName());
        if (registered && approved) {
            code = registration.login(player.getName(), player.getPassword());
            if (code == null) {
                model.addAttribute("bad_pass", true);

                return openRegistrationForm(request, model);
            }
        } else {
            if (!registered) {
                code = registration.register(player.getName(), player.getPassword());
            } else {
                code = registration.getCode(player.getName());
            }

            if (!approved) {
                String email = player.getName();
                String host = WebSocketRunner.Host.REMOTE.host;
                String link = "http://" + host + "/codenjoy-contest/register?approve_email=" + code;
                mailService.sendEmail(email, "Codenjoy регистрация",
                        "Пожалуйста, подтверди регистрацию кликом на этот линк<br>" +
                                "<a target=\"_blank\" href=\"" + link + "\">" + link + "</a><br>" +
                                "Он направит тебя к игре.<br>" +
                                "<br>" +
                                "Если тебя удивило это письмо, просто удали его.<br>" +
                                "<br>" +
                                "<a href=\"http://codenjoy.com\">Команда Codenjoy</a>");
            }
        }

        if (approved) {
            player = playerService.register(player.getName(), request.getRemoteAddr(), player.getGameName());

            if (player.getGameType().isSingleBoard()) {
                return "redirect:/board/" + player.getName() + "?code=" + code;
            } else {
                return "redirect:/board/?code=" + code;
            }
        } else {
            model.addAttribute("wait_approve", true);
            return openRegistrationForm(request, model);
        }
    }
}
