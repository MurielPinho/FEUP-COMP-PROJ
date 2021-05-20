.class public myClass
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public static sum([I)I
		.limit stack 20 ; TBD
		.limit locals 7

		iconst_0
		istore_1

		iconst_0
		istore_2

	Loop:

		aload_0
		arraylength
		istore_3

		iload_2
		iload_3
		if_icmpge End

		iload_0
		istore 4

		iload_1
		iload 4
		iadd
		istore_1

		iinc 2 1

		goto Loop

	End:

		iload_1
		ireturn

.end method
