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
.method public static func(I)I
		.limit stack 2
		.limit locals 15

		iconst_0
		istore_1

		iconst_3
		iconst_4
		if_icmplt cmpt0
		iconst_0
		istore_2
		goto endcmp0
	cmpt0:
		iconst_1
		istore_2
	endcmp0:

		iload_2
		ifeq cmpt1
		iconst_0
		istore_3
		goto endcmp1
	cmpt1:
		iconst_1
		istore_3
	endcmp1:

		iconst_1
		iload_3
		iand
		istore 4

		iload 4
		ifeq cmpt2
		iconst_0
		istore 5
		goto endcmp2
	cmpt2:
		iconst_1
		istore 5
	endcmp2:

		iload 5
		istore 6
		iload 6

		iconst_0
		if_icmpeq else1

		iload_1
		istore 7

		goto endif1

	else1:

		iconst_0
		istore 7

	endif1:

		iload_0
		iconst_1
		if_icmplt cmpt3
		iconst_0
		istore 8
		goto endcmp3
	cmpt3:
		iconst_1
		istore 8
	endcmp3:
		iload 8

		iconst_0
		if_icmpeq else2

		iconst_1
		iconst_3
		if_icmplt cmpt4
		iconst_0
		istore 9
		goto endcmp4
	cmpt4:
		iconst_1
		istore 9
	endcmp4:
		iload 9

		iconst_0
		if_icmpeq else3

		iconst_1
		istore 7

		goto endif3

	else3:

		iload_1
		istore 7

	endif3:

		goto endif2

	else2:

		iconst_2
		iconst_2
		imul
		istore 10

		iconst_1
		iload 10
		iadd
		istore 11

		iload 11
		iconst_5
		imul
		istore 7

	endif2:

		iconst_2
		iconst_1
		if_icmplt cmpt5
		iconst_0
		istore 12
		goto endcmp5
	cmpt5:
		iconst_1
		istore 12
	endcmp5:
		iload 12

		iconst_0
		if_icmpeq else4

		iconst_3
		istore 7

		goto endif4

	else4:

		iconst_4
		istore 7

	endif4:

		iload 7
		ireturn

.end method
