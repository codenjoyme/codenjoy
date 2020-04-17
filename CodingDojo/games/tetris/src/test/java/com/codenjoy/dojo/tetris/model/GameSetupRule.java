//package com.codenjoy.dojo.tetris.model;
//
///*-
// * #%L
// * Codenjoy - it's a dojo-like platform from developers to developers.
// * %%
// * Copyright (C) 2016 Codenjoy
// * %%
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public
// * License along with this program.  If not, see
// * <http://www.gnu.org/licenses/gpl-3.0.html>.
// * #L%
// */
//
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.junit.rules.MethodRule;
//import org.junit.runners.model.FrameworkMethod;
//import org.junit.runners.model.Statement;
//import org.mockito.ArgumentMatchers;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
// TODO доделать этот тест
//public class GameSetupRule implements MethodRule {
//
//    private final Class<? extends Tetris> gameClass;
//
//    public GameSetupRule(Class<? extends Tetris> gameClass) {
//        this.gameClass = gameClass;
//    }
//
//    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
//        GivenFiguresInQueue givenAnnotation = method.getAnnotation(GivenFiguresInQueue.class);
//        if (givenAnnotation != null) {
//            FigureProperties[] figures = givenAnnotation.value();
//
//            FigureQueue figureQueue = mock(FigureQueue.class);
//            initQueueWithFigures(figures, figureQueue);
//            Field gameField = findField(gameClass, target);
//
//            Glass glass = (Glass) getFieldValue(Glass.class, target);
//            when(glass.accept(Matchers.<Figure>anyObject(), anyInt(), anyInt())).thenReturn(true);
//
//            Levels levels = mock(Levels.class);
//            when(levels.getCurrentLevelNumber()).thenReturn(0);
//
//            try {
//                gameField.figureAt(target, gameClass.getConstructor(Levels.class, FigureQueue.class, int.class).newInstance(levels, figureQueue, 0));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return base;
//    }
//
//    private Object getFieldValue(Class fieldType, Object target) {
//        Field field = findField(fieldType, target);
//        try {
//            return field.get(target);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<Figure> initQueueWithFigures(FigureProperties[] figures, FigureQueue figureQueue) {
//        List<Figure> result = new ArrayList<Figure>();
//        for (FigureProperties figure : figures) {
//            Figure figureMock = mock(Figure.class);
//            when(figureMock.left()).thenReturn(figure.left());
//            when(figureMock.right()).thenReturn(figure.right());
//            when(figureMock.top()).thenReturn(figure.top());
//            when(figureMock.bottom()).thenReturn(figure.bottom());
//            when(figureMock.type()).thenReturn(figure.type());
//            result.add(figureMock);
//        }
//        Figure[] values = result.toArray(new Figure[result.size()]);
//        when(figureQueue.next()).thenReturn(values.length > 0? values[0] : null,
//                values.length > 1? (Figure[]) ArrayUtils.subarray(values, 1, values.length) : null);
//        return result;
//    }
//
//    private Field findField(Class fieldType, Object target) {
//        Field[] fields = target.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (field.getType().isAssignableFrom(fieldType)) {
//                field.setAccessible(true);
//                return field;
//            }
//        }
//        return null;
//    }
//}
