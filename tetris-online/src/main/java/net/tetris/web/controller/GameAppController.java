package net.tetris.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: serhiy.zelenin
 * Date: 10/24/12
 * Time: 2:12 PM
 */
@Controller
public class GameAppController {

    @RequestMapping(value = "/")
    public String uploadGameApp() {
        return "upload";
    }

    @RequestMapping(value = "/uploaded")
    public String gameAppUploaded() {
        return "uploaded";
    }
}
