package lapr.project.store;

import lapr.project.exception.IllegalContainerException;
import oracle.ucp.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContainerMaterialStore {

    /**
     * The map with the materials and it's heat conductivity
     */
    HashMap<String, Double> materials = new HashMap<>();

    /**
     * Statically fills up the map
     */
    public ContainerMaterialStore(){
        materials.put("Stone Wool", 0.038);
        materials.put("Cork", 0.04);
        materials.put("Fiber-glass", 0.046);
        materials.put("Steel", 45.0);
        materials.put("Iron", 55.0);
        materials.put("Zinc", 110.0);
        materials.put("Aluminium", 239.0);
    }

    /**
     * returns the container's Resistivity by area
     * @param external the external material
     * @param median the middle material
     * @param internal the internal material
     * @param externalWidth the external Width
     * @param medianWidth the middle Width
     * @param internalWidth the internal Width
     * @return return the resistivity of the material by area
     */
    public double getResistivityByArea(String external, String median, String internal, double externalWidth, double medianWidth, double internalWidth){
        double externalK;
        double medianK;
        double internalK;
        if (!materials.containsKey(external))
            throw new IllegalContainerException("there's no such material: " + external);
        if (!materials.containsKey(median))
            throw new IllegalContainerException("there's no such material: " + median);
        if (!materials.containsKey(internal))
            throw new IllegalContainerException("there's no such material: " + internal);
        externalK = materials.get(external);
        medianK = materials.get(median);
        internalK = materials.get(internal);
        return externalWidth/externalK + medianWidth/medianK + internalWidth/internalK;
    }

    /**
     * returns the container's Resistivity by area
     * @param external the external material
     * @param median the middle material
     * @param internal the internal material
     * @param externalWidth the external Width
     * @param medianWidth the middle Width
     * @param internalWidth the internal Width
     * @param area the containers surface area
     * @return return the resistivity of the material by area
     */
    public double getResistivity(String external, String median, String internal, double externalWidth, double medianWidth, double internalWidth, double area){
        return getResistivityByArea(external, median, internal, externalWidth, medianWidth, internalWidth)/area;
    }
}
