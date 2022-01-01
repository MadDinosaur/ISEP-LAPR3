package lapr.project.controller;

import lapr.project.store.ContainerMaterialStore;

public class CalculateContainerResistivityController {

    /**
     * the materials used for the container
     */
    private final ContainerMaterialStore containerMaterialStore;

    /**
     * Fills the instance with the materials values
     */
    public CalculateContainerResistivityController(){
        containerMaterialStore = new ContainerMaterialStore();
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
        return containerMaterialStore.getResistivity(external, median, internal, externalWidth, medianWidth, internalWidth, area);
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
        return containerMaterialStore.getResistivityByArea(external, median, internal, externalWidth, medianWidth, internalWidth);
    }
}
