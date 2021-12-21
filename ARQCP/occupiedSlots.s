.section .text
    .global occupiedSlots

occupiedSlots:
	pushq %rbx
    movq $0, %rcx
    movslq %edx, %rdx
    movb $0, %dl
    cmpq %rcx, %rsi
    jne loop_locations
	jmp end
     
loop_locations:
	pushq %rcx
	pushq %rsi
	pushq %rdi
	pushq %rdx
	
	leaq (%rdi, %rcx, 1), %rdi
	movb $2, %al
	
	popq %rcx
	popq %rsi
	popq %rdi
	popq %rdx

	addb %al, %dl
	incq %rcx
	
	cmpq %rcx, %rsi
    jne loop_locations

end:
	movb %dl, %al
	popq %rbx
    ret
