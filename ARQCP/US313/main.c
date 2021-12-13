#include <stdio.h>

#define X 2
#define Y 3
#define Z 5

int m1[Y][Z] = {{1,2,3,4,5}, {6,7,8,9,10}, {11,12,13,14,15}};

int m2[X][Y][Z] = {
        {{1,2,3,4,5}, {6,7,8,9,10}, {11,12,13,14,15}},
        {{16,17,18,19,20}, {21,22,23,24,25}, {26,27,28,29,30}}
};




int get_even(){
    int i,j,k, even = 0;

    int *ptr = &m2[0][0][0];

    for(i=0;i<X;i++){
        for(j=0;j<Y;j++){
            for(k=0;k<Z;k++){
                if(*ptr % 2 == 0)
                    even++;
                ptr++;
            }
        }
    }

    return even;
}


int main(void) {


    int *ptr1 = &m1[0][0];
    printf("m1[2][3] = %d\n", m1[2][3]);
    printf("m1[2][3] = %d\n", *(*m1 + (2*Z) + 3));
    printf("m1[2][3] = %d\n", *(ptr1 + (2*Z) + 3));


    int *ptr2 = &m2[0][0][0];
    printf("m2[1][2][3] = %d\n",m2[1][2][3]);
    printf("m2[1][2][3] = %d\n",*(*(*(m2+1) + 2) + 3));
    printf("m2[1][2][3] = %d\n",*(ptr2 + (1*Y*Z) + (2*Z) + 3));

    int even = get_even();
    printf("Number of even elements = %d\n", even);


    return 0;
}

