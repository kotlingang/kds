package `fun`.kotlingang.kds.components

import `fun`.kotlingang.kds.data_manager.AsyncContentDataManager
import `fun`.kotlingang.kds.files.CommonFileInterface


internal expect class FileDataManager(file: CommonFileInterface) : AsyncContentDataManager
