.section .section

	.global ptr

	.global X

	.global Y

	.global Z

		

.section .text

	.global freeSpaces

	

freeSpaces:

	pushq %rbx

	movq $0, %rdi				#coordinate X, initiates at 0

	movq $0, %r14				#coordinate Y, initiates at 0

	movq $0, %r9				#coordinate Z, initiates at 0

	movslq X(%rip), %rsi

	movslq Y(%rip), %r11

	movslq Z(%rip), %r12

	movq ptr(%rip), %rcx		#pointer for 3D matrix

	movq $0, %r15				#free spaces

	movq $0, %r8				#occupied spaces

	movq $0, %r13

	

loopThroughX:

	cmpq %rsi, %rdi

	jge end

	jmp loopThroughY

	

loopThroughY:

	cmpq %r11, %r14

	jge nextX

	jmp loopThroughZ

	

loopThroughZ:

	cmpq %r12, %r9

	jge nextY



	movq %rdi, %rax				#moves 1 into rax, 

	mulq %r11

	addq %r14, %rax

	mulq %r12

	addq %r9, %rax

	

	movl (%rcx, %rax, 4), %ebx

    

    cmpl $0, %ebx

    je free

    incl %r8d				#increments occupied spaces 

	jmp nextZ



free:

	addl $1, %r15d

	jmp nextZ

	

nextX:

    addq $1, %rdi

    movq $0, %r14

    movq $0, %r9

    jmp loopThroughX



nextY:

    addq $1, %r14

    movq $0, %r9

    jmp loopThroughY



nextZ:

    addq $1, %r9

    jmp loopThroughZ

	

end:

	shl $16, %r15d

	movslq %r15d, %rax

	movslq %r8d, %r8

	addq %r8, %rax

	popq %rbx

	ret

