package io.github.fermarcgom;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@MicronautTest
class ApplicationTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void whenApplicationStartsItWorks() {
        Assertions.assertThat(application.isRunning()).isTrue();
    }
}
