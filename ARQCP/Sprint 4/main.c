#include <stdio.h>
#include <stdlib.h>

#define FILE_PATH "containers.txt"


/**
 * ID - The container's ID
 * X  - The container position in the X axis
 * Y  - The container position in the Y axis
 * Z  - The container position in the Z axis
 * Refrigerated - A flag to see if the container is refrigerated, 1 if it is 0 if it isn't
 */
typedef struct {
    int id;
    char refrigerated;
    unsigned char x;
    unsigned char y;
    unsigned char z;
} container;

/** Declare Pointer */
container  *ptrContainers = NULL;

/** Number of containers to allocate */
int n;

container* fill_vec(container *vec,FILE *inFile);

int main() {

    FILE *inFile = fopen(FILE_PATH,"r");

    /** Checks if the file exists */
    if (inFile == NULL){
        printf("Error! File not found\n");
        exit(-1);
    }

    /** Gets the number of containers */
    fscanf(inFile, "%d", &n);

    /** Allocate n containers in the heap */
    ptrContainers = (container *) calloc(n,sizeof(container));

    ptrContainers = fill_vec(ptrContainers,inFile);

    /** Print vector */
    int i;
    for(i = 0;i < n; i++){
        printf("ID: %d \n Refrigerated:%c \n X:%c\n Y:%c\n Z:%c\n",(ptrContainers + i)->id,(ptrContainers + i)->refrigerated,(ptrContainers + i)->x,(ptrContainers + i)->y,(ptrContainers + i)->z);
    }

    /** Close the file */
    fclose(inFile);

    /** Free Memory */
    free(ptrContainers);

    return 0;
}

container* fill_vec(container *vec,FILE *inFile){

    double v;
    int i;
    int tempID;
    char tempRefrigerated;
    unsigned char tempX;
    unsigned char tempY;
    unsigned char tempZ;

    v = fscanf(inFile,"%d,%c,%c,%c,%c", &tempID,&tempRefrigerated,&tempX,&tempY,&tempZ);
    i = 0;
    /**  A cycle for every line of the file */
    while (v != EOF) {
        (vec+i) -> id = tempID;
        (vec+i) -> refrigerated = tempRefrigerated;
        (vec+i) -> x = tempX;
        (vec+i) -> y = tempY;
        (vec+i) -> z = tempZ;
        v = fscanf(inFile,"%d,%c,%c,%c,%c", &tempID,&tempRefrigerated,&tempX,&tempY,&tempZ);
        i++;
    }
    return vec;
}
