package lapr.project.store;

import lapr.project.model.BST;
import lapr.project.model.Ship;

public class Tree_Ship extends BST<Ship> {

    /**
     * Inserts the elements on the binary tree according to their mmsi values
     * @param element the ship to be introduced
     */
    public void insertMMSI(Ship element){
        root = insertMMSI(element, root());
    }

    /**
     * Inserts the elements on the binary tree according to their mmsi values
     * @param element the ship to be introduced
     * @param node the node in which the search is currently at
     */
    private Node<Ship> insertMMSI(Ship element, Node<Ship> node){
        if (node == null){
            node = new Node<>(element, null, null);
            return node;
        }
        if (node.getElement().getMmsi().compareTo(element.getMmsi()) == 0){
            node.getElement().getPositioningDataList().addPositioningDataList(element.getPositioningDataList().getPositioningDataList());
        } else if (node.getElement().getMmsi().compareTo(element.getMmsi()) > 0){
            node.setLeft(insertMMSI(element, node.getLeft()));
        }else if (node.getElement().getMmsi().compareTo(element.getMmsi()) < 0){
            node.setRight(insertMMSI(element, node.getRight()));
        }
        return node;
    }

    /**
     * Inserts the elements on the binary tree according to their imo values
     * @param element the ship to be introduced
     */
    public void insertIMO(Ship element){
        root = insertIMO(element, root());
    }

    /**
     * Inserts the elements on the binary tree according to their mmsi values
     * @param element the ship to be introduced
     * @param node the node in which the search is currently at
     */
    private Node<Ship> insertIMO(Ship element, Node<Ship> node){
        if (node == null){
            node = new Node<>(element, null, null);
            return node;
        }
        if (node.getElement().getImo() == element.getImo()){
            node.getElement().getPositioningDataList().addPositioningDataList(element.getPositioningDataList().getPositioningDataList());
        } else if (node.getElement().getImo() > element.getImo()){
            node.setLeft(insertIMO(element, node.getLeft()));
        }else if (node.getElement().getImo() < element.getImo()){
            node.setRight(insertIMO(element, node.getRight()));
        }
        return node;
    }

    /**
     * Inserts the elements on the binary tree according to their call sign values
     * @param element the ship to be introduced
     */
    public void insertCallSign(Ship element){
        root = insertCallSign(element, root());
    }

    /**
     * Inserts the elements on the binary tree according to their call sign values
     * @param element the ship to be introduced
     * @param node the node in which the search is currently at
     */
    private Node<Ship> insertCallSign(Ship element, Node<Ship> node){
        if (node == null){
            node = new Node<>(element, null, null);
            return node;
        }
        if (node.getElement().getCallSign().compareTo(element.getCallSign()) == 0){
            node.getElement().getPositioningDataList().addPositioningDataList(element.getPositioningDataList().getPositioningDataList());
        } else if (node.getElement().getCallSign().compareTo(element.getCallSign()) > 0){
            node.setLeft(insertCallSign(element, node.getLeft()));
        }else if (node.getElement().getCallSign().compareTo(element.getCallSign()) < 0){
            node.setRight(insertCallSign(element, node.getRight()));
        }
        return node;
    }

    /**
     * searches the tree to try and find a ship with the same imo value
     * @param imo the imo value to be look after
     * @return a ship with the same imo or null
     */
    public Ship findIMO(int imo){
        return findIMO(imo, root());
    }

    /**
     * searches the tree to try and find a ship with the same imo value
     * @param imo the imo value to be look after
     * @param node the current node being compared
     * @return a ship with the same imo or null
     */
    private Ship findIMO(int imo, Node<Ship> node){
        if (node == null){
            return null;
        }
        if (node.getElement().getImo() == imo){
           return node.getElement();
        } else if (node.getElement().getImo() > imo){
            return findIMO(imo, node.getLeft());
        }else if (node.getElement().getImo() < imo){
            return findIMO(imo, node.getRight());
        }
        return null;
    }

    /**
     * searches the tree to try and find a ship with the same callSign value
     * @param callSign the imo value to be look after
     * @return a ship with the same callSign or null
     */
    public Ship findCallSign(String callSign){
        return findCallSign(callSign, root());
    }

    /**
     * searches the tree to try and find a ship with the same callSign value
     * @param callSign the callSign value to be look after
     * @param node the current node being compared
     * @return a ship with the same callSign or null
     */
    private Ship findCallSign(String callSign, Node<Ship> node){
        if (node == null){
            return null;
        }
        if (node.getElement().getCallSign().compareTo(callSign) == 0){
            return node.getElement();
        } else if (node.getElement().getCallSign().compareTo(callSign) > 0){
            return findCallSign(callSign, node.getLeft());
        }else if (node.getElement().getCallSign().compareTo(callSign) < 0){
            return findCallSign(callSign, node.getRight());
        }
        return null;
    }

    /**
     * searches the tree to try and find a ship with the same mmsi value
     * @param mmsi the mmsi value to be looked after
     * @return a ship with the same mmsi or null
     */
    public Ship findMMSI(String mmsi){
        return findMMSI(mmsi, root());
    }

    /**
     * searches the tree to try and find a ship with the same mmsi value
     * @param mmsi the mmsi value to be look after
     * @param node the current node being compared
     * @return a ship with the same mmsi or null
     */
    private Ship findMMSI(String mmsi, Node<Ship> node){
        if (node == null){
            return null;
        }
        if (node.getElement().getMmsi().compareTo(mmsi) == 0){
            return node.getElement();
        } else if (node.getElement().getMmsi().compareTo(mmsi) > 0){
            return findMMSI(mmsi, node.getLeft());
        }else if (node.getElement().getMmsi().compareTo(mmsi) < 0){
            return findMMSI(mmsi, node.getRight());
        }
        return null;
    }
}
