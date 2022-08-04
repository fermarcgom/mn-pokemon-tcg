package io.github.fermarcgom.auth;

import io.github.fermarcgom.persistence.dao.UserRepository;
import io.github.fermarcgom.persistence.domain.User;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationProviderTest {

    @Mock
    private UserRepository userRepository;

    private AuthenticationProvider authenticationProvider;

    @BeforeEach
    void setup() {
        User databaseUser = new User();
        databaseUser.setEmail("user@test.com");
        databaseUser.setPassword("password");
        Mockito.when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(databaseUser));
        authenticationProvider = new AuthenticationProvider(userRepository);
    }

    @Test
    void whenPasswordIsCorrectThenAuthenticationIsSuccessful() {
        TestSubscriber<AuthenticationResponse> subscriber = new TestSubscriber<>();

        authenticationProvider.authenticate(null, new UsernamePasswordCredentials("user@test.com", "password"))
                .subscribe(subscriber);

        subscriber.assertComplete();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        AuthenticationResponse response = subscriber.values().get(0);
        assertThat(response.isAuthenticated()).isTrue();
        Authentication authentication = response.getAuthentication().orElse(null);
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@test.com");
        assertThat(authentication.getRoles()).containsExactly("ADMIN");
        assertThat(authentication.getAttributes().get("language")).isEqualTo("en");
    }

    @Test
    void whenPasswordIsIncorrectThenAuthenticationIsFailing() {
        TestSubscriber<AuthenticationResponse> subscriber = new TestSubscriber<>();

        authenticationProvider.authenticate(null, new UsernamePasswordCredentials("user@test.com", "wrong"))
                .subscribe(subscriber);

        subscriber.assertNotComplete();
        subscriber.assertError((exception) -> "Wrong username or password".equals(exception.getMessage()));
    }

    @Test
    void whenUserDoesNotExistThenAuthenticationIsFailing() {
        TestSubscriber<AuthenticationResponse> subscriber = new TestSubscriber<>();
        Mockito.when(userRepository.findByEmail("user2@test.com"))
                .thenReturn(Optional.empty());

        authenticationProvider.authenticate(null, new UsernamePasswordCredentials("user2@test.com", "wrong"))
                .subscribe(subscriber);

        subscriber.assertNotComplete();
        subscriber.assertError((exception) -> "Wrong username or password".equals(exception.getMessage()));
    }
}
