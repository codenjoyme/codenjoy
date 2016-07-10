package net.tetris.services;

import net.tetris.dom.Levels;
import net.tetris.services.levels.LevelsFactory;
import net.tetris.services.levels.ProbabilityWithoutOverflownLevels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
  * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 5:11 PM
  */
@Component
public class GameSettingsService implements GameSettings {

    private String levelSettings;

    @Autowired
    private LevelsFactory levels;
    private String protocol = TetrisPlayerService.WS_PROTOCOL;

    public GameSettingsService() {
        levelSettings = ProbabilityWithoutOverflownLevels.class.getSimpleName();
    }

    @Override
    public void setGameLevels(String levelSettings) {
        this.levelSettings = levelSettings;
    }

    @Override
    public String getCurrentGameLevels() {
        return levelSettings;
    }

    @Override
    public List<String> getGameLevelsList() {
        Set<Class<? extends Levels>> levelsInPackage = levels.getAllLevelsInPackage();
        List<String> result = new LinkedList<>();
        for (Class<? extends Levels> clazz : levelsInPackage) {
            result.add(clazz.getSimpleName());
        }
        return result;
    }

    @Override
    public String getCurentProtocol() {
        return protocol;
    }

    @Override
    public List<String> getProtocols() {
        return Arrays.asList(TetrisPlayerService.HTTP_PROTOCOL,
                TetrisPlayerService.WS_PROTOCOL);
    }

    @Override
    public void setCurrentProtocol(String protocol) {
        this.protocol = protocol;
    }
}
