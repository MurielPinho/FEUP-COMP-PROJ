.class public Pot2
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public static main([Ljava/lang/String;)V
		.limit stack 2
		.limit locals 13

		iload_0
		istore_1

		iload_2
		ifeq cmpt0
		iconst_0
		istore_3
		goto endcmp0
	cmpt0:
		iconst_1
		istore_3
	endcmp0:
		aload_0
		invokevirtual expr2()V


		iload 4
		istore 5

		iconst_4
		iload 5
		if_icmplt cmpt1
		iconst_0
		istore 6
		goto endcmp1
	cmpt1:
		iconst_1
		istore 6
	endcmp1:

		iload_3
		iload 6
		iand
		istore 7

		iload_1
		iload 7
		iand
		istore 8

		return
.end method

.method public static expr1()I
		.limit stack 2
		.limit locals 6

		iconst_2
		istore_0

		iconst_3
		istore_1

		iconst_0
		istore_2

		iload_0
		iload_1
		if_icmplt cmpt2
		iconst_0
		istore_3
		goto endcmp2
	cmpt2:
		iconst_1
		istore_3
	endcmp2:

		iload_3
		iload_2
		iand
		istore 4

		iload 4
		ireturn

.end method

.method public static expr2()I
		.limit stack 2
		.limit locals 16

		iload_1
		iload_2
		imul
		istore_0

		iload 4
		ifeq cmpt3
		iconst_0
		istore 3
		goto endcmp3
	cmpt3:
		iconst_1
		istore 3
	endcmp3:

		iload_0
		iload 6
		isub
		istore 5

		iload 5
		iload_3
		if_icmplt cmpt4
		iconst_0
		istore 7
		goto endcmp4
	cmpt4:
		iconst_1
		istore 7
	endcmp4:

		iload 9
		iload 10
		iand
		istore 8

		iload 8
		ifeq cmpt5
		iconst_0
		istore 11
		goto endcmp5
	cmpt5:
		iconst_1
		istore 11
	endcmp5:

		iload 13
		iload 11
		iand
		istore 12

		iload 7
		iload 12
		iand
		istore 14

		iload 14
		ireturn

.end method

.method public static expr3()I
		.limit stack 2
		.limit locals 36

		iload_1
		iload_2
		imul
		istore_0

		iload 4
		iload_0
		if_icmplt cmpt6
		iconst_0
		istore_3
		goto endcmp6
	cmpt6:
		iconst_1
		istore_3
	endcmp6:

		iload 6
		istore 5

		iload 5
		istore 7

		iload 7
		iload 9
		iand
		istore 8

		iload 8
		istore 10

		iload 10
		ifeq cmpt7
		iconst_0
		istore 11
		goto endcmp7
	cmpt7:
		iconst_1
		istore 11
	endcmp7:

		iload 11
		istore 12

		iload 14
		iload 15
		idiv
		istore 13

		iload 13
		iload 17
		iadd
		istore 16

		iload 19
		iload 20
		isub
		istore 18

		iload 16
		iload 18
		if_icmplt cmpt8
		iconst_0
		istore 21
		goto endcmp8
	cmpt8:
		iconst_1
		istore 21
	endcmp8:

		iload 21
		ifeq cmpt9
		iconst_0
		istore 22
		goto endcmp9
	cmpt9:
		iconst_1
		istore 22
	endcmp9:

		iload 24
		istore 23

		iload 23
		istore 25

		iload 25
		istore 26

		iload 28
		ifeq cmpt10
		iconst_0
		istore 27
		goto endcmp10
	cmpt10:
		iconst_1
		istore 27
	endcmp10:

		iload 26
		iload 27
		iand
		istore 29

		iload 31
		iload 29
		iand
		istore 30

		iload 22
		iload 30
		iand
		istore 32

		iload 12
		iload 32
		iand
		istore 33

		iload_3
		iload 33
		iand
		istore 34

		iload 34
		ireturn

.end method
