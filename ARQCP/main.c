#include <stdio.h>
#include <stdlib.h>
#include "fill3DMatrix.h"
#include "isContainerHere.h"
#include "occupiedSlots.h"
#include "freeSpaces.h"
#include "structs.h"
#include "fillDynamicArray.h"
#include "isRefrigerated.h"
#include "calculateEnergy.h"
#include "enoughEnergy.h"

char *ptrLocations;
char *ptrLoc;
int positions = 0;

void callFreeAndOccupiedSpaces(){
    fill3DMatrix();
    long freeOccupied = freeSpaces();
    printf("Number of free and occupied spaces, in order: %lX\n", freeOccupied);
}

void callIsContainerHere() {
    char location[3];
    ptrLoc = location;

    fill3DMatrix();

    printf("Input the desired container's location:\nX: ");
    scanf("%hhd", ptrLoc);
    printf("Y: ");
    scanf("%hhd", (ptrLoc + 1));
    printf("Z: ");
    scanf("%hhd", (ptrLoc + 2));

    if (isContainerHere())
        printf("Yes! There is a container in the location (%d, %d, %d)\n", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
    else
        printf("No! There is not a container in the location (%d, %d, %d)\n", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
}

void callOccupiedSlots() {

    //reads the amount of positions that are being analysed
    printf("Input the desired amount of positions: ");
    scanf("%d", &positions);

    if (positions <= 0)
        return;

    //initiates the array with 3 coordinates for each position
    char locations[positions][3];

    //Loops for the amount of positions and fills each coordinate
    for(int i = 0; i < positions; i++){
        printf("Input the desired location-%d:\nX: ", i + 1);
        scanf("%hhd", &locations[i][0]);
        printf("Y: ");
        scanf("%hhd", &locations[i][1]);
        printf("Z: ");
        scanf("%hhd", &locations[i][2]);
    }

    //calls the function and receives the amount of occupied slots from those selected
    ptrLocations = &locations[0][0];
    char occupied = occupiedSlots();

    printf("There are %d containers in the chosen locations\n", occupied);

}

void containerEnergy() {
    unsigned char x, y, z;

	//Get the coordinates
    printf("Input the desired container's location:\nX: ");
    scanf("%hhd", &x);
    printf("Y: ");
    scanf("%hhd", &y);
    printf("Z: ");
    scanf("%hhd", &z);

    //Used so after it is posible to calculate container energy
    int pos = 0;
    int *containerPosition = &pos;

	char refrigerated = isRefrigerated(ptrContainers, containerNum, x, y, z, containerPosition);

	//prints container information if found or ends mathod if container not found
    if (refrigerated == 0)
	{
		puts("\n\nThe container is not refrigerated");
	} else if (refrigerated == 1){
		puts("\n\nThe container is refrigerated");
	} else{
		puts("\n\nThe container does not exist");
		return;
	}

	//Print energy information
	printf("\n\nAnd the energy cost for a 1h voyage for the chosen container is %ld J", calculateEnergy(ptrContainers, *containerPosition));


}

void sendAlert() {
	int numGen;

	// Gets the total number of energy generators in a ship
	printf("How many energy generators does the ship have?\n Number of Generators: ");
	scanf("%d", &numGen);

	// Calls enoughEnergy(), it will return either 1 or 0
	char needsEnergy = enoughEnergy(ptrContainers, containerNum, numGen);

	// If needsEnergy = 1 then the ship doesn't have enough energy, if =0 it can provide enough energy
	if(needsEnergy == 1){
		printf("\n The ship doesn't have enough energy for all the containers!\n");
	}else{
		printf("\n The ship can provide enough energy for all containers.\n");
	}

}

int main(void) {
    char opt = -1;

    while (opt != 0) {
        printf("\n\nSelect what you want to do:\n");
        printf("\t1) Fill a matrix with all of the containers' location\n");
        printf("\t2) Know free and occupied spaces\n");
        printf("\t3) Know if a container is in the given location\n");
        printf("\t4) Know how many positions are occupied from given positions\n");
        printf("\t5) Fill a dynamic array with the container's info\n");
        printf("\t6) Find out if a container is refrigerated and it's energy cost\n");
        printf("\t7) Alert for current ship's energy\n");
        printf("\t0) Quit program\n");

        scanf("%hhd", &opt);

        switch (opt) {
            case 1:
                fill3DMatrix();
                break;
            case 2:
				callFreeAndOccupiedSpaces();
				break;
            case 3:
                callIsContainerHere();
                break;
            case 4:
                callOccupiedSlots();
                break;
            case 5:
                fillDynamicArray();
                break;
            case 6:
				containerEnergy();
				break;
			case 7:
				sendAlert();
				break;
            default:
                if (opt != 0)
                    printf("invalid option! please choose another.\n");
        }
    }

    if (ptrContainers != NULL)
        free(ptrContainers);

    return 0;
}
