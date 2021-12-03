package lapr.project.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tree2D<T> {
    public static class Node<T> {
        protected Point2D.Double coords;
        protected T element;
        protected Node<T> left;
        protected Node<T> right;

        /**
         *
         * @param e An element stored at this node
         * @param x The element's x coordinate
         * @param y The element's y coordinate
         */
        public Node(T e, double x, double y) {
            element = e;
            this.coords = new Point2D.Double(x, y);
        }

        /**
         * Gets the element's x coordinate
         * @return returns the element's x coordinate
         */
        public double getX() {
            return coords.getX();
        }

        /**
         * Gets the element's y coordinate
         * @return returns the element's y coordinate
         */
        public double getY() {
            return coords.getY();
        }

        /**
         *
         * @param node The node to be copied
         */
        public void setObject(Node<T> node) {
            this.element = node.element;
            this.coords = node.coords;
            this.left = node.left;
            this.right = node.right;
        }
        
    }

    /**
     * The comparator for the element's x coordinate
     */
    private final Comparator<Node<T>> cmpX = Comparator.comparingDouble(Node::getX);

    /**
     * The comparator for the element's y coordinate
     */
    private final Comparator<Node<T>> cmpY = Comparator.comparingDouble(Node::getY);
    private Node<T> root;

    /**
     * Adds a list of elements to tree
     * @param values the list that will be added
     */
    public void insert(List<Node<T>> values) {
        if (values == null)
            return;

        root = insert(values, true);
    }

    /**
     * Adds a list of nodes to the tree
     * @param values The list of values to be added
     * @param divX True if the comparator to be used is the X, False if the comparator to be used is the Y
     * @return returns a node
     */
    private Node<T> insert(List<Node<T>> values, boolean divX) {
        if (values.size() == 0)
            return null;

        values.sort(divX ? cmpX : cmpY);

        int middlePoint = values.size() /2 + 1;

        Node<T> node = values.get(middlePoint - 1);

        node.right = insert(values.subList(middlePoint, values.size()), !divX);
        node.left = insert(values.subList(0, middlePoint - 1), !divX);

        return node;
    }

    /**
     * Gets the nearest element to that coordinates
     * @param x The element's X coordinate
     * @param y The element's Y coordinate
     * @return returns the closest element
     */
    public T findNearestNeighbour(double x, double y) {
        if (root == null)
            return null;

        return findNearestNeighbour(root, x, y, new Node<T>(null, 0, 0), true);
    }

    /**
     * Gets the nearest element to that coordinates
     * @param node A node
     * @param x The element's x coordinate
     * @param y The element's y coordinate
     * @param closestNode The closest node
     * @param divX True if the comparator to be used is the X, False if the comparator to be used is the Y
     * @return returns the closest element
     */
    private T findNearestNeighbour(Node<T> node, double x, double y, Node<T> closestNode, boolean divX) {
        if (node == null)
            return null;

        double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);
        double closestDist = Point2D.distanceSq(closestNode.coords.x, closestNode.coords.y, x, y);

        if (closestDist > d)
            closestNode.setObject(node);

        double delta = divX ? x - node.coords.x : y - node.coords.y;
        double delta2 = delta * delta;

        Node<T> node1 = delta < 0 ? node.left : node.right;
        Node<T> node2 = delta < 0 ? node.right : node.left;

        findNearestNeighbour(node1, x, y, closestNode, !divX);

        if (delta2 < closestDist)
            findNearestNeighbour(node2, x, y, closestNode,!divX);

        return closestNode.element;
    }

    /**
     * To string method
     * @return a string of the element
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);

        return sb.toString();
    }

    /**
     * To string recursive method
     * @param root the tree's root
     * @param level the current level
     * @param sb the string builder
     */
    private void toStringRec(Node<T> root, int level, StringBuilder sb){
        if(root==null)
            return;

        toStringRec(root.right, level+1, sb);

        if (level!=0){
            for(int i=0;i<level-1;i++)
                sb.append("|\t");

            sb.append("|-------"+root.element +"\n");
        }
        else
            sb.append(root.element +"\n");

        toStringRec(root.left, level+1, sb);
    }

    /**
     * A method that returns the tree with all of its balanced factor in a tree
     * @return a string of the element
     */
    public String balanceFactor(){
        StringBuilder sb = new StringBuilder();
        balanceFactor(root, 0, sb);

        return sb.toString();
    }

    /**
     * A method that shows the balance factor of the whole tree
     * @param root the tree's root
     * @param level the current level
     * @param sb the string builder
     */
    private void balanceFactor(Node<T> root, int level, StringBuilder sb){
        if(root==null)
            return;

        balanceFactor(root.right, level+1, sb);
        int balance = height(root.right) - height(root.left);

        if (level!=0){
            for(int i=0;i<level-1;i++)
                sb.append("|\t");

            sb.append("|------>"+balance +"\n");
        }
        else
            sb.append(balance +"\n");

        balanceFactor(root.left, level+1, sb);
    }

    /**
     * Returns the size of the tree
     * @return size of the tree
     */
    public int size(){
        return size(root);
    }

    /**
     * returns the size of a given node
     * @param node the node being analyzed
     * @return the size of the node
     */
    private int size(Node<T> node){
        if (node == null)
            return 0;

        return size(node.left) + size(node.right) + 1;
    }

    /**
     * Returns a list of elements
     * @return returns a list of elements
     */
    public List<T> inOrder(){
        List<T> snapshot = new ArrayList<>();

        if (root!=null)
            inOrderSubtree(root, snapshot);   // fill the snapshot recursively

        return snapshot;
    }

    /**
     * Returns a list of elements using recursion
     * @param node the current node
     * @param snapshot the list
     */
    private void inOrderSubtree(Node<T> node, List<T> snapshot) {
        if (node == null)
            return;

        inOrderSubtree(node.left, snapshot);
        snapshot.add(node.element);
        inOrderSubtree(node.right, snapshot);
    }

    /**
     * return the height of any given node
     * @param node the node in question
     * @return the height of a node
     */
    protected int height(Node<T> node){
        if (node == null)
            return -1;

        if (node.left == null && node.right == null)
            return 0;

        int leftChild = height(node.left);
        int rightChild = height(node.right);

        if (leftChild > rightChild) {
            return leftChild + 1;
        } else {
            return rightChild + 1;
        }
    }
}