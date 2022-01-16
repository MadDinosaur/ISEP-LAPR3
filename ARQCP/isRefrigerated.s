.section .data
	.equ DATA_SIZE, 44					#Offset for the array size
	.equ REF_OFFSET, 40					#Offset for the refrigeted infromation of the container
	.equ X_OFFSET, 41					#Offset for the coordinate x information
	.equ Y_OFFSET, 42					#Offset for the coordinate y information
	.equ Z_OFFSET, 43      				#Offset for the coordinate z information
	
.section .text
	.global isRefrigerated
	
	#Pointer to array in %RDI
	#Number of elements in array in %ESI
	#Coordinate x in %DL
	#Coordinate y in %CL
	#Coordinate z in %R8B
	#Pointer to the position where the container is found in %R9
	
isRefrigerated:
	pushq %rbp
	movq %rsp ,%rbp
	
	movl $0, (%r9)						#this register is used for 2 things, fiist it is used as a way to count how many containers have been searched
										#The second is that this user stories need to return a refrigerated flag but the flag information alone makes it imposible 
										#To Calculate the energy need for this very specific container as many containers are refrigerated but not all have the same materials
										#As susch this registor is a ponter to the position of the contaier that was found in the array which let's us calculate it's energy input
										
	movl $2, %eax						#The default return value is 2 in case a container is not found
	
	cmpl (%r9), %esi
	je end
	
Check_X:
	cmpb X_OFFSET(%rdi), %dl			#Cheks if the input coordinte is equal to the x coordinate of this container else it skips to the next container
	jne continuation
	
check_Y:
	cmpb Y_OFFSET(%rdi), %cl			#Cheks if the input coordinte is equal to the y coordinate of this container
	jne continuation
	
check_Z:
	cmpb Z_OFFSET(%rdi), %r8b			#Cheks if the input coordinte is equal to the z coordinate of this container
	jne continuation
	
container_found:
	movb REF_OFFSET(%rdi), %al			#Moves the flag value to the return register if a container is found
	jmp end

continuation:
	incl (%r9)							#i++
	
	cmpl (%r9), %esi					
	je end
	
	addq $DATA_SIZE, %rdi				#Adds the data_size offset to %RDI to go to the next container in the list
	jmp Check_X

end:
	movq %rbp ,%rsp
	popq %rbp
	ret

