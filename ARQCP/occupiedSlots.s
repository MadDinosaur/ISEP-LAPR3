.section .data
	.global ptrLocations
	.global positions
	.global ptrLoc
	
.section .text
    .global occupiedSlots

occupiedSlots:
	pushq %rbx
	
    movslq positions(%rip), %rdx  	#Stores the number of positions on the ptrLocations
    movq ptrLocations(%rip), %rdi 	#Has all the desired locations
    
    movq $0, %rcx 					#Makes rcx the counter for the amount of loops necessary
    movb $0, %sil					#A temporary variable to count all the positions which have containers
    
    cmpq %rcx, %rdx  				#If the ammount of positions is 0 then it ends
    jne loop_locations
    
	jmp end
     
loop_locations:
	pushq %rcx   					#Stores the current variables in the stack
	pushq %rsi
	pushq %rdi
	pushq %rdx
	
	imulq $3, %rcx					#Each position has 3 values so rcx needs to be 3 times the current loopThroughX
	leaq (%rdi ,%rcx ,1), %rdx		#Stores the pointer to the first value of each position
	movq %rdx, ptrLoc(%rip)			#Changes the value of the pointer that has the positions for the method 'isContainerHere'
	call isContainerHere 			#Calls the needed method which return 1 if theres a container in said position
	
	popq %rdx						#Recalls the variable values from the tack
	popq %rdi
	popq %rsi
	popq %rcx

	addb %al, %sil 					#Adds the previously given return value and adds it to the counter
	incq %rcx
	
	cmpq %rcx, %rdx					#Loops the function while it still has positions
    jne loop_locations

end:
	movb %sil, %al					#Sends the stores counter to the return value
	popq %rbx
    ret
