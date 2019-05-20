package com.github.k0zka.contentcompress

import java.io.File
import java.io.FilenameFilter

internal class SubdirsFilter : FilenameFilter {

	override fun accept(dir: File, name: String): Boolean {
		return File(dir, name).isDirectory && "WEB-INF" != name
	}

}