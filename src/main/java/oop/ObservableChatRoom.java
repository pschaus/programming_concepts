package oop;

import java.util.*;

/**
 *  !!!! Please do the ChatRoom exercise first !!!!
 *
 * In this exercise, you will implement a chat room system
 * using the Observer design pattern.
 *
 * Chat rooms are represented by objects of the class ObservableChatRoom
 * and users are represented by objects implementing the
 * ObservingChatUser interface.
 *
 * Users can write messages in chat rooms as shown below:
 *
 *    ObservableChatRoom room = new ObservableChatRoom();
 *    ObservingChatUser alice = ......;
 *    ObservingChatUser bob = ......;
 *    ObservingChatUser claude = .....;
 *    room.writeMessage(claude, "hello");  // Claude writes a message
 *
 * Users can subscribe to chat rooms. Every time somebody writes
 * a message in the chat room, all subscribed users are notified
 * (but not the user who wrote the message):
 *
 *    room.subscribe(alice);
 *    room.subscribe(bob);
 *    room.subscribe(claude);
 *    room.writeMessage(claude, "how are you doing?");
 *    // -> Alice and Bob are notified
 *
 * This means that the chat room is the Observable and the subscribed
 * users are the observers. The receiveNotification method of the
 * users has as parameters the name of the user who sent the message
 * and the message.
 *
 * A user can also unsubscribe from a chat room. After unsubscribing, the
 * user does not receive notifications anymore:
 *
 *    room.unsubscribe(alice);
 *    room.writeMessage(claude, "me again");
 *    // -> Alice is NOT notified.
 *
 *  Reminder: An ArrayList is like a Python list. You can add elements
 *  with the method add(...), and you can access elements with the method
 *  get(...). The class also has methods contains(...) and remove(...).
 */


public class ObservableChatRoom {
    // the list of all users subscribed to this chat room
    private final ArrayList<ObservingChatUser> subscribedUsers = new ArrayList<ObservingChatUser>();

    /**
     * The observer interface
     */
    public static interface ObservingChatUser {
        public String getName();
        public void receiveNotification(String senderName, String text);
    }

    /**
     * Here is a class that implements the observer interface.
     * It represents a user who counts the number of notifications that
     * he or she has received.
     */
    public static class CountingUser implements ObservingChatUser {
        // TODO add variables if needed
        // BEGIN STRIP
        private final String name;
        private int count;
        // END STRIP

        // Constructor
        public CountingUser(String name) {
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

        @Override
        public void receiveNotification(String senderName, String text) {
            System.out.println("Message written by " +senderName + ": "+text);

            // TODO count the number of notifications
            // BEGIN STRIP
            this.count++;
            // END STRIP
        }

        // Returns the number of notifications that this user received
        public int getCount() {
            // STUDENT return -1;
            // BEGIN STRIP
            return this.count;
            // END STRIP
        }
    }

    /**
     * Subscribe the user to this room.
     * The user will be notified about new messages.
     * If the user is already subscribed, do nothing.
     *
     * @param user  The user. You can assume that user!=null
     */
    public void subscribe(ObservingChatUser user) {
        // TODO
        // BEGIN STRIP
        if(!subscribedUsers.contains(user)) {
            subscribedUsers.add(user);
        }
        // END STRIP
    }

    /**
     * Unsubscribe the user from this room.
     * The user will not be notified anymore about new messages.
     *
     * @param user The user. You can assume that the user was subscribed.
     */
    public void unsubscribe(ObservingChatUser user) {
        // TODO
        // BEGIN STRIP
        subscribedUsers.remove(user);
        // END STRIP
    }

    /**
     * Write a message into the chat room.
     * This will notify all subscribed users about this new message.
     * The user who writes the message must not be notified.
     *
     * @param user   The user who writes the message
     * @param text   The message
     */
    public void writeMessage(ObservingChatUser user, String text) {
        // TODO
        // BEGIN STRIP
        for(ObservingChatUser otherUser : subscribedUsers) {
            if(otherUser!=user) {
                otherUser.receiveNotification(user.getName(), text);
            }
        }
        // END STRIP
    }

    //===================================================
    // Example:
    // If you have implemented everything correctly,
    // you should see:
    //     Message written by Alice: Hello
    //     Message written by Bob: Good morning
    //     Alice has been notified: 1
    //     Bob has been notified: 1
    //===================================================

    public static void main(String[] args) {
        ObservableChatRoom room = new ObservableChatRoom();

        CountingUser alice = new CountingUser("Alice");
        CountingUser bob = new CountingUser("Bob");

        room.subscribe(alice);
        room.subscribe(bob);

        room.writeMessage(alice, "Hello");
        room.writeMessage(bob,"Good morning");

        System.out.println("Alice has been notified: " + alice.getCount());
        System.out.println("Bob has been notified: " + bob.getCount());
    }
}
