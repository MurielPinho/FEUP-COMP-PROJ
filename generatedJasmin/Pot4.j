.class public Pot4
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public func(I)I
		.limit stack 2
		.limit locals 6

		iconst_0
		istore_2

	Loop1:

		iconst_1

		iconst_1
		iand
		if_icmpeq Body1

		goto EndLoop1

	Body1:

		iconst_1
		iconst_0
		iand
		istore_3
		iload_3

		iconst_0
		if_icmpeq else1

	Loop2:
		iload_2

		iconst_3
		if_icmplt Body2

		goto EndLoop2

	EndLoop2:

	Body2:

		goto endif1

	else1:

		bipush 8
		istore_2

	EndLoop1:

	endif1:

		iload_2
		istore 4

		iload 4
		ireturn

.end method
