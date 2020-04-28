package com.suturf.video.repo;

import com.opentok.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum SessionList {
    INSTANCE;

    Map<String, Session> sessions = new HashMap<>();

    public void addSession(final String room, final Session sb) {
        sessions.putIfAbsent(room, sb);
    }

    public Session getSessionInfo(final String rm) {
        if (sessions.containsKey(rm)) {
            return sessions.get(rm);
        }
        return null;
    }

    public void deleteSession(final String rm) {
        sessions.remove(rm);
    }

    public List<String> getRooms() {
        return sessions.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
