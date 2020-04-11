package zen.proverbs;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class TestCounts {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void getProverbCount() {
        final String ret = client.toBlocking()
                .retrieve(HttpRequest.GET("/zen/count"));
        assertEquals("20", ret);
    }

    @Test
    public void getDefaultProverbCount() {
        final String ret = client.toBlocking()
                .retrieve(HttpRequest.GET("/zen/count/default"));
        assertEquals("20", ret);
    }

    @Test
    public void getCustomProverbCount() {
        final String ret = client.toBlocking()
                .retrieve(HttpRequest.GET("/zen/count/custom"));
        assertEquals("0", ret);
    }


}
