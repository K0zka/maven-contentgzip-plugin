package com.github.k0zka.contentcompress

import java.io.File
import java.io.FilenameFilter

internal class FilesToGzipFilter(
	private val minSize: Long,
	private val extensions: List<String>
) : FilenameFilter {
	override fun accept(dir: File, name: String): Boolean {
		val file = File(dir, name)
		return (matchesAny(name) && file.isFile
				&& file.length() >= minSize)
	}

	fun matchesAny(fileName: String): Boolean {
		for (pattern in extensions) {
			if (fileName.endsWith(pattern)) {
				return true
			}
		}
		return false
	}
}