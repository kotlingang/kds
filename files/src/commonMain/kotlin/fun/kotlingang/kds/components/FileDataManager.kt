package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.manager.AsyncContentDataManager
import `fun`.kotlingang.kds.files.CommonFileInterface


internal expect class FileDataManager(file: CommonFileInterface) : AsyncContentDataManager
