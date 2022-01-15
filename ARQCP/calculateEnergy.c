#include "structs.h"

#define outsideTemperature 20
#define refrigeratedTemperature -5
#define nonRefrigeratedTemperature 7

int calculateEnergy(container *ptrContainer, int pos){
	ptrContainer += pos;
	
	long energy = 0;
	double area = 0;
	double resistivity = 0;
	int temperature = 0;
	
	//Calculates the temperature diference
	if (ptrContainer->refrigerated == 1)
		temperature = outsideTemperature - refrigeratedTemperature;
	else
		temperature =  outsideTemperature - nonRefrigeratedTemperature;
	
	//Calculates the surface area
	area += 2 * ptrContainer->length * ptrContainer->height;
	area += 2 * ptrContainer->width * ptrContainer->height;
	area += ptrContainer->length * ptrContainer->width;
	
	//Calculates the Resistivity
	resistivity +=  ptrContainer->thickness[0] / ptrContainer->thermalRes[0];
	resistivity +=  ptrContainer->thickness[1] / ptrContainer->thermalRes[1];
	resistivity +=  ptrContainer->thickness[2] / ptrContainer->thermalRes[2];
	
	resistivity /= area;
	
	//Calculates energy for a 1h trip
	energy = (temperature / resistivity) * 3600;
	
	return energy;
}
