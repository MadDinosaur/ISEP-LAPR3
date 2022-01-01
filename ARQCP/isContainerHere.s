.section .data
	.global ptrLoc                  //pointer to an array with the offsets
    .global ptr                     //pointer to the 3d-matrix
    .global X                       //size of the 3d-matrix in the x axis
    .global Y                       //size of the 3d-matrix in the y axis
    .global Z                       //size of the 3d-matrix in the z axis

.section .text
    .global isContainerHere

    isContainerHere:
        pushq %rbx                  //places the current value of %rbx on the stack
        movq $0, %rax               //moves 4 bytes with the value of 0 to the register %rax (to clear the register)
        movq ptrLoc(%rip), %rdi     //moves 4 bytes with the value stored in ptrLoc to %rdi (value is the address of ptrLoc)
        movq ptr(%rip), %rcx        //moves 4 bytes with the value stored in ptrLoc to %rcx (value is the address of ptr)

        movb (%rdi), %al            //moves 1 byte with the value pointed by ptrLoc to %al (offset for the x axis)
        mulb Y(%rip)                //multiplies the byte stored in %al by Y
        addb 1(%rdi), %al           //adds the offset of the y axis to the value currently in %al
        mulb Z(%rip)                //multiplies the byte stored in %al by Z
        addb 2(%rdi), %al           //adds the offset of the z axis to the value currently in %al

        movl (%rcx, %rax, 4), %ebx  //moves the value of the previous arithmetic operation to %ebx

        cmp $0, %ebx                //compares the value in %ebx with 0
        jne containerExists         //if the value in %ebx is not 0 jumps to "containerExists"

        movb $0, %al                //moves 1 byte with the value of 0 to the register %al
        jmp end                     //jumps to the end of the function

    containerExists:
        movb $1, %al                //moves 1 byte with the value of 1 to the register %al

    end:
        popq %rbx                   //gets the value of %rbx that was saved on the stack
        ret
