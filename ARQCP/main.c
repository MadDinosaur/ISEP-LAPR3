#include <stdio.h>
#include "fill3DMatrix.h"
#include "isContainerHere.h"

int *ptr;

void callIsConainterHere() {
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
        printf("Yes! There is a container in the location (%d, %d, %d)", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
    else 
        printf("No! There is not a container in the location (%d, %d, %d)", *ptrLoc, *(ptrLoc + 1), *(ptrLoc + 2));
}

int main(void) {
    char opt = -1;
    
    while (opt != 0) {
        printf("\n\nSelect what you want to do:\n");
        printf("\t1) Fill a matrix with all of the containers' location\n");
        printf("\t2) Know if a container is in the given location\n");
        printf("\t0) Quit program\n");

        scanf("%hhd", &opt);

        switch (opt) {
            case 1: 
                fill3DMatrix();
                break;
            case 2: 
                callIsConainterHere();
                break;
            default:
                if (opt != 0)
                    printf("invalid option! please choose another.\n");
        }
    }

    return 0;
}