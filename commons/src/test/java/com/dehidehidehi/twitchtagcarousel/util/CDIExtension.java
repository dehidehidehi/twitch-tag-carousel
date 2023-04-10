package com.dehidehidehi.twitchtagcarousel.util;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Enables CDI 2.0 in a JUnit5 test.<br>
 * Example usage: annotate a class with @ExtendWith({@link CDIExtension}).
 */
public class CDIExtension implements TestInstancePostProcessor {

	private static final SeContainer CONTAINER = SeContainerInitializer.newInstance().initialize();
	private static final Predicate<Annotation> IS_QUALIFIER = a -> a.annotationType().isAnnotationPresent(Qualifier.class);

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws IllegalAccessException {

		for (Field field : getFields(testInstance.getClass())) {
			if (field.getAnnotation(Inject.class) != null) {
				Annotation[] qualifiers = Stream.of(field.getAnnotations())
														  .filter(IS_QUALIFIER)
														  .toArray(Annotation[]::new);
				Object injected = CONTAINER.select(field.getType(), qualifiers).get();
				field.setAccessible(true);
				field.set(testInstance, injected);
			}
		}
	}

	private List<Field> getFields(Class<?> clazzInstance) {
		List<Field> fields = new ArrayList<>();
		if (!clazzInstance.getSuperclass().equals(Object.class)) {
			fields.addAll(getFields(clazzInstance.getSuperclass()));
		}
		fields.addAll(Arrays.asList(clazzInstance.getDeclaredFields()));
		return fields;
	}
}
