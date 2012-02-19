package net.tetris.dom;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class GameSetupRule implements MethodRule {
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        GivenFiguresInQueue givenAnnotation = method.getAnnotation(GivenFiguresInQueue.class);
        if (givenAnnotation != null) {
            FigureProperties[] figures = givenAnnotation.value();
            FigureQueue figureQueue = getFigureQueue(target);
            if (figureQueue == null) {
                return base;
            }
            reset(figureQueue);
            initQueueWithFigures(figures, figureQueue);
            Field gameField = findField(TetrisGame.class, target);
            Field consoleField = findField(GameConsole.class, target);
            try {
                gameField.set(target, new TetrisGame((GameConsole) consoleField.get(target), figureQueue));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return base;
    }

    private List<Figure> initQueueWithFigures(FigureProperties[] figures, FigureQueue figureQueue) {
        List<Figure> result = new ArrayList<Figure>();
        for (FigureProperties figure : figures) {
            Figure figureMock = mock(Figure.class);
            when(figureMock.getLeft()).thenReturn(figure.left());
            when(figureMock.getRight()).thenReturn(figure.right());
            when(figureMock.getTop()).thenReturn(figure.top());
            when(figureMock.getBottom()).thenReturn(figure.bottom());
            when(figureQueue.next()).thenReturn(figureMock);
            result.add(figureMock);
        }
        return result;
    }

    private FigureQueue getFigureQueue(Object target) {
        Field field = findField(FigureQueue.class, target);
        if (field == null) {
            throw new RuntimeException(FigureQueue.class + " field not found");
        }
        try {
            return (FigureQueue) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field findField(Class fieldType, Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(fieldType)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }
}
