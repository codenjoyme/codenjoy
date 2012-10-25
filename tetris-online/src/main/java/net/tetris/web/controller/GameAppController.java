package net.tetris.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    public String gameAppUploaded(HttpServletRequest request, Model model) {
        String warFileName = (String) request.getAttribute("warFileName");

        model.addAttribute("warFileName", warFileName);
        return "uploaded";
    }
}
