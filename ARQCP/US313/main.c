#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define LENGTH_CONTAINER 14.5
#define WIDTH_CONTAINER 2.5
#define Z 9
#define FILE_PATH "containers.txt"

int *ptr;

int main(void){

    /**
     * The ship's length
     **/
    double lengthShip;

    /**
     * The ship's width
     */
    double widthShip;

    /**
     * The matrix's X size
     */
    int X;

    /**
     * The matrix's Y size
     */
    int Y;

    /**
     * The iterators to traverse the matrix
     */
    int i;
    int j;
    int k;

    /**
     * The temporary X value
     */
    int tempX;

    /**
     * The temporary Y value
     */
    int tempY;

    /**
     * The temporary Z value
     */
    int tempZ;

    /**
     * The temporary ID
     */
    int tempID;



    FILE *in_file = fopen(FILE_PATH,"r");

    /**
     * Checks if the file exists
     */
    if (in_file == NULL){
        printf("Error! File not found\n");
        exit(-1);
    }

    /**
     * Gets the value of the Ship's width and length
     */
    fscanf(in_file, "%lf,%lf", &widthShip, &lengthShip);

    /**
     * Gets the size of X and Y
     */
    X = floor(widthShip/WIDTH_CONTAINER);
    Y = floor(lengthShip/LENGTH_CONTAINER);

    /**
     * The matrix created with size X,Y,Z that will store the containers' ID
     */
    int matrix[X][Y][Z];
    ptr = &matrix[0][0][0];

    /**
     * Sets all the values of the matrix to 0
     */
    for(i=0;i<X;i++) {
        for (j = 0; j < Y; j++) {
            for (k = 0; k < Z; k++) {

                matrix[i][j][k] = 0;
            }
        }
    }


    double v;
    v = fscanf(in_file,"%d,%d,%d,%d", &tempID,&tempX,&tempY,&tempZ);

    /**
     * A cycle for every line of the file
     */
    while (v != EOF) {
        matrix[tempX][tempY][tempZ] = tempID;
        v = fscanf(in_file, "%d,%d,%d,%d", &tempID, &tempX, &tempY, &tempZ);
    }



    // Show the matrix
    for(i=0;i<X;i++) {
        for (j = 0; j < Y; j++) {
            for (k = 0; k < Z; k++) {

                printf("%d ", matrix[i][j][k]);
            }
            printf("\n");
        }
        printf("\n");
    }

    // Show the ship measures
    printf("Width:%f Length:%f\n",widthShip,lengthShip);

    // Show the array sizes
    printf("X:%d Y:%d Z:%d\n",X,Y,Z);

    /**
     * Close the file
     */
    fclose(in_file);
}
