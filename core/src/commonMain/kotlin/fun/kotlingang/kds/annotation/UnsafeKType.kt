package `fun`.kotlingang.kds.annotation


@RequiresOptIn (
    message = """
        This function uses unsafe `KType to value` association, so you can provide 
        KType associated with one type and value with different type, be careful!
    """,
    level = RequiresOptIn.Level.WARNING
)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
public annotation class UnsafeKType
