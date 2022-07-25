package io.github.fermarcgom.auth;

import io.github.fermarcgom.persistence.dao.UserRepository;
import io.github.fermarcgom.persistence.domain.User;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class JDBCAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCAuthenticationProvider.class);
    private final UserRepository userRepository;

    public JDBCAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(
            @Nullable HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final String identity = (String) authenticationRequest.getIdentity();
            LOG.debug("User {} tries to login...", identity);

            Optional<User> user = userRepository.findByEmail(identity);

            user.ifPresentOrElse(
                    (userFound -> {
                        LOG.debug("Found user: {}", userFound.getEmail());
                        final String secret = (String) authenticationRequest.getSecret();
                        if (userFound.getPassword().equals(secret)) {
                            LOG.debug("User logged in");
                            HashMap<String, Object> attributes  = new HashMap<>();
                            attributes.put("language", "en");
                            emitter.onNext(AuthenticationResponse.success(identity, Collections.singleton("ADMIN"), attributes));
                            emitter.onComplete();
                        } else {
                            LOG.debug("Wrong password provider for user {}", userFound.getEmail());
                            emitter.onError(AuthenticationResponse.exception("Wrong username or password"));
                        }
                    }),
                    () -> {
                        LOG.debug("No user found with email {}", identity);
                        emitter.onError(AuthenticationResponse.exception("Wrong username or password"));
                    }
            );
        }, BackpressureStrategy.ERROR);
    }
}
