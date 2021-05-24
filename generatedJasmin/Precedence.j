.class public Precedence
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

		iconst_2
		istore_0

		iconst_4
		istore_1

		bipush 8
		istore_2

		bipush 16
		istore_3

		bipush 32
		istore 4

		iload_2
		iload_3
		imul
		istore 5

		iload 5
		iload_1
		idiv
		istore 6

		iload 4
		iconst_2
		isub
		istore 7

		iload 6
		iload 7
		iadd
		istore 8

		iload_0
		iload 8
		iadd
		istore 9
		iload 9
		invokestatic io/println(I)V


		return
.end method
