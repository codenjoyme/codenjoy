package net.tetris.services;

import net.tetris.dom.FigureQueue;
import net.tetris.dom.Levels;
import net.tetris.services.levels.LevelsFactory;
import net.tetris.services.levels.ProbabilityLevels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public GameSettingsService() {
        levelSettings = ProbabilityLevels.class.getSimpleName();
    }

    @Override
    public Levels getGameLevels(FigureQueue playerQueue) {
        return levels.getGameLevels(playerQueue, levelSettings);
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
}
