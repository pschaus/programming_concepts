package oop;

import java.util.ArrayList;

/**
 * In this exercise, you will implement a chat room system.
 *
 * Chat rooms are represented by objects of the class ChatRoom
 * and users are represented by objects of the class ChatUser.
 *
 * Users can write messages in chat rooms as shown below:
 *
 *    ChatRoom room = new ChatRoom();
 *    ChatUser alice = new ChatUser("Alice");
 *    ChatUser bob = new ChatUser("Bob");
 *    room.writeMessage(bob, "hello");  // Bob writes a message
 *
 * You can get the list of all messages written in the chat room with
 * the method getMessageLog():
 *
 *    ArrayList<String> log = room.getMessageLog();
 *
 *  When a user writes a message in the chat room, it is stored in the
 *  message log in the format "user_name wrote the_message".
 *  In our example above, the message log should therefore
 *  contain the message:
 *     "Bob wrote hello"
 *
 *  Reminder: An ArrayList is like a Python list. You can add elements
 *  with the method add(...), and you can access elements with the method
 *  get(...). The class also has a method contains(...).
 */


public class ChatRoom {
    // the list of all messages
    private final ArrayList<String> messageLog = new ArrayList<String>();

    /**
     * A chat room user.
     */
    public static class ChatUser {
        // TODO add variables if needed
        // BEGIN STRIP
        private final String name;
        // END STRIP

        // Constructor
        public ChatUser(String name) {
            // TODO
            // BEGIN STRIP
            this.name = name;
            // END STRIP
        }

        // Returns the name of the user
        public String getName() {
            // TODO
            // STUDENT return null;
            // BEGIN STRIP
            return this.name;
            // END STRIP
        }
    }

    /**
     * Return the message log (the list of all messages written in this chat room)
     * @return the message log
     */
    public ArrayList<String> getMessageLog() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return this.messageLog;
        // END STRIP
    }

    /**
     * Write a message into the chat room.
     * The message will be stored in the message log in the format
     *      "user_name wrote the_message"
     *
     * @param user   The user who writes the message.
     *               You can assume that user!=null
     * @param text   The message
     */
    public void writeMessage(ChatUser user, String text) {
        // TODO
        // BEGIN STRIP
        this.messageLog.add(user.getName() + " wrote " + text);
        // END STRIP
    }

    //===================================================
    // Example:
    // If you have implemented everything correctly,
    // you should see:
    //     Alice wrote Hello
    //     Bob wrote Good morning
    //===================================================

    public static void main(String[] args) {
        ChatRoom room = new ChatRoom();

        ChatUser alice = new ChatUser("Alice");
        ChatUser bob = new ChatUser("Bob");

        room.writeMessage(alice, "Hello");
        room.writeMessage(bob,"Good morning");

        ArrayList<String> log = room.getMessageLog();
        for(String logEntry : log) {
            System.out.println(logEntry);
        }
    }

}
