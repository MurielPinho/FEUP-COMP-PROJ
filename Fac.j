.class public Fac
.super java/lang/Object

; class fields

; standard initializer
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

; methods
.method public compFac(I)I
		.limit stack 2
		.limit locals 7
		iload_1

		iconst_1
		if_icmpge else

		iconst_1
		istore_2

		goto endif

	else:

		iload_1
		iconst_1
		isub
		istore_3

		iload_3
		aload_0
		invokevirtual compFac(I)V


		iload_1
		iload 4
		imul
		istore_2

	endif:

		iload_2
		ireturn

.end method

.method public static main([Ljava/lang/String;)V
		.limit stack 2
		.limit locals 8

		new Fac
		dup
		invokespecial <init>()V


		iconst_2
		aload_3
		invokevirtual put(I)V


		bipush 10
		aload_1
		invokevirtual compFac(I)V

		iload 4
		invokestatic io/println(I)V


		return
.end method
