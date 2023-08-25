package oop;// This file must *not* be modified!

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalWarmingTest {
    static final float THRESHOLD = 0.000001f;

    private static class Observer implements GlobalWarming.RecordObserver {
        private int countRecords;
        private float temperature;
        private String place;

        Observer(String place) {
            this.place = place;
        }

        @Override
        public void signalNewRecord(String place, float temperature) {
            if (place.equals(this.place)) {
                countRecords += 1;
                this.temperature = temperature;
            }
        }

        public int getCountRecords() {
            return countRecords;
        }

        public float getRecord() {
            if (countRecords > 0) {
                return temperature;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testSimple() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();

        Observer paris = new Observer("Paris");
        assertEquals(0, paris.getCountRecords());
        database.addObserver(paris);

        Observer london = new Observer("London");
        assertEquals(0, london.getCountRecords());
        database.addObserver(london);

        database.temperatureMeasured("Paris", 15);
        assertEquals(1, paris.getCountRecords());
        assertEquals(0, london.getCountRecords());
        assertEquals(15, paris.getRecord(), THRESHOLD);

        database.temperatureMeasured("London", 25);
        assertEquals(1, paris.getCountRecords());
        assertEquals(1, london.getCountRecords());
        assertEquals(15, paris.getRecord(), THRESHOLD);
        assertEquals(25, london.getRecord(), THRESHOLD);

        database.temperatureMeasured("Paris", 10);
        assertEquals(1, paris.getCountRecords());
        assertEquals(1, london.getCountRecords());
        assertEquals(15, paris.getRecord(), THRESHOLD);
        assertEquals(25, london.getRecord(), THRESHOLD);

        database.temperatureMeasured("London", 10);
        assertEquals(1, paris.getCountRecords());
        assertEquals(1, london.getCountRecords());
        assertEquals(15, paris.getRecord(), THRESHOLD);
        assertEquals(25, london.getRecord(), THRESHOLD);

        database.temperatureMeasured("London", 30);
        assertEquals(1, paris.getCountRecords());
        assertEquals(2, london.getCountRecords());
        assertEquals(15, paris.getRecord(), THRESHOLD);
        assertEquals(30, london.getRecord(), THRESHOLD);

        database.temperatureMeasured("Paris", 28);
        assertEquals(2, paris.getCountRecords());
        assertEquals(2, london.getCountRecords());
        assertEquals(28, paris.getRecord(), THRESHOLD);
        assertEquals(30, london.getRecord(), THRESHOLD);
    }
    
    // BEGIN STRIP
    
    private void checkSame(Observer [] observers, float expectedTemperature, int expectedCount) {
        for (Observer observer : observers) {
            assertEquals(expectedTemperature, observer.getRecord(), THRESHOLD);
            assertEquals(expectedCount, observer.getCountRecords());
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testMultipleObserverSamePlace() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();

        Observer [] paris = new Observer[5];
        for (int i = 0; i < 5; i++) {
            paris[i] = new Observer("Paris");
            database.addObserver(paris[i]);
        }

        Observer [] bogota = new Observer[5];
        for (int i = 0; i < 5; i++) {
            bogota[i] = new Observer("Bogota");
            database.addObserver(bogota[i]);
        }
        
        database.temperatureMeasured("Bogota", 24);
        
        checkSame(bogota, 24.0f, 1);
        
        database.temperatureMeasured("Bogota", 12);
        checkSame(bogota, 24.0f, 1);
        
        database.temperatureMeasured("Paris", 30);
        checkSame(paris, 30.0f, 1);
        checkSame(bogota, 24.0f, 1);
    }
    
    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testSameTemperatureDoNotUpdate() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();
        Observer bruxelles = new Observer("Bruxelles");
        Observer paris = new Observer("Paris");
        Observer london = new Observer("London");
        database.addObserver(bruxelles);
        database.addObserver(paris);
        database.addObserver(london);
        
        database.temperatureMeasured("Bruxelles", 24.0f);
        assertEquals(24, bruxelles.getRecord(), THRESHOLD);
        assertEquals(1, bruxelles.getCountRecords());
        
        database.temperatureMeasured("Paris", 1.0f);
        database.temperatureMeasured("Bruxelles", 24.0f);
        assertEquals(24, bruxelles.getRecord(), THRESHOLD);
        assertEquals(1.0, paris.getRecord(), THRESHOLD);
        assertEquals(1, bruxelles.getCountRecords());
        assertEquals(1, paris.getCountRecords());

        database.temperatureMeasured("London", 13.0f);
        database.temperatureMeasured("Paris", 1.0f);
        database.temperatureMeasured("Bruxelles", 24.0f);
        assertEquals(24, bruxelles.getRecord(), THRESHOLD);
        assertEquals(1.0, paris.getRecord(), THRESHOLD);
        assertEquals(13.0, london.getRecord(), THRESHOLD);
        assertEquals(1, bruxelles.getCountRecords());
        assertEquals(1, paris.getCountRecords());
        assertEquals(1, london.getCountRecords());
    }
    
    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testNegativeMaximum() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();
        Observer bruxelles = new Observer("Bruxelles");
        database.addObserver(bruxelles);
        
        database.temperatureMeasured("Bruxelles", -10.2f);
        assertEquals(-10.2f, bruxelles.getRecord(), THRESHOLD);
        assertEquals(1, bruxelles.getCountRecords());
        
        database.temperatureMeasured("Bruxelles", -5.3f);
        assertEquals(-5.3f, bruxelles.getRecord(), THRESHOLD);
        assertEquals(2, bruxelles.getCountRecords());
    }
    
    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testLargeNumberObservers() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();
        Observer [] manyObservers = new Observer[1000];
        for (int i = 0; i < manyObservers.length; i++) {
            manyObservers[i] = new Observer("LLN");
            database.addObserver(manyObservers[i]);
        }
        database.temperatureMeasured("LLN", 8.0f);
        database.temperatureMeasured("LLN", 10.0f);
        database.temperatureMeasured("LLN", 5.0f);  // Not a record

        for (int i = 0; i < manyObservers.length; i++) {
            assertEquals(2, manyObservers[i].getCountRecords());
            assertEquals(10.0f, manyObservers[i].getRecord(), THRESHOLD);
        }
    }

    /**
     * The following test was added after the exam, as many students
     * had not understood how to deal with multiple cities. This test
     * corresponds to 28% of the marks.
     **/
    @Test
    @Grade(value = 2, cpuTimeout=100)
    public void testOneCity() {
        GlobalWarming.IPCCDatabase database = new GlobalWarming.IPCCDatabase();

        Observer a = new Observer("city");
        assertEquals(0, a.getCountRecords());
        database.addObserver(a);

        Observer b = new Observer("city");
        assertEquals(0, b.getCountRecords());
        database.addObserver(b);

        Observer c = new Observer("city");
        assertEquals(0, c.getCountRecords());
        database.addObserver(c);

        database.temperatureMeasured("city", 15);
        assertEquals(1, a.getCountRecords());
        assertEquals(1, b.getCountRecords());
        assertEquals(1, c.getCountRecords());
        assertEquals(15, a.getRecord(), THRESHOLD);
        assertEquals(15, b.getRecord(), THRESHOLD);
        assertEquals(15, c.getRecord(), THRESHOLD);

        database.temperatureMeasured("city", 10);
        assertEquals(1, a.getCountRecords());
        assertEquals(1, b.getCountRecords());
        assertEquals(1, c.getCountRecords());
        assertEquals(15, a.getRecord(), THRESHOLD);
        assertEquals(15, b.getRecord(), THRESHOLD);
        assertEquals(15, c.getRecord(), THRESHOLD);

        database.temperatureMeasured("city", 25);
        assertEquals(2, a.getCountRecords());
        assertEquals(2, b.getCountRecords());
        assertEquals(2, c.getCountRecords());
        assertEquals(25, a.getRecord(), THRESHOLD);
        assertEquals(25, b.getRecord(), THRESHOLD);
        assertEquals(25, c.getRecord(), THRESHOLD);

        database.temperatureMeasured("city", 10);
        assertEquals(2, a.getCountRecords());
        assertEquals(2, b.getCountRecords());
        assertEquals(2, c.getCountRecords());
        assertEquals(25, a.getRecord(), THRESHOLD);
        assertEquals(25, b.getRecord(), THRESHOLD);
        assertEquals(25, c.getRecord(), THRESHOLD);
    }

    // END STRIP
}
