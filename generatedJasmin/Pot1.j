.class public Pot1
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public static getNum()I
		.limit stack 2
		.limit locals 11

		iconst_1
		istore_0

		iload_0
		istore_1

		iload_1
		istore_2

		iconst_3
		bipush 6
		imul
		istore_3

		iload_3
		iload_2
		isub
		istore 4

		iconst_5
		iload 4
		iadd
		istore 5

		iconst_3
		iload 5
		if_icmplt cmpt0
		iconst_0
		istore 6
		goto endcmp0
	cmpt0:
		iconst_1
		istore 6
	endcmp0:

		iconst_0
		iload 6
		iand
		istore 7

		iconst_1
		iload 7
		iand
		istore 8

		iload 8
		istore 9

		bipush 21
		ireturn

.end method

.method public static sum(II)I
		.limit stack 2
		.limit locals 6

		iload_0
		iload_1
		iadd
		istore_2

		iload_2
		ireturn

.end method

.method public static sum([II)I
		.limit stack 2
		.limit locals 9

		iconst_0
		istore_2

		iconst_0
		istore_3

	Loop1:
		iload_2
		iload_1
		if_icmplt Body1

		goto EndLoop1

	Body1:

		iload_0
		istore 4

		iload_3
		iload 4
		iadd
		istore 5

		iload 5
		istore_3

	EndLoop1:

		iload_3
		ireturn

.end method

.method public static sub(II)I
		.limit stack 2
		.limit locals 6

		iload_0
		iload_1
		isub
		istore_2

		iload_2
		ireturn

.end method

.method public static sub()I
		.limit stack 2
		.limit locals 10
		aload_0
		invokevirtual getNum()V


		iload_2
		istore_3
		aload_1
		invokevirtual getNum()V


		iload 4
		istore 5

		iload_3
		iload 5
		isub
		istore 6

		iload 6
		ireturn

.end method

.method public static sub(I)I
		.limit stack 2
		.limit locals 4

		iconst_0
		iload_0
		isub
		istore_1

		iload_1
		ireturn

.end method

.method public static sub()I
		.limit stack 2
		.limit locals 6
		aload_0
		invokevirtual getNum()V


		iload_1
		istore_2

		iconst_0
		iload_2
		isub
		istore_3

		iload_3
		ireturn

.end method

.method public static mult(II)I
		.limit stack 2
		.limit locals 6

		iload_0
		iload_1
		imul
		istore_2

		iload_2
		ireturn

.end method
