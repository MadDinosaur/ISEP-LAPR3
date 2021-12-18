# A Makefile for the project program
INCLUDES = isContainerHere.h occupiedSlots.h fill3DMatrix.h
SOURCES = isContainerHere.s occupiedSlots.s fill3DMatrix.c main.c
OBJFILES = isContainerHere.o occupiedSlots.o fill3DMatrix.o main.o
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
