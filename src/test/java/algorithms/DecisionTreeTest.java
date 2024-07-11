package algorithms;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// BEGIN STRIP
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// END STRIP

@Grade
public class DecisionTreeTest {

// BEGIN STRIP
    private class _CorrectInnerNode extends DecisionTree {
        public DecisionTree left, right;
        public final int featureIdx;

        public _CorrectInnerNode(int featureIdx, DecisionTree left, DecisionTree right) {
            this.left = left;
            this.right = right;
            this.featureIdx = featureIdx;
        }

        @Override
        public boolean predict(boolean [] features) {
            if (features[this.featureIdx])
                return this.left.predict(features);
            return this.right.predict(features);
        }
    }

    private class _CorrectLeaf extends DecisionTree {
        final boolean label;
        
        public _CorrectLeaf(boolean label) {
            this.label = label;
        }

        @Override
        public boolean predict(boolean [] features) {
            return this.label;
        }
    }

    private DecisionTree _correctCons(int featureIndex, DecisionTree left, DecisionTree right) {
        return new _CorrectInnerNode(featureIndex, left, right);
    }

    private DecisionTree _correctDecision(boolean label) {
        return new _CorrectLeaf(label);
    }

    private final Random r = new Random();
    private DecisionTree _currentTree;

    private int _getFeature(Set<Integer> usedFeatures, int max) {
        return new Random().ints(0, max).filter((x) -> !usedFeatures.contains(x)).findFirst().getAsInt();
    }

    private DecisionTree _createTree(int maxDepth, boolean complete) {
        _currentTree = _createTree(maxDepth, 0, new HashSet<Integer>(), complete);
        return _copyTree(_currentTree);
    }

    private DecisionTree _createTree(int maxDepth, int currentDepth, Set<Integer> usedFeatures, boolean complete) {
        double probStop = (double) currentDepth / (double) maxDepth;
        if (probStop == 1.0 || (r.nextDouble() < probStop && !complete)) {
            return _correctDecision(r.nextBoolean());
        }
        int value = _getFeature(usedFeatures, maxDepth);
        usedFeatures.add(value);
        DecisionTree left = _createTree(maxDepth, currentDepth+1, usedFeatures, complete);
        DecisionTree right = _createTree(maxDepth, currentDepth+1, usedFeatures, complete);
        usedFeatures.remove(value);
        return _correctCons(value, left, right);
    }

    private DecisionTree _copyTree(DecisionTree tree) {
        if (tree instanceof _CorrectInnerNode) {
            _CorrectInnerNode n = (_CorrectInnerNode) tree;
            return DecisionTree.splitNode(n.featureIdx, _copyTree(n.left), _copyTree(n.right));
        } else if (tree instanceof _CorrectLeaf) {
            _CorrectLeaf l = (_CorrectLeaf) tree;
            return DecisionTree.decisionNode(l.label);
        } else
            throw new IllegalArgumentException("Can not copy other trees than the correct ones");
    }

    private boolean [] _createFeatures() {
        int size = r.nextInt(10) + 1;
        return _createFeatures(size);
    }

    private boolean [] _createFeatures(int size) {
        boolean [] features = new boolean[size];
        for (int i = 0; i < size; i++)
            features[i] = r.nextBoolean();
        return features;
    }

    private boolean [] _createFeatures(boolean value) {
        int size = r.nextInt(10) + 1;
        boolean [] features = new boolean[size];
        for (int i = 0; i < size; i++)
            features[i] = value;
        return features;
    }
    // END STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

     //      4
     //     / \
     //    2   T
     //   / \
     //  F   T

        DecisionTree left = DecisionTree.splitNode(2,DecisionTree.decisionNode(false),DecisionTree.decisionNode(true));
        DecisionTree dt = DecisionTree.splitNode(4,left,DecisionTree.decisionNode(true));
        boolean [] input1 = new boolean[] {false,true,false,false,true};
        assertTrue(dt.predict(input1));
        boolean [] input2 = new boolean[] {false,true,true,false,false};
        assertTrue(dt.predict(input2));
        boolean [] input3 = new boolean[] {false,true,true,false,true};
        assertFalse(dt.predict(input3));

    }


// BEGIN STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testDecision() {
        DecisionTree True = DecisionTree.decisionNode(true);
        DecisionTree False = DecisionTree.decisionNode(false);
        boolean [] features = _createFeatures();
        assertTrue(True.predict(features),"Un noeud créer avec `decisionNode(true)` ne retourne pas true" );
        assertFalse(False.predict(features),"Un noeud créer avec `decisionNode(false)` ne retourne pas false");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testTrue() {
        boolean [] features = _createFeatures(true);
        DecisionTree tree = _createTree(features.length, false);
        assertEquals(_currentTree.predict(features), tree.predict(features),"La classification n'est pas correcte lorsque toutes les features sont true");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFalse() {
        boolean [] features = _createFeatures(false);
        DecisionTree tree = _createTree(features.length, false);
        assertEquals(_currentTree.predict(features), tree.predict(features),"La classification n'est pas correcte lorsque toutes les features sont false");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testComplete() {
        boolean [] features = _createFeatures();
        DecisionTree tree = _createTree(features.length, true);
        assertEquals(_currentTree.predict(features), tree.predict(features),"La classification n'est pas correcte avec un arbre de décision complet");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRandom() {
        boolean [] features = _createFeatures();
        DecisionTree tree = _createTree(features.length, false);
        assertEquals(_currentTree.predict(features), tree.predict(features),"La classification n'est pas correct avec un arbre quelconque");
    }
// END STRIP

}
