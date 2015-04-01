package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.FileFilter;

final class DotNotFilter implements FileFilter {
	public boolean accept(File file) {
		return !file.getName().startsWith(".");
	}
}