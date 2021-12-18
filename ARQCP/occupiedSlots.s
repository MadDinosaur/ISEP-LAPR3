.section .text
    .global occupiedSlots

occupiedSlots:
     movq $0, %rax
     movw (%rdi), %cx
     cmp $0, %cx
     jl end

     movw $1, %ax

end:
     ret
