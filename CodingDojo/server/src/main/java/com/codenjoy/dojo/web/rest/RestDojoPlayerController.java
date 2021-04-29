package com.codenjoy.dojo.web.rest;

import com.codenjoy.dojo.services.DojoPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RestDojoPlayerController {

    @Autowired
    private DojoPlayerService dojoPlayerService;

    @PostMapping("/change/{username}/to/{newUsername}")
    public int updateGitHubUsername(@PathVariable("username") String username,
                                    @PathVariable("newUsername") String newUsername) {

        return dojoPlayerService.updateGitHubUsername(username, newUsername);
    }

    @PostMapping("/update/{username}/score")
    public void updateUserScore(@PathVariable("username") String username,
                                @RequestBody long score) {

        dojoPlayerService.updateUserScore(username, score);
    }
}
