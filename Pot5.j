.class public Pot5
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public func_x()I
		.limit stack 2
		.limit locals 6

		iload_1
		iload_2
		iand
		istore_3

		iload_3
		ireturn

.end method

.method public func_x([II)I
		.limit stack 2
		.limit locals 11

		iconst_0
		iconst_1
		isub
		istore_3

		iload_3
		istore 4

		iload_1
		istore 5

		iload 5
		iconst_2
		if_icmplt cmpt0
		iconst_0
		istore 6
		goto endcmp0
	cmpt0:
		iconst_1
		istore 6
	endcmp0:
		iload 6

		iconst_0
		if_icmpeq else1

		iconst_0
		istore 7

		iload_1
		istore 4

		goto endif1

	else1:

		iconst_0
		iconst_2
		isub
		istore 8

		iload 8
		istore 4

	endif1:

		iload 4
		ireturn

.end method

.method public func_y(II)I
		.limit stack 2
		.limit locals 6

		iload_1
		iload_2
		isub
		istore_3

		iload_3
		ireturn

.end method
