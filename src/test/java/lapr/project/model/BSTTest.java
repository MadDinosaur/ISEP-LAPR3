package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BSTTest {
    Integer[] arr = {20,15,10,13,8,17,40,50,30,7};
    int[] height={0,1,2,3,3,3,3,3,3,4};
    Integer[] inorderT= {7,8,10,13,15,17,20,30,40,50};
    Integer[] preorderT= {20, 15, 10, 8, 7, 13, 17, 40, 30, 50};
    Integer[] posorderT = {7, 8, 13, 10, 17, 15, 30, 50, 40, 20};

    BST<Integer> instance;

    public BSTTest() {
    }

    @BeforeEach
    public void setUp(){
        instance = new BST();
        for(int i :arr)
            instance.insert(i);
    }

    @Test
    public void testFind() {
        System.out.println("find");
        assertEquals(instance.find(instance.root(), 7).getElement(), 7);
        assertEquals(instance.find(instance.root(), 50).getElement(), 50);
    }

    @Test
    public void testFind2(){
        assertEquals(instance.find(instance.root(), 30).getElement(), 30);
        assertEquals(instance.find(instance.root(), 40).getElement(), 40);
        assertEquals(instance.find(instance.root(), 10).getElement(), 10);
        assertEquals(instance.find(instance.root(), 8).getElement(), 8);
        assertEquals(instance.find(instance.root(), 20).getElement(), 20);
        assertNotEquals(instance.find(instance.root(), 15).getElement(), 40);
        assertNotEquals(instance.find(instance.root(), 20).getElement(), null);
        assertNotNull(instance.toString());
    }

    /**
     * Test of size method, of class BST.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        assertEquals(instance.size(), arr.length);

        BST<String> sInstance = new BST();
        assertEquals(sInstance.size(), 0);
        sInstance.insert("A");
        assertEquals(sInstance.size(), 1);
        sInstance.insert("B");
        assertEquals(sInstance.size(), 2);
        sInstance.insert("A");
        assertEquals(sInstance.size(), 2);
    }

    /**
     * Test of insert method, of class BST.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        int arr[] = {20,15,10,13,8,17,40,50,30,20,15,10};
        BST<Integer> instance = new BST();
        for (int i=0; i<9; i++){            //new elements
            instance.insert(arr[i]);
            assertEquals(instance.size(), i+1);
        }
        for(int i=9; i<arr.length; i++){    //duplicated elements => same size
            instance.insert(arr[i]);
            assertEquals(instance.size(), 9);
        }
    }
    /**
     * Test of remove method, of class BST.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");

        BST<String> sInstance = new BST();
        sInstance.remove("1");

        int qtd=arr.length;
        instance.remove(999);

        assertEquals(instance.size(), qtd);
        for (int i=0; i<arr.length; i++){
            instance.remove(arr[i]);
            qtd--;
            assertEquals(qtd,instance.size());
        }

        instance.remove(999);
        assertEquals(0,instance.size());
    }
    /**
     * Test of isEmpty method, of class BST.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isempty");

        assertFalse(instance.isEmpty());
        instance = new BST();
        assertTrue(instance.isEmpty());

        instance.insert(11);
        assertFalse(instance.isEmpty());

        instance.remove(11);
        assertTrue(instance.isEmpty());
    }
    /**
     * Test of height method, of class BST.
     */
    @Test
    public void testHeight() {
        System.out.println("height");

        instance = new BST();
        assertEquals(instance.height(), -1);
        for(int idx=0; idx<arr.length; idx++){
            instance.insert(arr[idx]);
            assertEquals(instance.height(), height[idx]);
        }
        instance = new BST();
        assertEquals(instance.height(), -1);
    }
    /**
     * Test of smallestelement method, of class TREE.
     */
    @Test
    public void testSmallestElement() {
        System.out.println("smallestElement");
        assertEquals(new Integer(7), instance.smallestElement());
        instance.remove(7);
        assertEquals(new Integer(8), instance.smallestElement());
        instance.remove(8);
        assertEquals(new Integer(10), instance.smallestElement());
    }
    /**
     * Test of processBstByLevel method, of class TREE.
     */
    @Test
    public void testProcessBstByLevel() {
        System.out.println("processbstbylevel");
        Map<Integer, List<Integer>> expResult = new HashMap();
        expResult.put(0, Arrays.asList(new Integer[]{20}));
        expResult.put(1, Arrays.asList(new Integer[]{15,40}));
        expResult.put(2, Arrays.asList(new Integer[]{10,17,30,50}));
        expResult.put(3, Arrays.asList(new Integer[]{8,13}));
        expResult.put(4, Arrays.asList(new Integer[]{7}));

        Map<Integer,List<Integer>> result = instance.nodesByLevel();

        for(Map.Entry<Integer,List<Integer>> e : result.entrySet())
            assertEquals(expResult.get(e.getKey()), e.getValue());
    }


    /**
     * Test of inOrder method, of class BST.
     */
    @Test
    public void testInOrder() {
        BST<String> sInstance = new BST();
        instance.inOrder();

        System.out.println("inOrder");
        List<Integer> lExpected = Arrays.asList(inorderT);
        assertEquals(lExpected, instance.inOrder());
    }
    /**
     * Test of preOrder method, of class BST.
     */
    @Test
    public void testpreOrder() {
        BST<String> sInstance = new BST();
        assertEquals(sInstance.preOrder().spliterator().estimateSize(), 0);
        instance.preOrder();

        System.out.println("preOrder");
        List<Integer> lExpected = Arrays.asList(preorderT);
        assertEquals(lExpected, instance.preOrder());
    }
    /**
     * Test of posOrder method, of class BST.
     */
    @Test
    public void testposOrder() {
        BST<String> sInstance = new BST();
        assertEquals(sInstance.posOrder().spliterator().estimateSize(), 0);
        instance.posOrder();


        System.out.println("posOrder");
        List<Integer> lExpected = Arrays.asList(posorderT);
        assertEquals(lExpected, instance.posOrder());
    }

    @Test
    public void nullValuesTest(){
        BST<String> sInstance = new BST();
        sInstance.nodesByLevel();

        assertEquals(instance.toString(), instance.toString());
        assertEquals(sInstance.toString(), "");
    }
}