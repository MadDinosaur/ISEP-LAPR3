# A Makefile for the project program
INCLUDES = fillDynamicArray.h isContainerHere.h occupiedSlots.h fill3DMatrix.h freeSpaces.h isRefrigerated.h calculateEnergy.h enoughEnergy.h
SOURCES = fillDynamicArray.c isContainerHere.s occupiedSlots.s fill3DMatrix.c main.c freeSpaces.s isRefrigerated.s calculateEnergy.c enoughEnergy.c
OBJFILES = fillDynamicArray.o isContainerHere.o occupiedSlots.o fill3DMatrix.o main.o freeSpaces.o isRefrigerated.o calculateEnergy.o enoughEnergy.o
EXEC = prog

.SUFFIXES : .c .o .s

.c.o:
	gcc -Wall -g -c $<

.s.o:
	gcc -Wall -g -c $<

${EXEC}: ${OBJFILES}
	gcc -Wall -g -lm -o ${EXEC} ${OBJFILES}

${OBJFILES}: ${SOURCES} ${INCLUDES}

run: ${EXEC}
	./${EXEC}

clean:
	rm -f ${OBJFILES} ${EXEC}
