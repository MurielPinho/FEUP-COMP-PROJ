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
.method public static func_x()I
		.limit stack 2
		.limit locals 6

		iload_0
		iload_1
		iand
		istore_2

		iload_2
		ireturn

.end method

.method public static func_x([II)I
		.limit stack 2
		.limit locals 11

		iconst_0
		iconst_1
		isub
		istore_2

		iload_2
		istore_3

		iload_0
		istore 4

		iload 4
		iconst_2
		if_icmplt cmpt0
		iconst_0
		istore 5
		goto endcmp0
	cmpt0:
		iconst_1
		istore 5
	endcmp0:
		iload 5

		iconst_0
		if_icmpeq else1

		iconst_0
		istore 6

		iload_0
		istore_3

		goto endif1

	else1:

		iconst_0
		iconst_2
		isub
		istore 7

		iload 7
		istore_3

	endif1:

		iload_3
		ireturn

.end method

.method public static func_y(II)I
		.limit stack 2
		.limit locals 6

		iload_0
		iload_1
		isub
		istore_2

		iload_2
		ireturn

.end method
