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
.method public sum([I)I
		.limit stack 2
		.limit locals 7

		iconst_0
		istore_2

		iconst_0
		istore_3

	Loop:

		aload_1
		arraylength
		istore 4
		iload_3
		iload 4
		if_icmpge End

		iload_1
		istore 5

		iload_2
		iload 5
		iadd
		istore_2

		iinc 3 1

		goto Loop

	End:

		iload_2
		ireturn

.end method
