#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "structs.h"

#define FILE_PATH "containersDynamic.txt"

/** Number of containers to allocate */
int containerNum;

container* fill_vec(container *vec,FILE *inFile);

/** Declare Pointer */
container  *ptrContainers = NULL;

void fillDynamicArray() {
    FILE *inFile = fopen(FILE_PATH,"r");

    /** Checks if the file exists */
    if (inFile == NULL){
        printf("Error! File not found\n");
        exit(-1);
    }

    /** Gets the number of containers */
    fscanf(inFile, "%d", &containerNum);

    /** Allocate n containers in the heap */
    ptrContainers = (container *) calloc(containerNum,sizeof(container));

    ptrContainers = fill_vec(ptrContainers, inFile);

    /** Print vector */
    int i;
    for(i = 0;i < containerNum; i++){
        printf("ID: %d \n Refrigerated: %d \n X: %d\n Y: %d\n Z: %d\n Length: %.2fm\n Width: %.2fm\n Height: %.2fm\n Thermal Resistance of Material 1: %.2f\n Thickness of Material 1: %.2fm\n Thermal Resistance of Material 2: %.2f\n Thickness of Material 2: %.2fm\n Thermal Resistance of Material 3: %.2f\n Thickness of Material 3: %.2fm\n",
            (ptrContainers + i)->id,
            (ptrContainers + i)->refrigerated,
            (ptrContainers + i)->x,
            (ptrContainers + i)->y,
            (ptrContainers + i)->z,
            (ptrContainers + i)->length,
            (ptrContainers + i)->width,
            (ptrContainers + i)->height,
            (ptrContainers + i)->thermalRes[0],
            (ptrContainers + i)->thickness[0],
            (ptrContainers + i)->thermalRes[1],
            (ptrContainers + i)->thickness[1],
            (ptrContainers + i)->thermalRes[2],
            (ptrContainers + i)->thickness[2]);

        printf("---------------------------------\n");
    }

    /** Close the file */
    fclose(inFile);
}

container* fill_vec(container *vec,FILE *inFile){
    double v;
    int i, tempID;
    float tempThermalRes[3], tempThickness[3], tempLength, tempWidth, tempHeight;
    int tempRefrigerated;
    unsigned int tempX, tempY, tempZ;

    i = 0;
    /**  A cycle for every line of the file */
    do {
        v = fscanf(inFile,"%d;%d;%d;%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%f",
            &tempID, &tempRefrigerated, &tempX, &tempY, &tempZ, 
            &tempLength, &tempWidth, &tempHeight, 
             &tempThermalRes[0], &tempThickness[0],
            &tempThermalRes[1], &tempThickness[1],
            &tempThermalRes[2], &tempThickness[2]);

        (vec+i) -> thermalRes[0] = tempThermalRes[0];
        (vec+i) -> thermalRes[1] = tempThermalRes[1];
        (vec+i) -> thermalRes[2] = tempThermalRes[2];
        (vec+i) -> thickness[0] = tempThickness[0];
        (vec+i) -> thickness[1] = tempThickness[1];
        (vec+i) -> thickness[2] = tempThickness[2];
        (vec+i) -> length = tempLength;
        (vec+i) -> width = tempWidth;
        (vec+i) -> height = tempHeight;
        (vec+i) -> id = tempID;
        (vec+i) -> refrigerated = tempRefrigerated;
        (vec+i) -> x = tempX;
        (vec+i) -> y = tempY;
        (vec+i) -> z = tempZ;

        i++;
    }  while (v != EOF);

    return vec;
}
