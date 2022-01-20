package lapr.project.controller;

import lapr.project.store.ContainerMaterialStore;
import oracle.ucp.util.Pair;

import java.util.List;

public class CalculateTripEnergyController {
    /**
     * The class responsible for the calculation's
     */
    private final ContainerMaterialStore containerMaterialStore;

    /**
     * Fills the instance with the materials values
     */
    public CalculateTripEnergyController(){
        containerMaterialStore = new ContainerMaterialStore();
    }

    /**
     * Calculates the energy needed for the whole trip
     * @param numRef number of refrigerated containers
     * @param numNonRef number of non-refrigerated containers
     * @param baseTemp the voyage's information, on the first integer is the section's time and on the second is it's temperature
     * @param refRes the resistivity of refrigerated containers
     * @param nonRefRes the resistivity of non-refrigerated containers
     * @return the voyages energy needed to keep the containers in temperature
     */
    public double getEnergy(int numRef, int numNonRef, List<Pair<Integer, Integer>> baseTemp, double refRes, double nonRefRes){
        return containerMaterialStore.tripEnergy(numRef, numNonRef, baseTemp, refRes, nonRefRes);
    }
}
