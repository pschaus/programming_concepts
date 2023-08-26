package oop;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Your task is to control a robot using a sequence of textual
 * commands. The robot can move forward, turn left, or turn right. The
 * robot is controlled through the following set of 4 basic commands:
 *
 * - FORWARD
 *   => Move the robot forward
 *
 * - LEFT
 *   => Turn the robot to the left
 *
 * - RIGHT
 *   => Turn the robot to the right
 *
 * - REPEAT N
 *   ...
 *   END REPEAT
 *   => Repeat "N" times the commands "..."
 *
 * For instance, here is a sample sequence of textual commands:
 *
 *  FORWARD
 *  REPEAT 3
 *  FORWARD
 *  RIGHT
 *  END REPEAT
 *  FORWARD
 *  FORWARD
 *
 * If applied to a robot that turns at right angles, this sample
 * sequence would generate the following pattern, where "x" denotes
 * the starting point of the robot, and "^" is its final position:
 *
 *      ^
 *      |
 *      |
 * x---->---->
 *      |    |
 *      |    |
 *      <----<
 *
 * Pay attention to the fact that the "REPEAT" commands can be nested
 * (i.e. a "REPEAT" command may recursively contain other "REPEAT"
 * commands).
 *
 * Using the "Factory" design pattern, you must convert an array of
 * strings containing commands into an "Action" object that can be
 * used to control one instance of the "Robot" interface.
 **/

public class RobotActionFactory {

    /**
     * Interface defining an abstract robot to be controlled.
     **/
    public static interface Robot {

        /**
         * Move the robot forward.
         **/
        void moveForward();

        /**
         * Turn the robot to the left.
         **/
        void turnLeft();

        /**
         * Turn the robot to the right.
         **/
        void turnRight();
    }


    /**
     * Interface defining an abstract action that modifies the state
     * of the given robot. An action can correspond to one isolated
     * command (move forward, turn left, or turn right), to one
     * sequence of actions, or to one repetition of an action.
     **/
    public static interface Action {

        /**
         * Apply this action to the given robot.
         * @param robot The robot.
         **/
        void apply(Robot robot);
    }


    /**
     * This type of "Action" moves the robot forward.
     **/
    private static class MoveForwardAction implements Action {
        @Override
        public void apply(Robot robot) {
            // TODO Implement the body of this method
            // BEGIN STRIP
            robot.moveForward();
            // END STRIP
        }
    }


    /**
     * This type of "Action" turns the robot to the left.
     **/
    private static class TurnLeftAction implements Action {
        @Override
        public void apply(Robot robot) {
            // TODO Implement the body of this method
            // BEGIN STRIP
            robot.turnLeft();
            // END STRIP
        }
    }


    /**
     * This type of "Action" turns the robot to the right.
     **/
    private static class TurnRightAction implements Action {
        @Override
        public void apply(Robot robot) {
            // TODO Implement the body of this method
            // BEGIN STRIP
            robot.turnRight();
            // END STRIP
        }
    }


    /**
     * This type of "Action" represents a sequence of actions to be
     * applied to the robot.
     **/
    private static class SequenceOfActions implements Action {
        private List<Action> actions = new LinkedList<Action>();

        /**
         * Append a new action to the end of the sequence of actions.
         * @param action The action to be added.
         **/
        public void add(Action action) {
            actions.add(action);
        }

        @Override
        public void apply(Robot robot) {
            // TODO Implement the body of this method
            // BEGIN STRIP
            for (Action action: actions) {
                action.apply(robot);
            }
            // END STRIP
        }
    }


    /**
     * This type of "Action" executes another action, for a given
     * number of times.
     **/
    private static class RepeatAction implements Action {
        private int times;
        private Action action;

        /**
         * Constructor for a repetition of one action.
         * @param times The number of times the action must be executed.
         * @param action The action to be repeated.
         **/
        RepeatAction(int times,
                     Action action) {
            this.times = times;
            this.action = action;
        }

        @Override
        public void apply(Robot robot) {
            // TODO Implement the body of this method
            // BEGIN STRIP
            for (int i = 0; i < times; i++) {
                action.apply(robot);
            }
            // END STRIP
        }
    }

    /**
     * The factory method you have to implement.
     *
     * NB 1: In order to parse an integer from some string "s", you
     * can use the standard function "Integer.parseInt(s)".
     *
     * NB 2: If the array of commands cannot be parsed (e.g. because
     * of an unknown action, or because of a "REPEAT" command without
     * an "END REPEAT"), you must throw an exception of class
     * "IllegalArgumentException".
     *
     * @param commands The array of commands to drive the robot.
     * @return An "Action" object that will move the robot
     * according to the commands.
     **/
    public Action parse(String commands[]) {
        SequenceOfActions sequence = new SequenceOfActions();

        // TODO Implement the body of this method by filling the "sequence" object
        // BEGIN STRIP
        int i = 0;
        while (i < commands.length) {
            if (commands[i].equals("FORWARD")) {
                sequence.add(new MoveForwardAction());
                i++;
            } else if (commands[i].equals("LEFT")) {
                sequence.add(new TurnLeftAction());
                i++;
            } else if (commands[i].equals("RIGHT")) {
                sequence.add(new TurnRightAction());
                i++;
            } else if (commands[i].startsWith("REPEAT")) {
                String[] command = commands[i].split(" ");
                int times = Integer.parseInt(command[1]);

                int depth = 1;
                int j = i + 1;

                while (depth > 0 &&
                        j < commands.length) {
                    if (commands[j].startsWith("REPEAT")) {
                        depth ++;
                    } else if (commands[j].equals("END REPEAT")) {
                        depth --;
                    }
                    j++;
                }

                if (depth == 0) {
                    Action action = parse(Arrays.copyOfRange(commands, i + 1, j - 1));
                    sequence.add(new RepeatAction(times, action));
                    i = j;
                } else {
                    throw new IllegalArgumentException("Missing END REPEAT");
                }
            } else {
                throw new IllegalArgumentException("Unknown command: " + commands[i]);
            }
        }

        // END STRIP

        return sequence;
    }

    // BEGIN STRIP
    /*
    public Action parse(String commands[]) {
        ActionRes res = readSequence(commands,0);
        assert(res.idx == commands.length);
        return res.action;
    }



    class ActionRes {
        Action action;
        int idx;
        ActionRes(Action action, int idx) {
            this.action = action;
            this.idx = idx;
        }

    }

    private ActionRes readSequence(String commands[], int idx) {
        SequenceOfActions sequence = new SequenceOfActions();
        int i = idx;

        while (i < commands.length && !commands[i].equals("END REPEAT")) {
            if (commands[i].equals("FORWARD")) {
                sequence.add(new MoveForwardAction());
            } else if (commands[i].equals("LEFT")) {
                sequence.add(new TurnLeftAction());
            } else if (commands[i].equals("RIGHT")) {
                sequence.add(new TurnRightAction());
            }
            else if (commands[i].startsWith("REPEAT")) {
                String[] command = commands[i].split(" ");
                int times = Integer.parseInt(command[1]);
                ActionRes res = readSequence(commands,i+1);
                i = res.idx;
                if (i >= commands.length || !commands[i].equals("END REPEAT")) {
                    throw new IllegalArgumentException("Repeat without proper end");
                }
                sequence.add(new RepeatAction(times,res.action));
            } else {
                throw new IllegalArgumentException("Unknown command: " + commands[i]);
            }
            i++;
        }
        return new ActionRes(sequence,i);
    }
    */
    // END STRIP


}
