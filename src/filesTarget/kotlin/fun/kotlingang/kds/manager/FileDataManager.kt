package `fun`.kotlingang.kds.manager

import `fun`.kotlingang.kds.files.CommonFileInterface


internal expect class FileDataManager(file: CommonFileInterface) : AsyncDataManager
