package lapr.project.model;

import java.util.*;
import java.awt.geom.Point2D;

public class BST2DTree<T> {

    public class Node<T> {
        protected Point2D.Double coords;
        protected T element;
        protected Node<T> left;
        protected Node<T> right;

        public Node(T e, double x, double y) {
            element = e;
            this.coords = new Point2D.Double(x, y);
        }

        public double getX() {
            return coords.getX();
        }

        public double getY() {
            return coords.getY();
        }

        public void setObject(Node<T> node) {
            element = node.element;
            coords = node.coords;
            left = node.left;
            right = node.right;
        }
    }


    private final Comparator<Node<T>> cmpX = new Comparator<Node<T>>() {
        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getX(), p2.getX());
        }
    };
    private final Comparator<Node<T>> cmpY = new Comparator<Node<T>>() {
        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getY(), p2.getY());
        }
    };
    private Node<T> root;

    public void insert(T info, double x, double y) {
        if (root == null) {
            root = new Node<>(info, x, y);
            return;
        }
        insert(root, new Node<>(info, x, y), true);
    }

    private void insert(Node<T> currentNode, Node<T> node, boolean divX) {
        if (node.coords.equals(currentNode.coords))
            return;
        int cmpResult = (divX ? cmpX : cmpY).compare(node, currentNode);
        if (cmpResult < 0)
            if (currentNode.left == null)
                currentNode.left = node;
            else
                insert(currentNode.left, node, !divX);
        else if (currentNode.right == null)
            currentNode.right = node;
        else
            insert(currentNode.right, node, !divX);
    }

    public T findNearestNeighbour(double x, double y) {
        if (root == null)
            return null;
        return findNearestNeighbour(root, x, y, root, true);
    }

    private T findNearestNeighbour(Node<T> node, double x, double y, Node<T> closestNode, boolean divX) {
        if (node == null)
            return null;
        double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);
        double closestDist = Point2D.distanceSq(closestNode.coords.x,
                closestNode.coords.y, x, y);
        if (closestDist > d) {
            closestNode.setObject(node);
        }
        double delta = divX ? x - node.coords.x : y - node.coords.y;
        double delta2 = delta * delta;
        Node<T> node1 = delta < 0 ? node.left : node.right;
        Node<T> node2 = delta < 0 ? node.right : node.left;
        findNearestNeighbour(node1, x, y, closestNode, !divX);
        if (delta2 < closestDist){
            findNearestNeighbour(node2, x, y, closestNode,!divX);
        }
        return closestNode.element;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();
    }

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
}