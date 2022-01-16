#include "structs.h"
#include "calculateEnergy.h"

#define energyGeneratedPerHour 3600000

char enoughEnergy(container *ptrContainer, int containerNum, int numberGen){
	
	long totalEnergyGenerators = 0;
	long energyForContainers = 0;
	char needsEnergy = 0;
	
	// Calculates the total energy produced with all generators
	totalEnergyGenerators = energyGeneratedPerHour * numberGen;
	
	// Iterates through the pointer, when it find a refrigerated
	//container, it uses calculateEnergy to obtain the energy
	//that container uses, then it adds to a variable that
	//contains the sum of the energy from all the containers
	for(int i=0; i<containerNum; i++){
		if((ptrContainer + i)->refrigerated == 1){
			energyForContainers += calculateEnergy(ptrContainer, i);
		}
	}
	
	// Now it compares the total energy produced by all generators with
	//the energy from all containers, if total energy is smaller than
	//the energy from the containers, than it returns 1 if not it 
	//returns 0
	if(totalEnergyGenerators < energyForContainers){
		needsEnergy = 1;
	}else{
		needsEnergy = 0;	
	}
	
	return needsEnergy;
}
