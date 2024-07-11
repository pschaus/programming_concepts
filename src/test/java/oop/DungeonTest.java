package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


@Grade
class DungeonTest {
    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testSingleMoveInEmptyDungeon() {
        // an empty dungeon of size 5 x 3
        Dungeon dungeon = new Dungeon(5,3);

        // the player is at position (3,2) in the dungeon:
        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // the player does one step to the North
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertEquals(3, player.x);
        assertEquals(1, player.y);

        // let's also test the other directions
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        assertEquals(2, player.x);
        assertEquals(1, player.y);

        Dungeon.movePlayer(player, Dungeon.Direction.South);

        assertEquals(2, player.x);
        assertEquals(2, player.y);

        Dungeon.movePlayer(player, Dungeon.Direction.East);

        assertEquals(3, player.x);
        assertEquals(2, player.y);
        assertEquals(10, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testMultiMoveInEmptyDungeon() {
        Dungeon dungeon = new Dungeon(5, 3);

        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        Dungeon.movePlayer(player, Dungeon.Direction.West);
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        assertEquals(0, player.x);
        assertEquals(0, player.y);
        assertEquals(10, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testTrap() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putTrap(1,2);

        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(10, player.healthPoints);

        // move west and step on the trap!
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(9, player.healthPoints);

        // continue walking west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(9, player.healthPoints);

        // and go back east, step on the trap again!
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        assertEquals(8, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testHealthFountain() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putHealthFountain(1,2, 50);

        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move west and step on the health fountain!
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(60, player.healthPoints);

        // continue walking west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(60, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testDoorToAnotherDungeon() {
        // Two dungeons with a door from dungeon1 to dungeon2!
        Dungeon dungeon1 = new Dungeon(5, 3);
        Dungeon dungeon2 = new Dungeon(5, 3);
        dungeon1.putDoor(1,2, dungeon2);

        // Player starts in dungeon1
        Dungeon.Player player = new Dungeon.Player(dungeon1, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move west and step on the door
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // player is now in dungeon2
        assertSame(player.dungeon, dungeon2);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
    }

    @Test
    @Grade(value = 0.5, cpuTimeout = 1)
    public void testDoorToSameDungeon() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putDoor(1,2, dungeon);

        // Player starts in dungeon1
        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move west and step on the door
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // player is now in dungeon2
        assertSame(player.dungeon, dungeon);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testReplacingObject() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putTrap(1,2);

        // replace the trap by a health fountain
        dungeon.putHealthFountain(1,2, 50);

        Dungeon.Player player = new Dungeon.Player(dungeon, 2, 2, 10);

        // move west and step on the health fountain!
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(60, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testEverythingEveryWhere() {
        // Test case with two dungeons
        // Dungeon1 contains a door and two health fountains.
        // Dungeon2 contains a door, a health fountain, and two traps.

        Dungeon dungeon1 = new Dungeon(6, 4);
        Dungeon dungeon2 = new Dungeon(5, 3);

        dungeon1.putDoor(5,0, dungeon2);
        dungeon1.putHealthFountain(5, 1, 5);
        dungeon1.putHealthFountain(5, 2, 30);

        dungeon2.putDoor(2, 0, dungeon1);
        dungeon2.putHealthFountain(0,2, 10);
        dungeon2.putTrap(1,1);
        dungeon2.putTrap(2,1);

        Dungeon.Player player = new Dungeon.Player(dungeon1, 3, 2, 10);

        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertSame(player.dungeon, dungeon2);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        assertEquals(45, player.healthPoints);

        Dungeon.movePlayer(player, Dungeon.Direction.South);
        Dungeon.movePlayer(player, Dungeon.Direction.South);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertSame(player.dungeon, dungeon1);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        assertEquals(53, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 2)
    public void testLargeDungeon() {
        // do this test five times
        for(int run=0;run<5;run++) {
            int dim=50+2*new Random().nextInt(50);

            // a dungeon of size dim x dim filled with traps,
            Dungeon dungeon=new Dungeon(dim, dim);
            for(int y=1; y<dim-1; y++) {
                for(int x=1; x<dim-1; x++) {
                    dungeon.putTrap(x, y);
                }
            }

            // player starts at (0,0) and walks diagonally through the dungeon
            Dungeon.Player player=new Dungeon.Player(dungeon, 0, 0, dim*2);
            for(int i=1; i<dim; i++) {
                Dungeon.movePlayer(player, Dungeon.Direction.East);
                Dungeon.movePlayer(player, Dungeon.Direction.South);
            }

            assertEquals(dim-1, player.x);
            assertEquals(dim-1, player.y);
            assertEquals(5, player.healthPoints);
        }
    }

    // BEGIN STRIP
    //-------------------------------------------------------
    //-------------------------------------------------------
    //-------------------------------------------------------
    // Hidden tests

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testSingleMoveInEmptyDungeon2() {
        // an empty dungeon of size 5 x 3
        Dungeon dungeon = new Dungeon(5,3);

        // the player is at position (4,2) in the dungeon:
        Dungeon.Player player = new Dungeon.Player(dungeon, 4, 2, 10);

        // the player does one step to the North
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertEquals(4, player.x);
        assertEquals(1, player.y);

        // let's also test the other directions
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        assertEquals(3, player.x);
        assertEquals(1, player.y);

        Dungeon.movePlayer(player, Dungeon.Direction.South);

        assertEquals(3, player.x);
        assertEquals(2, player.y);

        Dungeon.movePlayer(player, Dungeon.Direction.East);

        assertEquals(4, player.x);
        assertEquals(2, player.y);
        assertEquals(10, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testMultiMoveInEmptyDungeon2() {
        Dungeon dungeon = new Dungeon(5, 4);

        Dungeon.Player player = new Dungeon.Player(dungeon, 4, 3, 10);

        Dungeon.movePlayer(player, Dungeon.Direction.West);
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        assertEquals(1, player.x);
        assertEquals(1, player.y);
        assertEquals(10, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testTrap2() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putTrap(0,2);

        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(10, player.healthPoints);

        // move west and step on the trap!
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(10, player.healthPoints);

        // continue walking west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(9, player.healthPoints);

        // and go back east, step on the trap again!
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        assertEquals(9, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testHealthFountain2() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putHealthFountain(1,2, 30);

        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move west and step on the health fountain!
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(40, player.healthPoints);

        // continue walking west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        assertEquals(40, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testDoorToAnotherDungeon2() {
        // Two dungeons with a door from dungeon1 to dungeon2!
        Dungeon dungeon1 = new Dungeon(5, 3);
        Dungeon dungeon2 = new Dungeon(5, 3);
        dungeon1.putDoor(1,1, dungeon2);

        // Player starts in dungeon1
        Dungeon.Player player = new Dungeon.Player(dungeon1, 3, 2, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move north and step on the door
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        // player is now in dungeon2
        assertSame(player.dungeon, dungeon2);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
    }

    @Test
    @Grade(value = 0.5, cpuTimeout = 1)
    public void testDoorToSameDungeon2() {
        Dungeon dungeon = new Dungeon(5, 3);
        dungeon.putDoor(1,1, dungeon);

        // Player starts in dungeon1
        Dungeon.Player player = new Dungeon.Player(dungeon, 3, 1, 10);

        // move west
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // move west and step on the door
        Dungeon.movePlayer(player, Dungeon.Direction.West);

        // player is now in dungeon2
        assertSame(player.dungeon, dungeon);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testReplacingObject2() {
        Dungeon dungeon = new Dungeon(5, 4);
        dungeon.putTrap(2,3);

        // replace the trap by a health fountain
        dungeon.putHealthFountain(2,3, 30);

        Dungeon.Player player = new Dungeon.Player(dungeon, 2, 2, 10);

        // move west and step on the health fountain!
        Dungeon.movePlayer(player, Dungeon.Direction.South);
        assertEquals(40, player.healthPoints);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testEverythingEveryWhere2() {
        // Test case with two dungeons
        // Dungeon1 contains a door and two health fountains.
        // Dungeon2 contains a door, a health fountain, and two traps.

        Dungeon dungeon1 = new Dungeon(6, 4);
        Dungeon dungeon2 = new Dungeon(5, 4);

        dungeon1.putDoor(5,0, dungeon2);
        dungeon1.putHealthFountain(5, 1, 15);
        dungeon1.putHealthFountain(5, 2, 30);

        dungeon2.putDoor(2, 1, dungeon1);
        dungeon2.putHealthFountain(0,3, 20);
        dungeon2.putTrap(1,2);
        dungeon2.putTrap(2,2);

        Dungeon.Player player = new Dungeon.Player(dungeon1, 3, 2, 10);

        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertSame(player.dungeon, dungeon2);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        assertEquals(55, player.healthPoints);

        Dungeon.movePlayer(player, Dungeon.Direction.South);
        Dungeon.movePlayer(player, Dungeon.Direction.South);
        Dungeon.movePlayer(player, Dungeon.Direction.South);
        Dungeon.movePlayer(player, Dungeon.Direction.North);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.East);
        Dungeon.movePlayer(player, Dungeon.Direction.North);

        assertSame(player.dungeon, dungeon1);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        assertEquals(73, player.healthPoints);
    }

    // END STRIP
}
