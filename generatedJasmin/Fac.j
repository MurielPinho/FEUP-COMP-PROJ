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
.method public static compFac(I)I
		.limit stack 2
		.limit locals 7
		iload_0

		iconst_1
		if_icmpge else

		iconst_1
		istore_1

		goto endif

	else:

		iload_0
		iconst_1
		isub
		istore_2

		iload_2
		aload_0
		invokevirtual compFac(I)V


		iload_0
		iload_3
		imul
		istore_1

	endif:

		iload_1
		ireturn

.end method

.method public static main([Ljava/lang/String;)V
		.limit stack 2
		.limit locals 8

		new Fac
		dup
		invokespecial <init>()V


		iconst_2
		aload_2
		invokevirtual put(I)V


		bipush 10
		aload_0
		invokevirtual compFac(I)V

		iload_3
		invokestatic io/println(I)V


		return
.end method
