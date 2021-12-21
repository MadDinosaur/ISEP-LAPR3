.section .data
	.global ptrLocations
	.global positions
	.global ptrLoc
	
.section .text
    .global occupiedSlots

occupiedSlots:
	pushq %rbx
	
    movq positions(%rip), %rdx
    leaq ptrLocations(%rip), %rdi
    
    movq $0, %rcx
    movb $0, %sil
    
    cmpq %rcx, %rdx
    jne loop_locations
    
	jmp end
     
loop_locations:
	pushq %rcx
	pushq %rsi
	pushq %rdi
	pushq %rdx
	
	imulq $3, %rcx
	movb (%rdi, %rcx, 1), %dl
	movb %dl, ptrLoc(%rip)
	call isContainerHere
	
	popq %rdx
	popq %rdi
	popq %rsi
	popq %rcx

	addb %al, %sil
	incq %rcx
	
	cmpq %rcx, %rdx
    jne loop_locations

end:
	movb %sil, %al
	popq %rbx
    ret
