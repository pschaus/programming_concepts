package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import oop.ChatRoom.ChatUser;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class ChatRoomTest {
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEmptyChatRoom() {
        ChatRoom room = new ChatRoom();

        // Nothing was written in the chat room.
        // The log should be an empty list.
        ArrayList<String> log = room.getMessageLog();
        assertNotEquals(null, log);
        assertEquals(0, log.size());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testTwoUsers() {
        ChatRoom room = new ChatRoom();
        ChatUser alice = new ChatUser("Alice");
        ChatUser bob = new ChatUser("Bob");
        room.writeMessage(alice, "Hello");
        room.writeMessage(bob,"Good morning");

        // The log should contain two messages.
        ArrayList<String> log = room.getMessageLog();
        assertNotEquals(null, log);
        assertEquals(2, log.size());
        assertEquals("Alice wrote Hello", log.get(0));
        assertEquals("Bob wrote Good morning", log.get(1));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testTwoRooms() {
        ChatRoom roomA = new ChatRoom();
        ChatRoom roomB = new ChatRoom();

        ChatUser alice = new ChatUser("Alice");
        ChatUser bob = new ChatUser("Bob");
        roomA.writeMessage(alice, "Hello");
        roomB.writeMessage(bob, "How are you?");
        roomB.writeMessage(alice,"Good morning");

        // message log of room A
        ArrayList<String> logA = roomA.getMessageLog();
        assertEquals(1, logA.size());
        assertEquals("Alice wrote Hello", logA.get(0));

        // message log of room B
        ArrayList<String> logB = roomB.getMessageLog();
        assertEquals(2, logB.size());
        assertEquals("Bob wrote How are you?", logB.get(0));
        assertEquals("Alice wrote Good morning", logB.get(1));
    }
}
