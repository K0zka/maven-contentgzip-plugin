package com.github.k0zka.contentcompress

import java.io.File
import java.io.FilenameFilter
import java.util.ArrayList

internal class FilesToGzipFilter(
		private val minSize: Long,
		extensions: List<String>
) : FilenameFilter {
	private val extensions: List<String>

	init {
		this.extensions = ArrayList(extensions)
	}

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