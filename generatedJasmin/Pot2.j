.class public Pot2
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
		.limit locals 13

		iload_0
		istore_1

		iload 4
		istore 5

		iconst_4
		iload 5
		ilth
		istore 6

		iload_3
		iload 6
		iandb
		istore 7

		iload_1
		iload 7
		iandb
		istore 8

		return
.end method

.method public static expr1()I
		.limit stack 20 ; TBD
		.limit locals 6

		iconst_2
		istore_0

		iconst_3
		istore_1

		iconst_0
		istore_2

		iload_0
		iload_1
		ilth
		istore_3

		iload_3
		iload_2
		iandb
		istore 4

		iload_4
		ireturn

.end method

.method public static expr2()I
		.limit stack 20 ; TBD
		.limit locals 16

		iload_1
		iload_2
		imul
		istore_0

		iload_0
		iload 6
		isub
		istore 5

		iload 5
		iload_3
		ilth
		istore 7

		iload 9
		iload 10
		iandb
		istore 8

		iload 13
		iload 11
		iandb
		istore 12

		iload 7
		iload 12
		iandb
		istore 14

		iload_14
		ireturn

.end method

.method public static expr3()I
		.limit stack 20 ; TBD
		.limit locals 36

		iload_1
		iload_2
		imul
		istore_0

		iload 4
		iload_0
		ilth
		istore_3

		aload_0
		iload 6
		iaload
		istore 5

		iload 5
		istore 7

		iload 7
		iload 9
		iandb
		istore 8

		iload 8
		istore 10

		iload 11
		istore 12

		iload 14
		iload 15
		idiv
		istore 13

		iload 13
		iload 17
		iadd
		istore 16

		iload 19
		iload 20
		isub
		istore 18

		iload 16
		iload 18
		ilth
		istore 21

		iload 24
		istore 23

		iload 23
		istore 25

		iload 25
		istore 26

		iload 26
		iload 27
		iandb
		istore 29

		iload 31
		iload 29
		iandb
		istore 30

		iload 22
		iload 30
		iandb
		istore 32

		iload 12
		iload 32
		iandb
		istore 33

		iload_3
		iload 33
		iandb
		istore 34

		iload_34
		ireturn

.end method
