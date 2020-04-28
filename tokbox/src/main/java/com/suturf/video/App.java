package com.suturf.video;

import com.suturf.video.repo.SessionBean;
import com.suturf.video.tokbox.TokboxSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.suturf.video.utils.JsonUtil.json;
import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    public static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static final String VER = "Video Chat Server v1.0";

    private static void enableCORS(final String origin,
                                   final String methods, final String headers) {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            response.type("application/json");
        });
    }

    public static void main(final String[] args) {

        enableCORS("*", "*", "*");

        get("/", (req, res) -> {
            return VER;
        });

        get("/room/:name", (req, res) -> {
            res.type("application/json");

            final TokboxSession sess = new TokboxSession();
            final SessionBean seb = sess.getSessionInfo(":name");
            return seb;
        }, json());
    }
}