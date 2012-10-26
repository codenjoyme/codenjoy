package net.tetris.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: serhiy.zelenin
 * Date: 10/26/12
 * Time: 2:00 PM
 */
@Controller
public class LeaderBoardController {
    @RequestMapping(value = "/board")
    public String board() {
        return "board";
    }
}
