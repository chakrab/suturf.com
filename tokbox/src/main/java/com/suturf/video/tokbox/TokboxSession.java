package com.suturf.video.tokbox;

import com.opentok.OpenTok;
import com.opentok.Session;
import com.opentok.TokenOptions;
import com.opentok.exception.OpenTokException;
import com.suturf.video.repo.SessionBean;
import com.suturf.video.repo.SessionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokboxSession {

    private final static Logger LOG = LoggerFactory.getLogger(TokboxSession.class);

    private final static int    API_CODE   = 0;
    private final static String API_SECRET = "x!x!x!x";

    public SessionBean getSessionInfo(final String room) {
        // Do we already have this room?
        SessionBean sb = null;
        Session session = SessionList.INSTANCE.getSessionInfo(room);
        if (session == null) {
            // Call OpenTok to get a new one
            final OpenTok ot = new OpenTok(API_CODE, API_SECRET);
            try {
                // Create Session
                session = ot.createSession();
                final String  sessionId = session.getSessionId();
                final String  token = ot.generateToken(sessionId,
                    new TokenOptions.Builder()
                        .expireTime((System.currentTimeMillis() / 1000L) + (60*15))  // 15 minutes
                        .build()
                );

                sb = new SessionBean();
                sb.setKeyId(API_CODE);
                sb.setRoomName(room);
                sb.setSessionId(sessionId);
                sb.setTokenId(token);
                LOG.info("Generated Token: {} for session: {}", token, sessionId);

                // Store this
                SessionList.INSTANCE.addSession(room, session);
            } catch (OpenTokException e) {
                LOG.error("TokboxSession::getSessionInfo", e);
            } finally {
                ot.close();
            }
        } else {
            try {
                // Generate only the token
                sb = new SessionBean();
                final String  sessionId = session.getSessionId();
                final String  token = session.generateToken(
                    new TokenOptions.Builder()
                        .expireTime((System.currentTimeMillis() / 1000L) + (60*15))  // 15 minutes
                        .build()
                );

                sb = new SessionBean();
                sb.setKeyId(API_CODE);
                sb.setRoomName(room);
                sb.setSessionId(sessionId);
                sb.setTokenId(token);
                LOG.info("Generated Token: {} using session: {}", token, sessionId);
            } catch (OpenTokException e) {
                LOG.error("TokboxSession::getSessionInfo", e);
            }
        }

        return sb;
    }
}
