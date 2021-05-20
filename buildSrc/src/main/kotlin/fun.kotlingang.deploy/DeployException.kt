/**
 * Throws when some error is occurred while deploying.
 * @param message - error details.
 */
class DeployException(message: String) : Exception(message)