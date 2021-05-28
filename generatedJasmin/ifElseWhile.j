.class public ifElseWhile
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
		.limit locals 7

		bipush 16
		istore_1

		iconst_0
		istore_2

		iconst_0
		istore_3

	Loop:
		iload_1
		iload_3
		if_icmple endif

		iload_2
		bipush 8
		iadd
		istore_2

		iinc 3 1

		goto Loop

	endif:
		iload_2
		invokestatic io/println(I)V


		return
.end method
