package com.codenjoy.dojo.kata.model.levels;

import org.reflections.Reflections;

import java.util.*;

/**
 * Created by indigo on 2017-03-04.
 */
public class LevelsLoader {

    public static List<Level> getAlgorithmsClasses() {
        List<Class<? extends Level>> classes = loadClasses();

        List<Level> result = createLevels(classes);

        sortByComplexity(result);

        return result;
    }

    private static void sortByComplexity(List<Level> result) {
        Collections.sort(result, new Comparator<Level>() {
            @Override
            public int compare(Level o1, Level o2) {
                return Integer.compare(o1.complexity(), o2.complexity());
            }
        });
    }

    private static List<Level> createLevels(List<Class<? extends Level>> classes) {
        List<Level> result = new LinkedList<>();
        for (Class<? extends Level> clazz : classes) {
            try {
                Level level = clazz.newInstance();
                result.add(level);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static List<Class<? extends Level>> loadClasses() {
        List<Class<? extends Level>> classes = new LinkedList<>();
        classes.addAll(findInPackage("com"));
        classes.addAll(findInPackage("org"));
        classes.addAll(findInPackage("net"));

        classes.remove(Level.class);
        classes.remove(NullLevel.class);
        classes.remove(LevelsPoolImpl.class);
        classes.remove(AlgorithmLevelImpl.class);
        classes.remove(QuestionAnswerLevelImpl.class);
        for (Class aClass : classes.toArray(new Class[0])) {
            if (aClass.getName().contains("Test$")) {
                classes.remove(aClass);
            }
        }

        return classes;
    }

    private static Collection<? extends Class<? extends Level>> findInPackage(String packageName) {
        return new Reflections(packageName).getSubTypesOf(Level.class);
    }
}
