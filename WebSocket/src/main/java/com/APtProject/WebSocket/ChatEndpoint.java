package com.APtProject.WebSocket;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ServerEndpoint("/chat/{documentId}")
@Component
public class ChatEndpoint {
    private static Map<String, Set<Session>> documentSessions = new HashMap<>();
    private static Map<String, Integer> documentVersions = new HashMap<>();
    private static Map<String, ArrayList<AckBody>> documentPastChanges = new HashMap<>();

    // private static final Set<Session> sessions = Collections.synchronizedSet(new
    // HashSet<>());
    // private static ArrayList<AckBody> pastChanges = new ArrayList<>();
    // private static Integer version = 0;

    @OnOpen
    public void onOpen(Session session, @PathParam("documentId") String documentId) {
        System.out.println("doc sessions " + documentSessions);

        if (!documentSessions.containsKey(documentId)) {
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
        if (documentVersions.containsKey(documentId)) {
            AckBody ackBody = new AckBody();
            ackBody.setIsVersion(true);
            ackBody.setVersion(documentVersions.get(documentId));
            Gson g = new Gson();    
            String jsonMessage = g.toJson(ackBody);
            try {
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            documentPastChanges.put(documentId, new ArrayList<>());
            documentVersions.put(documentId, 0);
            AckBody ackBody = new AckBody();
            ackBody.setIsVersion(true);
            ackBody.setVersion(documentVersions.get(documentId));
            Gson g = new Gson();    
            String jsonMessage = g.toJson(ackBody);
            try {
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("documentId") String documentId) {
        Set<Session> sessions = documentSessions.get(documentId);
        sessions.remove(session);
        System.out.println("Session closed for user: " + session);
        if (sessions.isEmpty()) {
            documentSessions.remove(documentId);
            documentVersions.remove(documentId);
            documentPastChanges.remove(documentId);
        }
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
        System.out.println("Da el document version" + documentVersions.get(documentId)+ " Da el change version" + change.getVersion());
        if (change.getVersion() < documentVersions.get(documentId)) {
            System.out.println("Old version received");
            for (AckBody pastchange : documentPastChanges.get(documentId)) {
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
                change.setIsVersion(false);
                String jsonMessage = g.toJson(change);
                session.getBasicRemote().sendText(jsonMessage);
                System.out.println("DATA SENT message to send to client CHANGE:" + change + " to:" + session.getId());
                System.out.println("DATA SENT message to send to client:" + jsonMessage + " to:" + session.getId());
            } else {
                unchanged.setIsAck(true);
                unchanged.setIsVersion(false);
                String jsonMessage = g.toJson(unchanged);
                session.getBasicRemote().sendText(jsonMessage);
                System.out.println(
                        "Ack SENT message to send to client: UNCHANGED" + unchanged + " to:" + session.getId());
                System.out.println("Ack SENT message to send to client:" + jsonMessage + " to:" + session.getId());
            }
        }
        int version = documentVersions.get(documentId);
        version++;
        documentVersions.put(documentId, version);
        ArrayList<AckBody> pastChanges = documentPastChanges.get(documentId);
        pastChanges.add(change);
        documentPastChanges.put(documentId, pastChanges);
        System.out.println("Server's version" + version);
    }
}
