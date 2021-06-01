.class public Pot3
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public func(I)I
		.limit stack 2
		.limit locals 16

		iconst_0
		istore_2

		iconst_3
		iconst_4
		if_icmplt cmpt0
		iconst_0
		istore_3
		goto endcmp0
	cmpt0:
		iconst_1
		istore_3
	endcmp0:

		iload_3
		ifeq cmpt1
		iconst_0
		istore 4
		goto endcmp1
	cmpt1:
		iconst_1
		istore 4
	endcmp1:

		iconst_1
		iload 4
		iand
		istore 5

		iload 5
		ifeq cmpt2
		iconst_0
		istore 6
		goto endcmp2
	cmpt2:
		iconst_1
		istore 6
	endcmp2:

		iload 6
		istore 7
		iload 7

		iconst_0
		if_icmpeq else1

		iload_2
		istore 8

		goto endif1

	else1:

		iconst_0
		istore 8

	endif1:

		iload_1
		iconst_1
		if_icmplt cmpt3
		iconst_0
		istore 9
		goto endcmp3
	cmpt3:
		iconst_1
		istore 9
	endcmp3:
		iload 9

		iconst_0
		if_icmpeq else2

		iconst_1
		iconst_3
		if_icmplt cmpt4
		iconst_0
		istore 10
		goto endcmp4
	cmpt4:
		iconst_1
		istore 10
	endcmp4:
		iload 10

		iconst_0
		if_icmpeq else3

		iconst_1
		istore 8

		goto endif3

	else3:

		iload_2
		istore 8

	endif3:

		goto endif2

	else2:

		iconst_2
		iconst_2
		imul
		istore 11

		iconst_1
		iload 11
		iadd
		istore 12

		iload 12
		iconst_5
		imul
		istore 13

		iload 13
		istore 8

	endif2:

		iconst_2
		iconst_1
		if_icmplt cmpt5
		iconst_0
		istore 14
		goto endcmp5
	cmpt5:
		iconst_1
		istore 14
	endcmp5:
		iload 14

		iconst_0
		if_icmpeq else4

		iconst_3
		istore 8

		goto endif4

	else4:

		iconst_4
		istore 8

	endif4:

		iload 8
		ireturn

.end method
