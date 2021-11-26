package lapr.project.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public void insert(List<Node<T>> values) {
        if (values == null) {
            return;
        }
        root = insert(values, true);
    }

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

    public List<T> inOrder(){
        List<T> snapshot = new ArrayList<>();
        if (root!=null)
            inOrderSubtree(root, snapshot);   // fill the snapshot recursively
        return snapshot;
    }

    private void inOrderSubtree(Node<T> node, List<T> snapshot) {
        if (node == null)
            return;
        inOrderSubtree(node.left, snapshot);
        snapshot.add(node.element);
        inOrderSubtree(node.right, snapshot);
    }
}