package com.codenjoy.dojo.services;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StateUtils {
	public static <T extends Point> List<T> filter(Object[] array, Class<T> clazz) {
		return (List) Arrays.stream(array)
				.filter(it -> it != null)
				.filter(it -> it.getClass().equals(clazz))
				.collect(toList());
	}

	public static <T extends Point> T filterOne(Object[] array, Class<T> clazz) {
		return (T) Arrays.stream(array)
				.filter(it -> it != null)
				.filter(it -> it.getClass().equals(clazz))
				.findFirst()
				.orElse(null);
	}
}
