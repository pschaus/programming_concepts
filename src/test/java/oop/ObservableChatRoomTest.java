package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static oop.ObservableChatRoom.ObservingChatUser;
import static oop.ObservableChatRoom.CountingUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Grade
public class ObservableChatRoomTest {
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNoSubscriber() {
        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        CountingUser bob  = new CountingUser("Bob");

        room.writeMessage(alice, "Hello");
        room.writeMessage(bob, "Good morning");

        // Nothing should happen if there is no subscriber
        assertEquals(0, alice.getCount());
        assertEquals(0, bob.getCount());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testOneSubscriber() {
        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        CountingUser bob  = new CountingUser("Bob");

        room.subscribe(bob);

        room.writeMessage(alice, "Hello");
        room.writeMessage(bob, "Good morning");
        room.writeMessage(alice, "How are you?");

        // Alice is not subscribed
        assertEquals(0, alice.getCount());
        // Bob should receive all messages of Alice
        assertEquals(2, bob.getCount());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testUnsubscribe() {
        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        CountingUser bob  = new CountingUser("Bob");

        room.subscribe(bob);

        room.writeMessage(alice, "Hello");
        assertEquals(1, bob.getCount());

        room.unsubscribe(bob);

        room.writeMessage(alice, "Good morning");
        assertEquals(1, bob.getCount());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSubscribeTwice() {
        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        CountingUser bob  = new CountingUser("Bob");

        room.subscribe(bob);
        room.subscribe(bob);

        room.writeMessage(alice, "Hello");

        assertEquals(1, bob.getCount());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testManySubscribers() {
        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        CountingUser bob  = new CountingUser("Bob");
        CountingUser claude  = new CountingUser("Claude");

        room.subscribe(alice);
        room.subscribe(bob);
        room.subscribe(claude);

        room.writeMessage(alice, "Hello");
        room.writeMessage(bob, "Good morning");
        room.writeMessage(claude, "How are you?");
        room.writeMessage(bob, "Fine, thanks");

        assertEquals(3, alice.getCount());
        assertEquals(2, bob.getCount());
        assertEquals(3, claude.getCount());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNotification() {
        boolean messageReceive = false;

        // We define here our own user class because
        // we want to verify whether the user is correctly notified.
        class BobUser extends CountingUser {
            BobUser(String name) {
                super(name);
            }

            @Override
            public void receiveNotification(String senderName, String text) {
                assertEquals("Alice", senderName);
                assertEquals("Hello", text);
            }
        }

        ObservableChatRoom room = new ObservableChatRoom();
        CountingUser alice  = new CountingUser("Alice");
        BobUser bob  = new BobUser("Bob");

        room.subscribe(bob);
        room.writeMessage(alice, "Hello");
    }
}
