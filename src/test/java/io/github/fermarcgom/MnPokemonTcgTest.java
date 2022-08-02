package io.github.fermarcgom;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@MicronautTest
class MnPokemonTcgTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testApplicationWorks() {
        Assertions.assertTrue(application.isRunning());
    }
}
