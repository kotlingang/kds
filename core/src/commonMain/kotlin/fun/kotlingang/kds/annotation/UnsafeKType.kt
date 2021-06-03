package `fun`.kotlingang.kds.annotation


@RequiresOptIn(message = """
    This function uses unsafe `KType to value` association, so you can provide 
    KType associated with Int and value with different type, be careful!
""")
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
annotation class UnsafeKType
