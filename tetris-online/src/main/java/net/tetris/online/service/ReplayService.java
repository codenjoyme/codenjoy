package net.tetris.online.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 6:17 PM
 */
@Service
public class ReplayService {
    @Autowired
    private ServiceConfiguration configuration;

    public void replay(String playerName, String timestamp) {
        GameLogFile gameLogFile = null;
        try {
            gameLogFile = new GameLogFile(configuration, playerName, timestamp);
            while (gameLogFile.readNextStep()) {
                //TODO:
//                gameLogFile.
            }
        } finally {
            if (gameLogFile != null) {
                gameLogFile.close();
            }
        }
    }
}
