package oop;


import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicsSolverTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSlot() {
        PhysicsSolver.Slot s = new PhysicsSolver.Slot();
        assertFalse(s.hasValue());
        assertTrue(s.setValue(42));
        assertTrue(s.hasValue());      
        assertEquals(42, s.getValue(), 0.001);
        assertFalse(s.setValue(42));
        s.clearValue();
        assertFalse(s.hasValue());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNoSlotValue() {
        PhysicsSolver.Slot s = new PhysicsSolver.Slot();
        assertThrows(RuntimeException.class, () -> s.getValue());
    }

    // BEGIN STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIncompatibleSlotValue() {
        PhysicsSolver.Slot s = new PhysicsSolver.Slot();
        s.setValue(42);
        assertThrows(RuntimeException.class, () -> s.setValue(44));

    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMultiplication1() {
        PhysicsSolver.Multiplication m = new PhysicsSolver.Multiplication();
        assertFalse(m.getProduct().hasValue());
        assertFalse(m.getFactor1().hasValue());
        assertFalse(m.getFactor2().hasValue());
        assertFalse(m.update());
        assertTrue(m.getFactor1().setValue(3));
        assertFalse(m.update());
        assertTrue(m.getFactor2().setValue(5));
        assertTrue(m.update());
        assertFalse(m.update());

        assertEquals(3.0 * 5.0, m.getProduct().getValue(), 0.001);
        assertEquals(3, m.getFactor1().getValue(), 0.001);
        assertEquals(5, m.getFactor2().getValue(), 0.001);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMultiplication2() {
        PhysicsSolver.Multiplication m = new PhysicsSolver.Multiplication();
        assertTrue(m.getProduct().setValue(3));
        assertFalse(m.update());
        assertTrue(m.getFactor1().setValue(5));
        assertTrue(m.update());
        assertFalse(m.update());

        assertEquals(3, m.getProduct().getValue(), 0.001);
        assertEquals(5, m.getFactor1().getValue(), 0.001);
        assertEquals(3.0 / 5.0, m.getFactor2().getValue(), 0.001);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMultiplication3() {
        PhysicsSolver.Multiplication m = new PhysicsSolver.Multiplication();
        assertTrue(m.getProduct().setValue(7));
        assertFalse(m.update());
        assertTrue(m.getFactor2().setValue(-4));
        assertTrue(m.update());
        assertFalse(m.update());

        assertEquals(7, m.getProduct().getValue(), 0.001);
        assertEquals(-7.0 / 4.0, m.getFactor1().getValue(), 0.001);
        assertEquals(-4, m.getFactor2().getValue(), 0.001);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSquaring1() {
        PhysicsSolver.Squaring m = new PhysicsSolver.Squaring();
        assertFalse(m.getSquare().hasValue());
        assertFalse(m.getNumber().hasValue());
        assertFalse(m.update());
        assertTrue(m.getNumber().setValue(3));
        assertTrue(m.update());
        assertFalse(m.update());

        assertEquals(9, m.getSquare().getValue(), 0.001);
        assertEquals(3, m.getNumber().getValue(), 0.001);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSquaring2() {
        PhysicsSolver.Squaring m = new PhysicsSolver.Squaring();
        assertTrue(m.getSquare().setValue(25));
        assertTrue(m.update());
        assertFalse(m.update());

        assertEquals(25, m.getSquare().getValue(), 0.001);
        assertEquals(5, m.getNumber().getValue(), 0.001);
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testElectricity() {
        // Solve for relations "V = R * I" (Ohm's law) and "P = I * V"
        // (electric power in resistive circuits)
        PhysicsSolver.Slot v = new PhysicsSolver.Slot();
        PhysicsSolver.Slot r = new PhysicsSolver.Slot();
        PhysicsSolver.Slot i = new PhysicsSolver.Slot();
        PhysicsSolver.Slot p = new PhysicsSolver.Slot();
        
        PhysicsSolver.Multiplication ohm = new PhysicsSolver.Multiplication();
        ohm.setProduct(v);
        ohm.setFactor1(r);
        ohm.setFactor2(i);
        
        PhysicsSolver.Multiplication power = new PhysicsSolver.Multiplication();
        power.setProduct(p);
        power.setFactor1(i);
        power.setFactor2(v);

        PhysicsSolver.solve(ohm, power);
        assertFalse(v.hasValue());
        assertFalse(r.hasValue());
        assertFalse(i.hasValue());
        assertFalse(p.hasValue());

        // https://www.build-electronic-circuits.com/ohms-law/
        v.setValue(12);    // V = 12 [V]
        PhysicsSolver.solve(ohm, power);        
        assertEquals(12, v.getValue(), 0.001);
        assertFalse(r.hasValue());
        assertFalse(i.hasValue());
        assertFalse(p.hasValue());

        i.setValue(0.02);  // I = 2 [mA]
        PhysicsSolver.solve(ohm, power);        
        assertEquals(12, v.getValue(), 0.001);
        assertEquals(600, r.getValue(), 0.001);   // R = 600 [ohm]
        assertEquals(0.02, i.getValue(), 0.001);
        assertEquals(0.24, p.getValue(), 0.001);  // P = 24 [W]

        // Example 1 - https://electronicsclub.info/ohmslaw.htm
        ohm.clearValues();
        power.clearValues();
        assertFalse(v.hasValue());
        assertFalse(r.hasValue());
        assertFalse(i.hasValue());
        assertFalse(p.hasValue());
        
        v.setValue(3);
        r.setValue(6);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(0.5, i.getValue(), 0.001);

        // Example 2 - https://electronicsclub.info/ohmslaw.htm
        ohm.clearValues();
        power.clearValues();
        v.setValue(6);
        i.setValue(0.06);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(100, r.getValue(), 0.001);

        // Example 3 - https://electronicsclub.info/ohmslaw.htm
        ohm.clearValues();
        power.clearValues();
        i.setValue(0.2);
        r.setValue(1200);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(240, v.getValue(), 0.001);

        // Example 4 - https://electronicsclub.info/ohmslaw.htm
        ohm.clearValues();
        power.clearValues();
        v.setValue(9);
        r.setValue(15000);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(0.0006, i.getValue(), 0.001);

        // https://www.electronics-tutorials.ws/dccircuits/dcp_2.html
        ohm.clearValues();
        power.clearValues();
        v.setValue(24);
        i.setValue(2);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(12, r.getValue(), 0.001);
        assertEquals(48, p.getValue(), 0.001);

        ohm.clearValues();
        power.clearValues();
        v.setValue(24);
        r.setValue(12);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(2, i.getValue(), 0.001);
        assertEquals(48, p.getValue(), 0.001);

        ohm.clearValues();
        power.clearValues();
        v.setValue(24);
        p.setValue(48);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(2, i.getValue(), 0.001);
        assertEquals(12, r.getValue(), 0.001);
        
        ohm.clearValues();
        power.clearValues();
        r.setValue(12);
        i.setValue(2);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(24, v.getValue(), 0.001);
        assertEquals(48, p.getValue(), 0.001);
        
        ohm.clearValues();
        power.clearValues();
        p.setValue(48);
        i.setValue(2);
        PhysicsSolver.solve(ohm, power);        
        assertEquals(24, v.getValue(), 0.001);
        assertEquals(12, r.getValue(), 0.001);
        
        ohm.clearValues();
        power.clearValues();
        p.setValue(48);
        r.setValue(12);
        PhysicsSolver.solve(ohm, power);
        assertFalse(v.hasValue());
        assertFalse(i.hasValue());

        // Need to add the relation "P = V^2 / R" if "I" and "V" are both unknown
        PhysicsSolver.Squaring squaredVoltage = new PhysicsSolver.Squaring();
        squaredVoltage.setNumber(v);

        PhysicsSolver.Multiplication power2 = new PhysicsSolver.Multiplication();
        power2.setProduct(squaredVoltage.getSquare());
        power2.setFactor1(p);
        power2.setFactor2(r);

        PhysicsSolver.solve(ohm, power, power2, squaredVoltage);
        assertEquals(24, v.getValue(), 0.001);
        assertEquals(2, i.getValue(), 0.001);

        squaredVoltage.clearValues();
        assertFalse(squaredVoltage.getSquare().hasValue());
        assertFalse(v.hasValue());
        assertTrue(p.hasValue());
        assertTrue(r.hasValue());
        assertTrue(i.hasValue());

        power2.clearValues();
        assertFalse(p.hasValue());
        assertFalse(r.hasValue());
        assertTrue(i.hasValue());

        ohm.clearValues();
        assertFalse(i.hasValue());
    }

    // END STRIP
}
