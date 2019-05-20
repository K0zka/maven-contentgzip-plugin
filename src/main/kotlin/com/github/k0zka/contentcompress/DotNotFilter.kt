package com.github.k0zka.contentcompress

import java.io.File
import java.io.FileFilter

internal class DotNotFilter : FileFilter {
	override fun accept(file: File): Boolean {
		return !file.name.startsWith(".")
	}
}