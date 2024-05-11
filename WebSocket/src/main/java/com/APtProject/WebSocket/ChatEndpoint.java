package com.APtProject.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ServerEndpoint("/chat/{documentId}")
@Component
public class ChatEndpoint {
    private static Map<String, Set<Session>> documentSessions = new HashMap<>();

    // private static final Set<Session> sessions = Collections.synchronizedSet(new
    // HashSet<>());
    private static ArrayList<AckBody> pastChanges = new ArrayList<>();
    private static Integer version = 0;

    @OnOpen
    public void onOpen(Session session, @PathParam("documentId") String documentId) {
        System.out.println("doc sessions " + documentSessions);
        // for (Map.Entry<String, Set<Session>> entry : documentSessions.entrySet()) {
        // String documentId2 = entry.getKey(); // Get the document ID (key) of the
        // current entry
        // Set<Session> sessions = entry.getValue(); // Get the sessions associated with
        // the current document ID
        // System.out.println("sessions " + sessions);
        // System.out.println("doc id get " + documentId.toString());
        // System.out.println("doc id in map " + documentId2.toString());
        // System.out.println("compare " +
        // documentId2.toString().equals(documentId.toString()));
        // }

        if (!documentSessions.containsKey(documentId)) {
            // If not present, add a new entry with an empty HashSet for sessions
            documentSessions.put(documentId, new HashSet<>());
            
            System.out.println("New document added with ID: " + documentId);
        } else {
            System.out.println("Document ID already exists: " + documentId);
        }
        Set<Session> sessions = documentSessions.get(documentId.toString());
        sessions.add(session);
        System.out.println("sessions " + sessions);
        System.out.println("New session opened for user: " + session + "oin doc id" +
        documentId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("documentId") String documentId) {
        Set<Session> sessions = documentSessions.get(documentId);
        sessions.remove(session);
        System.out.println("Session closed for user: " + session);
        pastChanges.clear();
        version = 0;
    }

    @OnMessage
    public synchronized void onMessage(String message, Session senderSession,
            @PathParam("documentId") String documentId)
            throws IOException {
        Gson g = new Gson();
        AckBody ackBody = g.fromJson(message, AckBody.class);
        System.out.println("DATA RECIEVED message to received to server:" + ackBody + " from:" + senderSession.getId());

        AckBody change = ackBody.clone();
        AckBody unchanged = ackBody.clone();
        if (change.getVersion() < version) {
            System.out.println("Old version received");
            for (AckBody pastchange : pastChanges) {
                // System.out.println("In for lopp");
                // System.out.println("pastchange.getVersion()" + pastchange.getVersion()+
                // "change.getVersion()" + change.getVersion());
                if (pastchange.getVersion() == change.getVersion()) {
                    System.out.println("Old version ");
                    Integer index = change.getIndex();
                    if (index >= pastchange.getIndex()) {
                        System.out.println("Old version index" + index);
                        index = index + 1;
                        System.out.println("new version index" + index);
                        change.setIndex(index);
                    }
                }
            }
        }
        Set<Session> sessions = documentSessions.get(documentId);
        for (Session session : sessions) {
            if (!session.getId().equals(senderSession.getId())) {
                change.setIsAck(false);
                String jsonMessage = g.toJson(change);
                session.getBasicRemote().sendText(jsonMessage);
                System.out.println("DATA SENT message to send to client CHANGE:" + change + " to:" + session.getId());
                System.out.println("DATA SENT message to send to client:" + jsonMessage + " to:" + session.getId());
            } else {
                unchanged.setIsAck(true);
                String jsonMessage = g.toJson(unchanged);
                session.getBasicRemote().sendText(jsonMessage);
                System.out.println(
                        "Ack SENT message to send to client: UNCHANGED" + unchanged + " to:" + session.getId());
                System.out.println("Ack SENT message to send to client:" + jsonMessage + " to:" + session.getId());
            }
        }

        ++version;
        pastChanges.add(change);
        System.out.println("Server's version" + version);
    }
}
