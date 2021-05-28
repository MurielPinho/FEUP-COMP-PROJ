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
.method public getNum()I
		.limit stack 2
		.limit locals 11

		iconst_1
		istore_1

		iload_1
		istore_2

		iload_2
		istore_3

		iconst_3
		bipush 6
		imul
		istore 4

		iload 4
		iload_3
		isub
		istore 5

		iconst_5
		iload 5
		iadd
		istore 6

		iconst_3
		iload 6
		if_icmplt cmpt0
		iconst_0
		istore 7
		goto endcmp0
	cmpt0:
		iconst_1
		istore 7
	endcmp0:

		iconst_0
		iload 7
		iand
		istore 8

		iconst_1
		iload 8
		iand
		istore 9

		iload 9
		istore 10

		bipush 21
		ireturn

.end method

.method public sum(II)I
		.limit stack 2
		.limit locals 6

		iload_1
		iload_2
		iadd
		istore_3

		iload_3
		ireturn

.end method

.method public sum([II)I
		.limit stack 2
		.limit locals 9

		iconst_0
		istore_3

		iconst_0
		istore 4

	Loop1:
		iload_3
		iload_2
		if_icmplt Body1

		goto EndLoop1

	Body1:

		iload_1
		istore 5

		iload 4
		iload 5
		iadd
		istore 6

		iload 6
		istore 4

	EndLoop1:

		iload 4
		ireturn

.end method

.method public sub(II)I
		.limit stack 2
		.limit locals 6

		iload_1
		iload_2
		isub
		istore_3

		iload_3
		ireturn

.end method

.method public sub(I)I
		.limit stack 2
		.limit locals 4

		iconst_0
		iload_1
		isub
		istore_2

		iload_2
		ireturn

.end method

.method public mult(II)I
		.limit stack 2
		.limit locals 6

		iload_1
		iload_2
		imul
		istore_3

		iload_3
		ireturn

.end method
