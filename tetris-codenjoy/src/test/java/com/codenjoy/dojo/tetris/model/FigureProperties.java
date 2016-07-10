package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.Figure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FigureProperties {

    int left() default 0;

    int right() default 0;

    int top() default 0;

    int bottom() default 0;

    Figure.Type type() default Figure.Type.I;
}
