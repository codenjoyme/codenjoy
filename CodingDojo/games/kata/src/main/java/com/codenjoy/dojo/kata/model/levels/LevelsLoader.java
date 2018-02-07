package com.codenjoy.dojo.kata.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.kata.model.levels.algorithms.*;
import com.codenjoy.dojo.kata.model.levels.algorithms.finale.*;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;

import java.util.*;

/**
 * Created by indigo on 2017-03-04.
 */
public class LevelsLoader {

    private static final String DEFAULT_KATA_PROFILE = "default";

    private static final Map<String, List<Level>> ALGORITHMS = Collections.unmodifiableMap(
            new HashMap<String, List<Level>>() {{
                put(DEFAULT_KATA_PROFILE, getDefaultAlgorithms());
                put("finale", getFinaleAlgorithms());
            }}
    );

    private static List<Level> getFinaleAlgorithms() {
        return new LinkedList<Level>() {{
            add(new FinaleHelloWorldAlgorithm()); // 0
            add(new FinaleFizzBuzzAlgorithm()); // 1
            add(new FinaleSumSquareDifferenceAlgorithm()); // 2
            add(new FinaleSequence1Algorithm()); // 3
            add(new FibonacciNumbersAlgorithm()); // 4
            add(new FinalePrimeFactoryAlgorithm()); // 5
            add(new FinalePowerDigitSumAlgorithm()); // 6
            add(new FinaleFactorialAlgorithm()); // 7
            add(new FinaleReverseAddPalindromeAlgorithm()); // 8
            add(new FinaleSequence2Algorithm()); // 9
            add(new FinaleXthPrimeAlgorithm()); // 10
            add(new FinaleLongDivisionAlgorithm()); // 11
        }}; // TODO: add finale algorithms set here
    }

    // TODO На админке можно менять порядок задач местами для играющих, а это убрать
    private static List<Level> getDefaultAlgorithms() {
        return new LinkedList<Level>(){{
            add(new HelloWorldAlgorithm());
            add(new FizzBuzzAlgorithm());
            add(new SumSquareDifferenceAlgorithm());
            add(new Sequence1Algorithm());
            add(new FibonacciNumbersAlgorithm());
            add(new PrimeFactoryAlgorithm());
            add(new PowerDigitSumAlgorithm());
//            add(new MakeBricksAlgorithm());
            add(new FactorialAlgorithm());
            add(new ReverseAddPalindromeAlgorithm());
            add(new Sequence2Algorithm());
            add(new XthPrimeAlgorithm());
            add(new LongDivisionAlgorithm());
        }};
    }

    public static List<Level> getAlgorithms() {
        String kataProfile = System.getenv("KATA_PROFILE");
        List<Level> levels = ALGORITHMS.getOrDefault(kataProfile, getDefaultAlgorithms());
        System.out.println("===> Using Kata algorithms for profile: " + kataProfile);
        System.out.println("===> Levels: \n" + StringUtils.join(levels, "\n"));
        return levels;
    }

    public static List<Level> getAlgorithmsOrderedByComplexity() {
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
        classes.remove(WaitLevel.class);
        classes.remove(LevelsPoolImpl.class);
        classes.remove(AlgorithmLevelImpl.class);
        classes.remove(QuestionAnswerLevelImpl.class);
        for (Class aClass : classes.toArray(new Class[0])) {
            String name = aClass.getName();
            if (name.contains("Test$") || name.contains("TestLevel")) {
                classes.remove(aClass);
            }
        }

        return classes;
    }

    private static Collection<? extends Class<? extends Level>> findInPackage(String packageName) {
        return new Reflections(packageName).getSubTypesOf(Level.class);
    }
}
