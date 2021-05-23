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
		.limit stack 20 ; TBD
		.limit locals 15

		iconst_0
		istore_1

		iconst_3
		iconst_4
		ilth
		istore_2

		iconst_1
		iload_3
		iandb
		istore 4

		iload 5
		istore 6

		iload_6
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
		ilth
		istore 8

		iload_8
		iconst_0
		if_icmpeq else2

		iconst_1
		iconst_3
		ilth
		istore 9

		iload_9
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
		ilth
		istore 12

		iload_12
		iconst_0
		if_icmpeq else4

		iconst_3
		istore 7

		goto endif4

	else4:

		iconst_4
		istore 7

	endif4:

		iload_7
		ireturn

.end method
