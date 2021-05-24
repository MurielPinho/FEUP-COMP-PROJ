.class public HelloWorld
.super java/lang/Object

; class fields
.field private a I

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public static main([Ljava/lang/String;)V
		.limit stack 2
		.limit locals 8
		invokestatic ioPlus/printHelloWorld()V


		iconst_2
		iconst_1
		iadd
		istore_1

		aload_0
		getfield I a
		istore_2

		iload_2
		istore_3

		return
.end method
