.class public VarLimits
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
		.limit stack 20 ; TBD
		.limit locals 7

		iconst_2
		istore_0

		iconst_3
		istore_1

		iconst_4
		istore_2

		iconst_5
		istore_3

		return
.end method
