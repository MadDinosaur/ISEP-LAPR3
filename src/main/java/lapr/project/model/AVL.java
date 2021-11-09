/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

/**
 *
 * @author DEI-ESINF
 * @param <E>
 */
public class AVL <E extends Comparable<E>> extends BST<E> {
    
    
    private int balanceFactor(Node<E> node){

        return height(node.getRight()) - height(node.getLeft());

    }
    
    private Node<E> rightRotation(Node<E> node){
      Node<E> leftSon = node.getLeft();
      node.setLeft(leftSon.getRight());
      leftSon.setRight(node);

      return leftSon;
    }
    
    private Node<E> leftRotation(Node<E> node){
        Node<E> rightSon = node.getRight();
        node.setRight(rightSon.getLeft());
        rightSon.setLeft(node);

        return  rightSon;
    }
    
    private Node<E> twoRotations(Node<E> node){

        if (balanceFactor(node) < 0) {
            node.setLeft(leftRotation(node.getLeft()));
            node = rightRotation(node);
        } else {
            node.setRight(rightRotation(node.getRight()));
            node = leftRotation(node);
        }
        return node;
    }
    
    private Node<E> balanceNode(Node<E> node)
    {
        int nodeBF = balanceFactor(node);

        if (node.getLeft() != null && nodeBF < -1) {

            int leftNodeBF = balanceFactor(node.getLeft());

            if (nodeBF * leftNodeBF < 0)
                return twoRotations(node);

            return rightRotation(node);

        }
        if (node.getRight() != null && nodeBF > 1) {

            int rightNodeBF = balanceFactor(node.getRight());

            if (nodeBF * rightNodeBF < 0)
                return twoRotations(node);

            return leftRotation(node);
        }
        return node;
    }
    
    @Override
    public void insert(E element){
        root = insert(element, root);
    }

    private Node<E> insert(E element, Node<E> node) {

        if (node == null) return new Node(element, null, null);

        if (node.getElement() != element) {

            if (element.compareTo(node.getElement()) > 0)
                node.setRight(insert(element, node.getRight()));


            if (element.compareTo(node.getElement()) < 0)
                node.setLeft(insert(element, node.getLeft()));

                node = balanceNode(node);
        }
        return node;
    }
    
    @Override  
    public void remove(E element){
        root = remove(element, root());
    }

    private Node<E> remove(E element, BST.Node<E> node) {

        if (node == null) return null;

        if (node.getElement() == element) {  // Verifica se são iguais

            if (node.getLeft() == null && node.getRight() == null) // Se os filhos forem null significa que ja não há mais
                return null;

            if (node.getLeft() == null) //
                return node.getRight();

            if (node.getRight() == null)
                return node.getLeft();

            E smallElem = smallestElement(node.getRight());
            node.setElement(smallElem);
            node.setRight(remove(smallElem, node.getRight()));
            node = balanceNode(node);


        } else if (node.getElement().compareTo(element) > 0) {
            node.setLeft(remove(element, node.getLeft()));
            node = balanceNode(node);
        } else {
            node.setRight(remove(element, node.getRight()));
            node = balanceNode(node);
        }
        return node;
    }
    
    
    public boolean equals(Object otherObj) {

        if (this == otherObj) 
            return true;

        if (otherObj == null || this.getClass() != otherObj.getClass())
            return false;

        AVL<E> second = (AVL<E>) otherObj;
        return equals(root, second.root);
    }

    public boolean equals(Node<E> root1, Node<E> root2) {
        if (root1 == null && root2 == null) 
           return true;
        else if (root1 != null && root2 != null) {
            if (root1.getElement().compareTo(root2.getElement()) == 0) {
                return equals(root1.getLeft(), root2.getLeft())
                        && equals(root1.getRight(), root2.getRight());
            } else  
                return false; 
        }
        else return false;
    }
   
}