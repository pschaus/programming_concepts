package oop;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class ChatAppTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testSubscriptionAndMessageNotification() {
        ChatApp.ChatRoom room = new ChatApp.ChatRoom("general");
        ChatApp.ChatRoom games = new ChatApp.ChatRoom("games");
        ChatApp.ChatUser alice = new ChatApp.ChatUser("Alice");
        ChatApp.ChatUser bob = new ChatApp.ChatUser("Bob");
        ChatApp.ChatUser john = new ChatApp.ChatUser("John");

        room.subscribe(alice);
        room.subscribe(bob);
        alice.sendMessage(room, "Salut tout le monde !");

        String console = output.toString();

        assertTrue(console.contains("Alice a rejoint le channel #general"));
        assertTrue(console.contains("Bob a rejoint le channel #general"));
        assertFalse(console.contains("John a rejoint le channel #general"));

        assertTrue(console.contains("Alice (general): Salut tout le monde !"));
        assertTrue(console.contains("[Bob] reçoit sur #general > Alice: Salut tout le monde !"));
        assertFalse(console.contains("[Bob] reçoit sur #games > Alice: Salut tout le monde !"));

        // BEGIN STRIP
        assertFalse(console.contains("[John] reçoit sur #general > Alice: Salut tout le monde !"));
        assertFalse(console.contains("[Alice] reçoit sur #general > Alice: Salut tout le monde !"));
        // END STRIP

        output.reset();

        room.subscribe(bob);

        console = output.toString();
        assertFalse(console.contains("Bob a rejoint le channel #general"));
        assertTrue(console.contains("Bob est déjà abonné au channel #general"));
    }

    @Test
    void testMuteCommand() {
        ChatApp.ChatRoom room = new ChatApp.ChatRoom("sport");
        ChatApp.ChatUser bob = new ChatApp.ChatUser("Bob");
        ChatApp.ChatUser charlie = new ChatApp.ChatUser("Charlie");

        room.subscribe(bob);
        room.subscribe(charlie);

        bob.sendMessage(room, "/mute Charlie");
        assertTrue(bob.isMuted("Charlie"));

        output.reset();
        charlie.sendMessage(room, "Salut !");
        String console = output.toString();
        assertFalse(console.contains("[Bob] reçoit"));
        assertTrue(console.contains("Charlie (sport): Salut !"));

        // BEGIN STRIP
        output.reset();

        bob.sendMessage(room, "/mute Charlie");
        charlie.sendMessage(room, "Ca va?");
        console = output.toString();
        assertTrue(console.contains("Charlie (sport): Ca va?"));
        assertTrue(console.contains("[Bob] reçoit sur #sport > Charlie: Ca va?"));
        // END STRIP

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

        // BEGIN STRIP
        alice.sendMessage(room, "Hello");
        String console = output.toString();

        assertTrue(console.contains("[Bob] reçoit sur #TPs > Alice: Hello"));
        output.reset();
        // END STRIP
        room.unsubscribe(bob);

        // BEGIN STRIP
        alice.sendMessage(room, "Vous avez fait l'exercice 4?");
        console = output.toString();
        assertFalse(console.contains("[Bob] reçoit"));
        // END STRIP

        assertEquals(1, room.getUserCount());
    }
}