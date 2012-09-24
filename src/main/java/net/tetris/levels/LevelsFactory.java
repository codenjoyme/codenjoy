package net.tetris.levels;

import net.tetris.dom.Levels;
import net.tetris.services.PlayerFigures;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.fest.reflect.core.Reflection.constructor;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 5:24 PM
 */
@Component
public class LevelsFactory {

    public Set<Class<? extends Levels>> getAllLevelsInPackage() {
        Reflections reflections = new Reflections(LevelsFactory.class.getPackage().getName());

        return reflections.getSubTypesOf(Levels.class);
    }

    public Levels getGameLevels(String levels, PlayerFigures queue) {
        String className = this.getClass().getPackage().getName() + '.' + levels;
        try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
            return (Levels)constructor().withParameterTypes(PlayerFigures.class).in(aClass).newInstance(queue);
        } catch (ClassNotFoundException e) {
            return throwRuntime(e);
        }
    }

    private Levels throwRuntime(Exception e) {
        throw new RuntimeException("Error during load game levels", e);
    }
}
