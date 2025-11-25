package oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ChatAppTest {

    @Test
    void testSubscriptionAndMessageNotification() {
        ChatApp.ChatRoom room = new ChatApp.ChatRoom("general");
        ChatApp.ChatUser alice = new ChatApp.ChatUser("Alice");
        ChatApp.ChatUser bob = new ChatApp.ChatUser("Bob");
        ChatApp.ChatUser john = new ChatApp.ChatUser("John");

        // Subscriptions
        room.subscribe(alice);
        room.subscribe(bob);

        // Alice sends message
        ChatApp.ChatMessage msg = new ChatApp.ChatMessage("post", "general", "Alice", "Salut tout le monde !");
        alice.sendMessage(room, msg);

        List<ChatApp.ChatMessage> aliceLog = alice.getLog();
        List<ChatApp.ChatMessage> bobLog = bob.getLog();
        List<ChatApp.ChatMessage> johnLog = john.getLog();

        // Subscription logs: check type and user fields
        assertTrue(aliceLog.stream().anyMatch(m -> m.getType().equals("join") && m.getUser().equals("Alice")));
        assertTrue(bobLog.stream().anyMatch(m -> m.getType().equals("join") && m.getUser().equals("Bob")));

        // Alice's post should be logged
        assertTrue(aliceLog.stream().anyMatch(m ->
                m.getType().equals("post") && m.getUser().equals("Alice") &&
                        m.getChannel().equals("general") &&
                        m.getContent().contains("Salut tout le monde !")));

        // Bob should have received the message
        assertTrue(bobLog.stream().anyMatch(m ->
                m.getType().equals("receive") && m.getUser().equals("Alice") &&
                        m.getChannel().equals("general") &&
                        m.getContent().contains("Salut tout le monde !")));

        // John should have empty log
        assertTrue(johnLog.isEmpty());

        // Test duplicate subscription
        ChatApp.ChatMessage result = room.subscribe(bob);

        // Check that the type, user and channel are correct
                assertEquals("info", result.getType());
                assertEquals("Bob", result.getUser());
                assertEquals("general", result.getChannel());

        // Bob should still be subscribed exactly once
                assertEquals(2, room.getUserCount()); // Alice + Bob

        // Unsubscribe Bob and check user count
                room.unsubscribe(bob);
                assertEquals(1, room.getUserCount()); // Only Alice remains

        // If we unsubscribe Bob again, he should not be present
                room.unsubscribe(bob);
                assertEquals(1, room.getUserCount()); // Still only Alice
    }

    @Test
    void testMuteCommand() {
        ChatApp.ChatRoom room = new ChatApp.ChatRoom("sport");
        ChatApp.ChatUser bob = new ChatApp.ChatUser("Bob");
        ChatApp.ChatUser charlie = new ChatApp.ChatUser("Charlie");

        room.subscribe(bob);
        room.subscribe(charlie);

        // Bob mutes Charlie
        ChatApp.ChatMessage muteMsg = new ChatApp.ChatMessage("mute", "-", "Charlie", "");
        bob.sendMessage(room, muteMsg);
        assertTrue(bob.isMuted("Charlie"));
        assertTrue(bob.getLog().stream().anyMatch(m -> m.getType().equals("mute") && m.getUser().equals("Charlie")));

        // Charlie sends message; Bob should not receive it
        ChatApp.ChatMessage postMsg = new ChatApp.ChatMessage("post", "sport", "Charlie", "Salut !");
        charlie.sendMessage(room, postMsg);
        assertFalse(bob.getLog().stream().anyMatch(m -> m.getType().equals("receive") && m.getUser().equals("Charlie")));
        assertTrue(charlie.getLog().stream().anyMatch(m -> m.getType().equals("post") && m.getContent().contains("Salut !")));

        // Bob unmutes Charlie
        bob.sendMessage(room, muteMsg);
        assertFalse(bob.isMuted("Charlie"));
        assertTrue(bob.getLog().stream().anyMatch(m -> m.getType().equals("mute") && m.getUser().equals("Charlie")));

        // Now Bob should receive Charlie’s messages again
        ChatApp.ChatMessage postMsg2 = new ChatApp.ChatMessage("post", "sport", "Charlie", "Ca va?");
        charlie.sendMessage(room, postMsg2);
        assertTrue(bob.getLog().stream().anyMatch(m ->
                m.getType().equals("receive") && m.getUser().equals("Charlie") &&
                        m.getContent().contains("Ca va?")));
        assertTrue(charlie.getLog().stream().anyMatch(m -> m.getType().equals("post") && m.getContent().contains("Ca va?")));
    }

    @Test
    void testUnsubscribe() {
        ChatApp.ChatRoom room = new ChatApp.ChatRoom("TPs");
        ChatApp.ChatUser alice = new ChatApp.ChatUser("Alice");
        ChatApp.ChatUser bob = new ChatApp.ChatUser("Bob");

        assertEquals(0, room.getUserCount());
        room.subscribe(alice);
        assertEquals(1, room.getUserCount());
        room.subscribe(bob);
        assertEquals(2, room.getUserCount());

        // Alice sends message → Bob should receive
        ChatApp.ChatMessage msg1 = new ChatApp.ChatMessage("post", "TPs", "Alice", "Hello");
        alice.sendMessage(room, msg1);
        assertTrue(bob.getLog().stream().anyMatch(m ->
                m.getType().equals("receive") && m.getUser().equals("Alice") &&
                        m.getContent().contains("Hello")));

        // Bob unsubscribes
        room.unsubscribe(bob);
        assertEquals(1, room.getUserCount());

        // Alice sends another → Bob should NOT receive
        int bobLogSizeBefore = bob.getLog().size();
        ChatApp.ChatMessage msg2 = new ChatApp.ChatMessage("post", "TPs", "Alice", "Vous avez fait l'exercice 4?");
        alice.sendMessage(room, msg2);

        // Check that Bob did not receive any new message from Alice
        assertEquals(bobLogSizeBefore, bob.getLog().size());
        assertFalse(bob.getLog().stream().anyMatch(m -> m.getType().equals("receive") &&
                m.getUser().equals("Alice") &&
                m.getContent().contains("Vous avez fait l'exercice 4?")));
    }
}
