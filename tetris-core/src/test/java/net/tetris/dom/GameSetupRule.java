package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.Figure;
import com.codenjoy.dojo.tetris.model.FigureQueue;
import org.apache.commons.lang.ArrayUtils;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Matchers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameSetupRule implements MethodRule {
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        GivenFiguresInQueue givenAnnotation = method.getAnnotation(GivenFiguresInQueue.class);
        if (givenAnnotation != null) {
            FigureProperties[] figures = givenAnnotation.value();

            FigureQueue figureQueue = mock(FigureQueue.class);
            initQueueWithFigures(figures, figureQueue);
            Field gameField = findField(TetrisAdvancedGame.class, target);
            
            Glass glass = (Glass) getFieldValue(Glass.class, target);
            when(glass.accept(Matchers.<Figure>anyObject(), anyInt(), anyInt())).thenReturn(true);

            try {
                gameField.set(target, new TetrisAdvancedGame(figureQueue, glass));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return base;
    }

    private Object getFieldValue(Class fieldType, Object target) {
        Field field = findField(fieldType, target);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Figure> initQueueWithFigures(FigureProperties[] figures, FigureQueue figureQueue) {
        List<Figure> result = new ArrayList<Figure>();
        for (FigureProperties figure : figures) {
            Figure figureMock = mock(Figure.class);
            when(figureMock.getLeft()).thenReturn(figure.left());
            when(figureMock.getRight()).thenReturn(figure.right());
            when(figureMock.getTop()).thenReturn(figure.top());
            when(figureMock.getBottom()).thenReturn(figure.bottom());
            when(figureMock.getType()).thenReturn(figure.type());
            result.add(figureMock);
        }
        Figure[] values = result.toArray(new Figure[result.size()]);
        when(figureQueue.next()).thenReturn(values.length > 0? values[0] : null,
                values.length > 1? (Figure[]) ArrayUtils.subarray(values, 1, values.length) : null);
        return result;
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
