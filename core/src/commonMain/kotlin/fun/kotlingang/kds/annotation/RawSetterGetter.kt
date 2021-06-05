package `fun`.kotlingang.kds.annotation


@RequiresOptIn (
    message = """
        The function is a raw getter or setter, you would probably use type-safe delegate instead of it.
        The true way for this library is to use setters only inside delegates, so please write your own or 
        create an issue if you missed one.
    """,
    level = RequiresOptIn.Level.WARNING
)
annotation class RawSetterGetter
