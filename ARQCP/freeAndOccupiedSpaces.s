.section .section
    .global ptr
    .global X
    .global Y
    .global Z
		
.section .text
    .global freeAndOccupiedSpaces
	
freeAndOccupiedSpaces:
    movq $0, %rdx				#coordinate X, initiates at 0
    movq $0, %r14				#coordinate Y, initiates at 0
    movq $0, %r9				#coordinate Z, initiates at 0
    movslq X(%rip), %r10
    movslq Y(%rip), %r11
    movslq Z(%rip), %r12
    movq ptr(%rip), %rsi			#pointer for 3D matrix
    movq $0, %r15				#free spaces
    movq $0, %r8				#occupied spaces
	
loopThroughX:
    cmpq %rdx, %r10
    jge end
    jmp loopThroughY
	
loopThroughY:
    cmpl %r14, %r11
    jge nextX
    jmp loopThroughZ
	
loopThroughZ:
    cmpl %r9, %r12
    jge nextY
	
    movq $4, %rax
    mulq %r12
    mulq %r11
    mulq %rdx
    addq %rax, %rsi
    
    movq $4, %rax
    mulq %r12
    mulq %r14
    addq %rax, %rsi
    
    movq $4, %rax
    mulq %r9
    addq %rax, %rsi
    
    cmpl $48, (%rsi)
    je free
    incq %r8				#increments occupied spaces 
    jmp nextZ

free:
    incq %r15
    jmp nextZ
	
nextX:
    addq $1, %rdx
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
    movq %r15, %rax
    rol $64, %rax
    orq %r8, %rax
    ret
	
