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
		.limit locals 15

		iconst_2
		istore_1

		iconst_4
		istore_2

		bipush 8
		istore_3

		bipush 16
		istore 4

		bipush 32
		istore 5

		iload_3
		iload 4
		imul
		istore 6

		iload 6
		iload_2
		idiv
		istore 7

		iload 5
		iconst_2
		isub
		istore 8

		iload 7
		iload 8
		iadd
		istore 9

		iload_1
		iload 9
		iadd
		istore 10

		iload 10
		istore 11
		iload 11
		invokestatic io/println(I)V


		return
.end method
