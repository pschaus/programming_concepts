package oop;
import java.util.*;

/**
 * In this practical exercise, you will implement a chat room system using the Observer pattern.
 *
 * When a user sends a message, all other subscribers of the chat room must be notified.
 * ALl messages sent or received by a user are stored in its log in the form of an arraylist
 * of Message objects
 *
 * A user can mute other users. In that case, they will not receive messages from muted users,
 * regardless of which chat room (channel) the messages are sent in.
 *
 * Users can subscribe or unsubscribe from different chat rooms to receive messages sent there.
 *
 * ---------------------------------------------------------
 * Included classes:
 *   - ChatMessage class (represents a message or event in the system)
 *   - User interface (Observer)
 *   - ChatUser class (concrete implementation of a user) TODO Implement its methods
 *   - ChatRoom class (Subject) TODO Implement its methods
 *   - ChatApp class (entry point)
 *
 *
 * ---------------------------------------------------------
 *
 * Example of expected behavior with user logs:
 *
 * ChatRoom general = new ChatRoom("general");
 * ChatUser alice = new ChatUser("Alice");
 * ChatUser bob = new ChatUser("Bob");
 *
 * general.subscribe(alice);
 * // Alice joins channel #general
 * // ChatMessage added to Alice's log:
 * // new ChatMessage("join", "general", "Alice", "")
 *
 * general.subscribe(bob);
 * // Bob joins channel #general
 * // ChatMessage added to Bob's log:
 * // new ChatMessage("join", "general", "Bob", "")
 *
 * bob.sendMessage(general, new ChatMessage("post", "general", "Bob", "Hi!"));
 * // Bob posts a message in #general
 * // ChatMessage added to Bob's log:
 * // new ChatMessage("post", "general", "Bob", "Hi!")
 * // Alice receives the message
 * // ChatMessage added to Alice's log:
 * // new ChatMessage("receive", "general", "Bob", "Hi!")
 *
 * alice.sendMessage(general, new ChatMessage("mute", "-", "Bob", ""));
 * // Alice mutes Bob
 * // ChatMessage added to Alice's log only:
 * // new ChatMessage("mute", "-", "Bob", "")
 *
 * bob.sendMessage(general, new ChatMessage("post", "general", "Bob", "Another message"));
 * // Bob posts another message
 * // ChatMessage added to Bob's log:
 * // new ChatMessage("post", "general", "Bob", "Another message")
 * // Alice does NOT receive it (muted)
 *
 * alice.sendMessage(general, new ChatMessage("mute", "-", "Bob", ""));
 * // Alice unmutes Bob
 * // ChatMessage added to Alice's log only:
 * // new ChatMessage("mute", "-", "Bob", "")
 *
 * bob.sendMessage(general, new ChatMessage("post", "general", "Bob", "It works again?"));
 * // Bob posts a message
 * // ChatMessage added to Bob's log:
 * // new ChatMessage("post", "general", "Bob", "It works again?")
 * // Alice receives it
 * // ChatMessage added to Alice's log:
 * // new ChatMessage("receive", "general", "Bob", "It works again?")
 *
 * general.unsubscribe(alice);
 * // Alice leaves channel #general
 * // ChatMessage added to Alice's log:
 * // new ChatMessage("leave", "general", "Alice", "")
 *
 * general.unsubscribe(alice);
 * // Alice was not in the channel
 * // ChatMessage added to Alice's log:
 * // new ChatMessage("info", "general", "Alice", "Alice is not in channel #general")
 */

public class ChatApp {

    /**
     * Represents a single entry in a user's activity log.
     *
     * Each ChatMessage corresponds to an event or an action in the chat application:
     *
     *   - "join": when a user joins a channel
     *     - `user` = joining user
     *     - `channel` = channel joined
     *     - `content` = optional, can be empty
     *
     *   - "leave": when a user leaves a channel
     *     - `user` = leaving user
     *     - `channel` = channel left
     *     - `content` = optional, can be empty
     *
     *   - "info": informational messages (e.g., already subscribed or invalid command)
     *     - `user` = user affected by the info
     *     - `channel` = relevant channel
     *     - `content` = description of the info (optional, can be empty)
     *     - Only logged in the sender's log (not broadcast to others)
     *
     *
     *   - "post": when a user sends a message to a channel
     *     - `user` = sender
     *     - `channel` = channel where message was sent
     *     - `content` = message text
     *
     *   - "receive": when a user receives a message from another user
     *     - `user` = sender of the message
     *     - `channel` = channel where message was sent
     *     - `content` = message text from sender
     *
     *   - "mute": when a user mutes or unmutes another user
     *     - `user` = target of the mute/unmute
     *     - `channel` = "-" (global)
     *     - `content` = optional, can be empty
     *     - Only logged in the sender's log (not broadcast to others)
     *
     * These objects allow keeping a complete history of all user actions
     * without relying on console output.
     *
     * Example:
     *   new ChatMessage("receive", "general", "Bob",
     *       "Hi!"); // Alice received a message from Bob in #general
     *
     *   new ChatMessage("mute", "-", "Bob", ""); // Sender muted Bob (only in sender's log)
     */
    public static class ChatMessage {
        private final String type;
        private final String channel;
        private final String user;
        private final String content;

        public ChatMessage(String type, String channel, String user, String content) {
            this.type = type;
            this.channel = channel;
            this.user = user;
            this.content = content;
        }

        public String getType() { return type; }
        public String getChannel() { return channel; }
        public String getUser() { return user; }
        public String getContent() { return content; }

        @Override
        public String toString() {
            return "[" + type + "] " + user + "@" + channel + ": " + content;
        }
    }

    /* ------------------ OBSERVER INTERFACE ------------------ */
    /**
     * Defines the Observer interface.
     * Each user can receive updates from a chat room, retrieve their name,
     * and access their message log.
     */
    public interface User {
        void update(ChatMessage message);
        String getName();
        List<ChatMessage> getLog();
        void toggleMuteUser(String userName);
    }

    /* ------------------ CONCRETE OBSERVER ------------------ */
    /**
     * Represents a user (Observer) who can join chat rooms, send messages,
     * mute/unmute other users, and keep an internal activity log.
     */
    public static class ChatUser implements User {
        private String name;
        private List<String> mutedUsers = new ArrayList<>();
        private List<ChatMessage> log = new ArrayList<>();

        public ChatUser(String name) {
            this.name = name;
        }

        /**
         * Called when a message is sent in a channel the user is subscribed to.
         * If the sender is muted, the message is ignored
         * Otherwise, if the message is a "post" message, add the corresponding
         * "receive" message to the user's log
         *
         * @param message the post message sent on the channel
         */
        @Override
        public void update(ChatMessage message) {
            // TODO
            // BEGIN STRIP
            // Ignore messages from muted users
            if (message.getType().equals("post") && !mutedUsers.contains(message.getUser())) {
                log.add(new ChatMessage("receive", message.getChannel(), message.getUser(), message.getContent()));
            }
            // END STRIP
        }

        /** @return the name of this user */
        @Override
        public String getName() {
            return name;
        }

        /** @return the list of all ChatMessages recorded for this user */
        public List<ChatMessage> getLog() {
            return log;
        }

        /**
         * Sends a message to the given chat room.
         * The message can be a normal text or a command (e.g. /mute).
         *
         * @param room    the chat room to send the message to
         * @param message the message content
         * @return a list of ChatMessages generated by this action
         */
        public List<ChatMessage> sendMessage(ChatRoom room, ChatMessage message) {
            // TODO
            // BEGIN STRIP
            List<ChatMessage> messages = room.sendMessage(this, message);
            log.addAll(messages);
            return messages;
            // END STRIP
        }

        /**
         * Toggles the mute state of another user.
         *
         * This action is local to the sender: it only affects the sender’s log.
         * The `user` field of the generated ChatMessage corresponds to the user
         * being muted or unmuted (the target), while the sender remains implicit.
         *
         * @param userName the name of the user to mute or unmute
         */
        @Override
        public void toggleMuteUser(String userName) {
            // TODO
            // BEGIN STRIP
            if (isMuted(userName)) {
                mutedUsers.remove(userName);
            } else {
                mutedUsers.add(userName);
            }
            // Only logged in sender's log
            log.add(new ChatMessage("mute", "-", userName, ""));
            // END STRIP
        }

        /**
         * Checks whether a given user is currently muted by the current used.
         *
         * @param userName the user to check
         * @return true if muted, false otherwise
         */
        public boolean isMuted(String userName) {
            return mutedUsers.contains(userName);
        }
    }

    /* ------------------ SUBJECT ------------------ */
    /**
     * Represents a chat room (channel).
     * This class is the "Subject" in the Observer pattern:
     *  - It keeps a list of subscribed users.
     *  - When a message is sent, it notifies all subscribers except the sender.
     */
    public static class ChatRoom {
        private String channelName;
        private List<User> users;

        /**
         * Creates a new chat room with the given name.
         *
         * @param channelName the channel name (e.g. "general", "sports", "games")
         */
        public ChatRoom(String channelName) {
            this.channelName = channelName;
            this.users = new ArrayList<>();
        }

        /** @return the name of the chat room */
        public String getChannelName() {
            return channelName;
        }

        /**
         * Subscribes a user to this chat room.
         * If the user is already subscribed, an informational message is added to their log.
         *
         * @param user the user joining the room
         * @return the ChatMessage created for this event
         */
        public ChatMessage subscribe(User user) {
            // TODO
            // BEGIN STRIP
            for (User u : users) {
                if (u.equals(user)) {
                    ChatMessage msg = new ChatMessage("info", channelName, user.getName(),
                            user.getName() + " is already subscribed to channel #" + channelName);
                    user.getLog().add(msg);
                    return msg;
                }
            }
            users.add(user);
            ChatMessage msg = new ChatMessage("join", channelName, user.getName(), "");
            user.getLog().add(msg);
            return msg;
            // END STRIP
        }

        /**
         * Unsubscribes a user from this chat room.
         * If the user was not subscribed, an informational message is added instead.
         *
         * @param user the user leaving the room
         * @return the ChatMessage created for this event
         */
        public ChatMessage unsubscribe(User user) {
            // TODO
            // BEGIN STRIP
            ChatMessage msg;
            if (users.contains(user)) {
                users.remove(user);
                msg = new ChatMessage("leave", channelName, user.getName(),"");
            } else {
                msg = new ChatMessage("info", channelName, user.getName(),
                        user.getName() + " is not in channel #" + channelName);
            }
            user.getLog().add(msg);
            return msg;
            // END STRIP
        }

        /**
         * Processes a ChatMessage sent to this chat room.
         *
         * - If the message type is "mute", it is treated as a command to
         *   mute or unmute another user. The action is only logged in the sender’s log,
         *   and the `user` field of the message corresponds to the target user.
         *
         * - If the message type is "post", it is broadcast to all other subscribed users.
         *   The sender logs the message in their own log, while each receiver logs it
         *   as a "receive" message (with `user` = sender) in their log.
         *
         * - Any other message type is treated as unknown, generating an "info" message
         *   in the sender’s log.
         */
        public List<ChatMessage> sendMessage(User sender, ChatMessage message) {
            // TODO
            // BEGIN STRIP
            List<ChatMessage> localLogs = new ArrayList<>();

            switch (message.getType()) {
                case "mute": {
                    sender.toggleMuteUser(message.getUser());
                    localLogs.add(message);
                    break;
                }
                case "post": {
                    // Sender logs the post
                    localLogs.add(message);
                    // Notify all other users
                    notifyUsers(sender, message);
                    break;
                }
                default:
                    localLogs.add(new ChatMessage("info", "-", sender.getName(), "Unknown message type"));
            }

            return localLogs;
            // END STRIP
        }

        /**
         * Notifies all users in the channel of a new posted message,
         * except the sender themselves.
         *
         * @param sender  the user who sent the message
         */
        private void notifyUsers(User sender, ChatMessage message) {
            // TODO
            // BEGIN STRIP
            for (User user : users) {
                if (!user.equals(sender)) {
                    // Store receive messages in receiver's log, user field = sender
                    user.update(message);
                }
            }
            // END STRIP
        }


        /** @return the number of users currently subscribed to this room */
        public int getUserCount() {
            return users.size();
        }
    }

    /* ------------------ MAIN ------------------ */
    public static void main(String[] args) {}
}
