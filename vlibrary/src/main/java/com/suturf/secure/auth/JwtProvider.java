package com.suturf.secure.auth;

import com.suturf.secure.repo.LoginRepo;
import io.micronaut.http.HttpRequest;
import io.reactivex.Flowable;
import io.micronaut.security.authentication.*;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides OAuth 2 Authentication
 */
public class JwtProvider implements AuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(JwtProvider.class);

    @Inject
    LoginRepo lrp;

    /**
     * Authentication Override
     *
     * @param ar Authentication Request Object
     * @return Authentication Response
     */
    @Override
    public Publisher<AuthenticationResponse> authenticate(
            final AuthenticationRequest ar) {

        final String usr = (String)ar.getIdentity();
        final String pwd = (String)ar.getSecret();
        LOG.info("User: {}", usr);

        try {
            if (lrp.validateUser(usr, pwd)) {
                final List<String> roles = Arrays.asList("Administrator");
                return Flowable.just(new UserDetails((String) ar.getIdentity(), roles));
            }
        } catch(SQLException e) {
            LOG.error("JwtProvider::authenticate - Did you run /dbinit?");
        }

        return Flowable.just(new AuthenticationFailed());
    }

    /**
     * Authentication Override
     *
     * @param request Http Request object
     * @param ar Authentication Request Object
     * @return Authentication Response
     */
    @Override
    public Publisher<AuthenticationResponse> authenticate(
            final HttpRequest<?> request,
            final AuthenticationRequest<?, ?> ar) {

        return authenticate(ar);
    }
}
