package com.globallogic.snake.web.controller;

import com.globallogic.snake.services.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    public AdminController() {
    }

    //for unit test
    AdminController(TimerService timerService) {
        this.timerService = timerService;
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

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminPage(Model model) {
        checkGameStatus(model);
        return "admin";
    }

}
