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
		istore_0

		iconst_0
		istore_1

		iconst_0
		istore_2

	Loop:
		iload_0
		iload_2
		if_icmple endif

		iload_1
		bipush 8
		iadd
		istore_1

		iinc 2 1

		goto Loop

	endif:
		iload_1
		invokestatic io/println(I)V


		return
.end method
