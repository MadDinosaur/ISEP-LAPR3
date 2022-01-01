.section .section
	.global ptr
	.global X
	.global Y
	.global Z

.section .text
	.global freeSpaces

freeSpaces:
	pushq %rbx                  #saves the stack pointer

	movq $0, %rdi				#coordinate X, initiates at 0
	movq $0, %r14				#coordinate Y, initiates at 0
	movq $0, %r9				#coordinate Z, initiates at 0

	movslq X(%rip), %rsi        #moves value of size X to rsi
	movslq Y(%rip), %r11        #moves value of size Y to r11
	movslq Z(%rip), %r12        #moves value of size Z to r12

	movq ptr(%rip), %rcx		#pointer for 3D matrix

	movq $0, %r15				#free spaces
	movq $0, %r8				#occupied spaces
	movq $0, %r13               #moves 0 to r13

loopThroughX:
	cmpq %rsi, %rdi             #compares the current coordinate value of X to the size of X
	jge end                     #if coordinate x greater than size of X, jumps to end
	jmp loopThroughY            #if not jumps to loopThroughY

loopThroughY:
	cmpq %r11, %r14             #compares the current coordinate value of Y to the size of Y
	jge nextX                   #if coordinate y greater than size of Y, jumps to nextX
	jmp loopThroughZ            #if not jumps to loopThroughZ

loopThroughZ:
	cmpq %r12, %r9              #compares the current coordinate value of Z to the size of Z
	jge nextY                   #if coordinate y greater than size of Y, jumps to end

	movq %rdi, %rax             #moves coordinate X to rax
	mulq %r11                   #multiplies rax with size of Y
	addq %r14, %rax             #adds coordinate Y to rax

	mulq %r12                   #multiplies rax by size of Z
	addq %r9, %rax              #adds coordinate Z to rax

	movl (%rcx, %rax, 4), %ebx  #uses pointer to iterate through the 3D array, obtains next element


    cmpl $0, %ebx               #0 on array means free space, compares if the current value being pointed is 0
    je free                     #if so, jumps to free
    incl %r8d				    #increments occupied spaces
	jmp nextZ                   #jumps to nextZ

free:
	addl $1, %r15d              #incremens free spaces
	jmp nextZ                   #jumps to nextZ

nextX:
    addq $1, %rdi               #increments coordinate X
    movq $0, %r14               #restarts coordinate Y
    movq $0, %r9                #restarts coordinate Z
    jmp loopThroughX            #jumps to loopThroughX

nextY:
    addq $1, %r14               #increments coordinate Y
    movq $0, %r9                #restarts coordinate Z
    jmp loopThroughY            #jumps to loopThroughY

nextZ:
    addq $1, %r9                #increments coordinate Z
    jmp loopThroughZ            #jumps to loopThroughZ

end:
	shl $16, %r15d              #pushs 16 bits of r15d (keeps number of free spaces) to the left
	movslq %r15d, %rax          #moves r15d from a 32 bit variable to a 64 bit variable
	movslq %r8d, %r8            #moves r8d from a 32 bit variable to a 64 bit variable
	addq %r8, %rax              #adds r8 to rax, most significant bytes of variable shows free spaces, least significant variables show occupied spaces
	popq %rbx                   #restores the previous stack pointer
	ret

