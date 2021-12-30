.section .data
	.global ptrLoc
    .global ptr
    .global X
    .global Y
    .global Z

.section .text
    .global isContainerHere

    isContainerHere:
        pushq %rbx
        movq $0, %rax
        movq ptrLoc(%rip), %rdi
        movq ptr(%rip), %rcx

        movb (%rdi), %al
        mulb Y(%rip)
        addb 1(%rdi), %al
        mulb Z(%rip)
        addb 2(%rdi), %al

        movl (%rcx, %rax, 4), %ebx

        cmp $0, %ebx
        jne containerExists

        movb $0, %al
        jmp end

    containerExists:
        movb $1, %al

    end:
        popq %rbx
        ret
