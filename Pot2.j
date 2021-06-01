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
		.limit locals 14

		iload_1
		istore_2

		iload_3
		ifeq cmpt0
		iconst_0
		istore 4
		goto endcmp0
	cmpt0:
		iconst_1
		istore 4
	endcmp0:
		aload_0
		invokevirtual expr2()V


		iload 5
		istore 6

		iconst_4
		iload 6
		if_icmplt cmpt1
		iconst_0
		istore 7
		goto endcmp1
	cmpt1:
		iconst_1
		istore 7
	endcmp1:

		iload 4
		iload 7
		iand
		istore 8

		iload_2
		iload 8
		iand
		istore 9

		iload 9
		istore 10

		return
.end method

.method public expr1()I
		.limit stack 2
		.limit locals 6

		iconst_2
		istore_1

		iconst_3
		istore_2

		iconst_0
		istore_3

		iload_1
		iload_2
		if_icmplt cmpt2
		iconst_0
		istore 4
		goto endcmp2
	cmpt2:
		iconst_1
		istore 4
	endcmp2:

		iload 4
		iload_3
		iand
		istore 5

		iload 5
		ireturn

.end method

.method public expr2()I
		.limit stack 2
		.limit locals 16

		iload_2
		iload_3
		imul
		istore_1

		iload 5
		ifeq cmpt3
		iconst_0
		istore 4
		goto endcmp3
	cmpt3:
		iconst_1
		istore 4
	endcmp3:

		iload_1
		iload 7
		isub
		istore 6

		iload 6
		iload 4
		if_icmplt cmpt4
		iconst_0
		istore 8
		goto endcmp4
	cmpt4:
		iconst_1
		istore 8
	endcmp4:

		iload 10
		iload 11
		iand
		istore 9

		iload 9
		ifeq cmpt5
		iconst_0
		istore 12
		goto endcmp5
	cmpt5:
		iconst_1
		istore 12
	endcmp5:

		iload 14
		iload 12
		iand
		istore 13

		iload 8
		iload 13
		iand
		istore 15

		iload 15
		ireturn

.end method

.method public expr3()I
		.limit stack 2
		.limit locals 36

		iload_2
		iload_3
		imul
		istore_1

		iload 5
		iload_1
		if_icmplt cmpt6
		iconst_0
		istore 4
		goto endcmp6
	cmpt6:
		iconst_1
		istore 4
	endcmp6:

		iload 7
		istore 6

		iload 6
		istore 8

		iload 8
		iload 10
		iand
		istore 9

		iload 9
		istore 11

		iload 11
		ifeq cmpt7
		iconst_0
		istore 12
		goto endcmp7
	cmpt7:
		iconst_1
		istore 12
	endcmp7:

		iload 12
		istore 13

		iload 15
		iload 16
		idiv
		istore 14

		iload 14
		iload 18
		iadd
		istore 17

		iload 20
		iload 21
		isub
		istore 19

		iload 17
		iload 19
		if_icmplt cmpt8
		iconst_0
		istore 22
		goto endcmp8
	cmpt8:
		iconst_1
		istore 22
	endcmp8:

		iload 22
		ifeq cmpt9
		iconst_0
		istore 23
		goto endcmp9
	cmpt9:
		iconst_1
		istore 23
	endcmp9:

		iload 25
		istore 24

		iload 24
		istore 26

		iload 26
		istore 27

		iload 29
		ifeq cmpt10
		iconst_0
		istore 28
		goto endcmp10
	cmpt10:
		iconst_1
		istore 28
	endcmp10:

		iload 27
		iload 28
		iand
		istore 30

		iload 32
		iload 30
		iand
		istore 31

		iload 23
		iload 31
		iand
		istore 33

		iload 13
		iload 33
		iand
		istore 34

		iload 4
		iload 34
		iand
		istore 35

		iload 35
		ireturn

.end method
