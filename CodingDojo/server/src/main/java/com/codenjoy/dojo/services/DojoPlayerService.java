package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DojoPlayerService {

    private final Registration registration;
    private final GameServerServiceImpl gameServerService;
    private final PlayerService playerService;
    private final UpdateHandler updateHandler;

    @Autowired
    public DojoPlayerService(Registration registration, GameServerServiceImpl gameServerService, PlayerService playerService, UpdateHandler updateHandler) {
        this.registration = registration;
        this.gameServerService = gameServerService;
        this.playerService = playerService;
        this.updateHandler = updateHandler;
    }

    public int updateGitHubUsername(String username, String newUsername) {

        String id = registration.getIdByGitHubUsername(username);
        if (id == null) {
            return 0;
        }
        String repository = gameServerService.createOrGetRepository(newUsername);

        playerService.updateUserRepository(id, repository);

        return registration.updateGitHubUsername(username, newUsername);
    }

    public void updateUserScore(String username, long score) {

        updateHandler.sendUpdate(username, score);
        playerService.updateScore(username, score);
    }
}
