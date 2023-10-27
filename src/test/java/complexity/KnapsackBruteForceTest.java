package complexity;

import static org.junit.jupiter.api.Assertions.*;

import org.javagrader.Allow;
import org.javagrader.Grade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Timeout;

@Grade
@Allow("all")
public class KnapsackBruteForceTest {

    private KnapsackBruteForce knapsack;
    private KnapsackBruteForce.Item[] items;

    @BeforeEach
    public void setUp() {
        knapsack = new KnapsackBruteForce();
        items = new KnapsackBruteForce.Item[]{
                new KnapsackBruteForce.Item(60, 10),
                new KnapsackBruteForce.Item(100, 20),
                new KnapsackBruteForce.Item(120, 30)
        };
    }

    @Test
    public void testMaxValue() {
        int result = KnapsackBruteForce.knapsack(items, 50);
        assertEquals(220, result);
    }

    @Test
    public void testWithCapacityZero() {
        int result = KnapsackBruteForce.knapsack(items, 0);
        assertEquals(0, result);
    }

    @Test
    public void testWithNoItems() {
        KnapsackBruteForce.Item[] noItems = {};
        int result = KnapsackBruteForce.knapsack(noItems, 50);
        assertEquals(0, result);
    }

    @Test
    public void testWithSingleItem() {
        KnapsackBruteForce.Item[] singleItem = { new KnapsackBruteForce.Item(60, 10) };
        int result = KnapsackBruteForce.knapsack(singleItem, 50);
        assertEquals(60, result);
    }

    @Test
    public void testItemTooHeavy() {
        KnapsackBruteForce.Item[] heavyItem = { new KnapsackBruteForce.Item(200, 100) };
        int result = KnapsackBruteForce.knapsack(heavyItem, 50);
        assertEquals(0, result);
    }
}