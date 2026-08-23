package parallelization;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Grade
@Allow("java.lang.Thread")
public class LeagueOfLegendsTest {

    private static class TestChampion implements LeagueOfLegends.Champion {
        private final String name;
        private final double armor;
        private final double magicResistance;

        public TestChampion(String name, double armor, double magicResistance) {
            this.name = name;
            this.armor = armor;
            this.magicResistance = magicResistance;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double getArmor() {
            return armor;
        }

        @Override
        public double getMagicResistance() {
            return magicResistance;
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPhysicalDamageNoArmor() {
        TestChampion champion = new TestChampion("A", 0, 0);
        long result = LeagueOfLegends.computePhysicalDamage(champion, 200);
        assertEquals(200, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPhysicalDamageWithArmor() {
        TestChampion champion = new TestChampion("A", 100, 0);
        long result = LeagueOfLegends.computePhysicalDamage(champion, 200);
        assertEquals(100, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMagicalDamageNoResistance() {
        TestChampion champion = new TestChampion("A", 0, 0);
        long result = LeagueOfLegends.computeMagicalDamage(champion, 300);
        assertEquals(300, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMagicalDamageWithResistance() {
        TestChampion champion = new TestChampion("A", 0, 100);
        long result = LeagueOfLegends.computeMagicalDamage(champion, 300);
        assertEquals(150, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNegativePhysicalDamage() {
        TestChampion champion = new TestChampion("A", 50, 50);
        assertThrows(
                IllegalArgumentException.class,
                () -> LeagueOfLegends.computePhysicalDamage(champion, -10)
        );
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNegativeMagicalDamage() {
        TestChampion champion = new TestChampion("A", 50, 50);
        assertThrows(
                IllegalArgumentException.class,
                () -> LeagueOfLegends.computeMagicalDamage(champion, -10)
        );
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testTotalDamage() {
        TestChampion champion = new TestChampion("A", 100, 100);
        long result = new LeagueOfLegends.Attack(200, 300).computeTotalDamage(champion);
        assertEquals(250, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequentialAllChampions() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 0, 0),
                new TestChampion("B", 100, 100),
                new TestChampion("C", 300, 300)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        long result = LeagueOfLegends.computeBossTotalDamageSequential(champions, predicate, attack, 0, champions.length);

        // A: 200 + 200 = 400
        // B: 100 + 100 = 200
        // C: 50 + 50 = 100
        assertEquals(700, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequentialWithPredicate() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 50, 50),
                new TestChampion("B", 100, 100),
                new TestChampion("C", 300, 900)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> c.getArmor() >= 100;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 150);

        long result = LeagueOfLegends.computeBossTotalDamageSequential(champions, predicate, attack, 0, champions.length);

        // B: 100 + 75 = 175
        // C: 50 + 15 = 65
        assertEquals(240, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequentialSubArray() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 0, 0),
                new TestChampion("B", 100, 100),
                new TestChampion("C", 300, 300)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        long result = LeagueOfLegends.computeBossTotalDamageSequential(champions, predicate, attack, 1, 3);

        // seulement B et C
        assertEquals(300, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequentialEmptyRange() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 0, 0)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        long result = LeagueOfLegends.computeBossTotalDamageSequential(champions, predicate, attack, 1, 1);

        assertEquals(0, result);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequentialInvalidIndices() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 0, 0)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(100, 100);

        assertThrows(
                IllegalArgumentException.class,
                () -> LeagueOfLegends.computeBossTotalDamageSequential(champions, predicate, attack, -1, 1));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testParallel() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            LeagueOfLegends.Champion[] champions = {
                    new TestChampion("A", 0, 0),
                    new TestChampion("B", 100, 100),
                    new TestChampion("C", 300, 300),
                    new TestChampion("D", 100, 100)
            };

            Predicate<LeagueOfLegends.Champion> predicate = c -> true;

            LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

            long result = LeagueOfLegends.computeBossTotalDamageParallel(champions, predicate, attack, executor);

            // 400 + 200 + 100 + 200
            assertEquals(900, result);

        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testParallelWithPredicate() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            LeagueOfLegends.Champion[] champions = {
                    new TestChampion("A", 0, 0),
                    new TestChampion("B", 100, 100),
                    new TestChampion("C", 300, 300),
                    new TestChampion("D", 50, 50)
            };

            Predicate<LeagueOfLegends.Champion> predicate = c -> c.getArmor() >= 100;

            LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

            long result = LeagueOfLegends.computeBossTotalDamageParallel(champions, predicate, attack, executor);

            // B = 200
            // C = 100
            assertEquals(300, result);

        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMostResistantUniqueMinimum() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 0, 0),
                new TestChampion("B", 100, 100),
                new TestChampion("C", 300, 300)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("C", result.get(0).getName());
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }


    private static Set<String> getNames(List<LeagueOfLegends.Champion> champions) {
        assertNotNull(champions);
        Set<String> s = new HashSet<>();
        for (LeagueOfLegends.Champion c : champions) {
            assertFalse(s.contains(c.getName()));
            s.add(c.getName());
        }
        return s;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMostResistantDuplicateMinimum() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 300, 300),
                new TestChampion("B", 300, 300),
                new TestChampion("C", 100, 100),
                new TestChampion("D", 0, 0)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            Set<String> names = getNames(result);
            // A et B reçoivent chacun 100 dégâts -> valeur dupliquée
            assertEquals(2, names.size());
            assertTrue(names.contains("A"));
            assertTrue(names.contains("B"));
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMostResistantSeveralDuplicateLevels() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 300, 300),
                new TestChampion("B", 300, 300),
                new TestChampion("C", 100, 100),
                new TestChampion("D", 100, 100),
                new TestChampion("E", 0, 0)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            Set<String> names = getNames(result);
            assertEquals(2, names.size());
            assertTrue(names.contains("A"));
            assertTrue(names.contains("B"));
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMostResistantAllValuesDuplicated() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 100, 300),
                new TestChampion("B", 100, 300),
                new TestChampion("C", 100, 300),
                new TestChampion("D", 100, 300)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            Set<String> names = getNames(result);
            assertEquals(4, names.size());
            assertTrue(names.contains("A"));
            assertTrue(names.contains("B"));
            assertTrue(names.contains("C"));
            assertTrue(names.contains("D"));
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMostResistantNoChampionMatchesPredicate() {
        LeagueOfLegends.Champion[] champions = {
                new TestChampion("A", 300, 300),
                new TestChampion("B", 100, 100)
        };

        Predicate<LeagueOfLegends.Champion> predicate =
                c -> c.getArmor() > 1000;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            assertNotNull(result);
            assertTrue(result.isEmpty());
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testDuplicateMinimumAcrossTwoHalves() {
        LeagueOfLegends.Champion[] champions = {
                // première moitié
                new TestChampion("A", 300, 300),
                new TestChampion("B", 100, 100),

                // deuxième moitié
                new TestChampion("C", 300, 300),
                new TestChampion("D", 50, 50)
        };

        Predicate<LeagueOfLegends.Champion> predicate = c -> true;

        LeagueOfLegends.Attack attack = new LeagueOfLegends.Attack(200, 200);

        Consumer<List<LeagueOfLegends.Champion>> check = (result) -> {
            Set<String> names = getNames(result);
            assertEquals(2, names.size());
            assertTrue(names.contains("A"));
            assertTrue(names.contains("C"));
        };

        check.accept(LeagueOfLegends.findMostResistentChampionsSequential(champions, predicate, attack, 0, champions.length));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            check.accept(LeagueOfLegends.findMostResistentChampionsParallel(champions, predicate, attack, executor));
        } finally {
            executor.shutdown();
        }
    }
}
