package com.mika.WineApp.it;

import org.junit.jupiter.api.TestInstance;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public @interface IntegrationTestRead {
}
