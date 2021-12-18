#include <stdio.h>
#include "fill3DMatrix.h"
#include "isContainerHere.h"
#include "occupiedSlots.h"

int *ptr;

void callIsContainerHere() {
    char location[3];
    char* ptrLoc = location;

    fill3DMatrix();

    printf("Input the desired container's location:\nX: ");
    scanf("%hhd", ptrLoc);
    printf("Y: ");
    scanf("%hhd", (ptrLoc + 1));
    printf("Z: ");
    scanf("%hhd", (ptrLoc + 2));

    if (isContainerHere(ptrLoc)) 
        printf("Yes! There is a container in the location (%d, %d, %d)\n", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
    else 
        printf("No! There is not a container in the location (%d, %d, %d)\n", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
}

void callOccupiedSlots() {
    int positions = 0;

    //reads the amount of positions that are being analysed
    printf("Input the desired amount of positions:\nX: ");
    scanf("%d", &positions);

    if (positions <= 0)
        return;

    //initiates the array with 3 coordinates for each position
    char locations[3][positions];

    //Loops for the amount of positions and fills each coordinate
    for(int i = 0; i < positions; i++){
        printf("Input the desired location-%d:\nX: ", i + 1);
        scanf("%hhd", &locations[0][i]);
        printf("Y: ");
        scanf("%hhd", &locations[1][i]);
        printf("Z: ");
        scanf("%hhd", &locations[2][i]);
    }

    //calls the function and receives the amount of occupied slots from those selected
    char occupied = occupiedSlots(locations[0][0]);

    printf("There are %d containers in the chosen locations\n", occupied);

}

int main(void) {
    char opt = -1;
    
    while (opt != 0) {
        printf("\n\nSelect what you want to do:\n");
        printf("\t1) Fill a matrix with all of the containers' location\n");
        printf("\t2) Know if a container is in the given location\n");
        printf("\t3) Know how many positions are occupied from given positions\n");
        printf("\t0) Quit program\n");

        scanf("%hhd", &opt);

        switch (opt) {
            case 1: 
                fill3DMatrix();
                break;
            case 2:
                callIsContainerHere();
                break;
            case 3: 
                callOccupiedSlots();
                break;    
            default:
                if (opt != 0)
                    printf("invalid option! please choose another.\n");
        }
    }

    return 0;
}