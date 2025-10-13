package oop;
import java.util.*;

/**
 * Dans ce TP, vous allez implémenter une salle de discussion en utilisant le pattern Observer.
 * Lorsqu’un utilisateur envoie un message, tous les autres abonnés de la salle doivent être notifiés.
 *
 * Un utilisateur peut mettre en silencieux (mute) d'autres utilisateurs, dans ce cas il ne recevra pas les messages
 * des utilisateurs "muted", peu importe la salle de discussion (channel)
 *
 * Un utilisateur peut s'abonner ou se désabonner aux différentes salles, pour recevoir les messages envoyés dessus.
 *
 * ---------------------------------------------------------
 * Classes incluses :
 *  - interface User (Observer)
 *  - classe ChatUser (implémentation d’un utilisateur)
 *  - classe ChatRoom (Subject)
 *  - classe ChatApp (point d’entrée)
 */

/**
Exemple de fonctionnement attendu avec affichage console :

ChatRoom general = new ChatRoom("general");
ChatUser alice = new ChatUser("Alice");
ChatUser bob = new ChatUser("Bob");

general.subscribe(alice);
// Console : Alice a rejoint le channel #general
general.subscribe(bob);
// Console : Bob a rejoint le channel #general

bob.sendMessage(general, "Salut !");
// Console : Bob (general): Salut !
// Console : [Alice] reçoit sur #general → Bob: Salut !

alice.sendMessage(general, "/mute Bob");
// Console : Alice a muté Bob

bob.sendMessage(general, "Encore un message");
// Console : Bob (general): Encore un message
// Alice ne reçoit rien car Bob est muté

alice.sendMessage(general, "/mute Bob");
// Console : Alice a réactivé Bob

bob.sendMessage(general, "Ça remarche ?");
// Console : Bob (general): Ça remarche ?
// Console : [Alice] reçoit sur #general → Bob: Ça remarche ?

general.unsubscribe(alice);
// Console : Alice a quitté le channel #general

general.unsubscribe(alice);
// Console : Alice n'est pas dans le channel #general
*/
public class ChatApp {

    /* ------------------ OBSERVER ------------------ */
    public interface User {
        void update(String channel, String message);
        String getName();
    }

    /* ------------------ CONCRETE OBSERVER ------------------ */
    public static class ChatUser implements User {
        private String name;
        private ArrayList<String> mutedUsers = new ArrayList<>();

        public ChatUser(String name) {
            // TODO
            // BEGIN STRIP
            this.name = name;
            // END STRIP
        }

        /**
         * Notifie l’utilisateur qu’un message a été reçu dans un canal.
         * Si l’expéditeur est dans la liste des utilisateurs "mutés",
         * le message est ignoré.
         *
         *  Le message est affiché sur la console au format suivant:
         *  "[<username>] reçoit sur #<channel> > <username expéditeur> : message
         * @param channel nom du canal où le message est posté
         * @param message message complet sous la forme "Expéditeur: contenu"
         */
        @Override
        public void update(String channel, String message) {
            // TODO
            // BEGIN STRIP
            String senderName = message.split(":")[0];
            if (!mutedUsers.contains(senderName)) {
                System.out.println("[" + name + "] reçoit sur #" + channel + " > " + message);
            }
            // END STRIP
        }

        /**
         * @return le nom de cet utilisateur
         */
        @Override
        public String getName() {
            // TODO
            // BEGIN STRIP
            return name;
            // END STRIP
        }

        /**
         * Envoie un message à la salle spécifiée.
         * Peut être un message normal ou une commande spéciale (ex: /mute).
         *
         * @param room    la salle de discussion où envoyer le message
         * @param message le message à envoyer
         */
        public void sendMessage(ChatRoom room, String message) {
            // TODO
            // BEGIN STRIP
            room.sendMessage(this, message);
            // END STRIP
        }

        /**
         * Active ou désactive le mute d’un utilisateur.
         * Si l’utilisateur est déjà muté => on le "démute"
         * Sinon => on le mute.
         * @param userName nom de l’utilisateur à (dé)muter
         */
        public void toggleMuteUser(String userName) {
            // TODO
            // BEGIN STRIP
            if (isMuted(userName)) {
                // Unmute
                for (int i = 0; i < mutedUsers.size(); i++) {
                    if (mutedUsers.get(i).equals(userName)) {
                        mutedUsers.remove(i);
                        System.out.println(name + " a réactivé " + userName);
                        return;
                    }
                }
            } else {
                // Mute
                mutedUsers.add(userName);
                System.out.println(name + " a muté " + userName);
            }
            // END STRIP
        }

        /**
         * Vérifie si un utilisateur donné est actuellement muté.
         *
         * @param userName nom de l’utilisateur à vérifier
         * @return true si l’utilisateur est muté, false sinon
         */
        public boolean isMuted(String userName) {
            // TODO
            // BEGIN STRIP
            for (String muted : mutedUsers) {
                if (muted.equals(userName)) {
                    return true;
                }
            }
            // END STRIP
            return false;

        }
    }

    /* ------------------ SUBJECT ------------------ */
    /**
     * Représente une salle de discussion (canal).
     * C’est le "Subject" du pattern Observer :
     *  - Elle garde une liste d’utilisateurs abonnés.
     *  - Lorsqu’un message est envoyé, elle notifie tous les abonnés.
     */
    public static class ChatRoom {
        private String channelName;
        private List<User> users;

        /**
         * Constructeur pour une nouvelle salle de discussion.
         *
         * @param channelName le nom du canal (ex: "general", "loisirs", "jeux vidéos", ...)
         */
        public ChatRoom(String channelName) {
            // TODO
            // BEGIN STRIP
            this.channelName = channelName;
            this.users = new ArrayList<>();
            // END STRIP
        }

        /**
         * @return le nom de la salle
         */
        public String getChannelName() {
            return channelName;
        }

        /**
         * Ajoute un utilisateur à la liste des abonnés à la salle.
         * Un message de bienvenue est affiché dans la console
         * au format suivant "<username> a rejoint le channel #<channelname>
         *
         * Si l'utilisateur est déjà abonné au canal, le message suivant est affiché:
         * "<username> est déjà abonné au channel #<channelname>
         * @param user l’utilisateur qui rejoint la salle
         */
        public void subscribe(User user) {
            // TODO
            // BEGIN STRIP
            for (User channel_user : users) {
                if (channel_user.equals(user)) {
                    System.out.println(user.getName() + " est déjà abonné au channel #" + channelName);
                    return;
                }
            }
            users.add(user);
            System.out.println(user.getName() + " a rejoint le channel #" + channelName);
            // END STRIP
        }

        /**
         * Retire un utilisateur de la salle.
         * Un message de départ est affiché dans la console.
         * au format suivant "<username> a quitté le channel #<channelname>
         *
         * Si l'utilisateur n'est pas dans le canal, affiche le message suivant
         * "<username> n'est pas dans le channel #<channelname>
         *
         * @param user l’utilisateur à retirer
         */
        public void unsubscribe(User user) {
            // TODO
            // BEGIN STRIP
            if (users.contains(user)) {
                users.remove(user);
                System.out.println(user.getName() + " a quitté le channel #" + channelName);
            } else {
                System.out.println(user.getName() + " n'est pas dans le channel #" + channelName);
            }
            // END STRIP
        }

        /**
         * Traite un message envoyé dans la salle.
         * <p>
         * - Si le message commence par "/mute", il est traité comme une commande
         *   pour activer ou désactiver le mute d’un autre utilisateur
         *   au format suivant : "/mute <username>"
         *
         *   - Sinon, le message est envoyé à tous les abonnés et affiché sur la console sous la forme:
         *   "<sender username> (<channel>): <message>"
         *
         * @param sender  l’utilisateur qui envoie le message
         * @param message contenu du message ou commande
         */
        public void sendMessage(User sender, String message) {
            // TODO
            // BEGIN STRIP
            if (message.startsWith("/mute")) {
                String[] parts = message.split(" ");
                if (parts.length == 2 && sender instanceof ChatUser) {
                    ((ChatUser) sender).toggleMuteUser(parts[1]);
                } else {
                    System.out.println("Usage: /mute <nomUtilisateur>");
                }
                return;
            }

            System.out.println(sender.getName() + " (" + channelName + "): " + message);
            notifyUsers(sender, message);
            // END STRIP
        }

        /**
         * Notifie tous les utilisateurs du canal d’un nouveau message,
         * sauf l’expéditeur lui-même.
         *
         * @param sender  l’expéditeur du message
         * @param message contenu du message
         */
        private void notifyUsers(User sender, String message) {
            // TODO
            // BEGIN STRIP
            for (User user : users) {
                if (!user.equals(sender)) {
                    user.update(channelName, sender.getName() + ": " + message);
                }
            }
            // END STRIP
        }

        /**
         * @return le nombre d’utilisateurs actuellement connectés à la salle
         */
        public int getUserCount() {
            // TODO
            // BEGIN STRIP
            return users.size();
            // END STRIP
            // STUDENT return false;
        }
    }

    /* ------------------ MAIN ------------------ */
    public static void main(String[] args) {}
}
