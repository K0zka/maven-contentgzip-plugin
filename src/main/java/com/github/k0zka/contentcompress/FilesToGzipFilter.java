package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

final class FilesToGzipFilter implements FilenameFilter {

	private final long minSize;
	private final List<String> extensions;

	public FilesToGzipFilter(final long minSize,
			final List<String> extensions) {
		this.minSize = minSize;
		this.extensions = new ArrayList<String>(extensions);
	}

	public boolean accept(File dir, String name) {
		final File file = new File(dir, name);
		return matchesAny(name) && file.isFile()
				&& file.length() >= minSize;
	}

	boolean matchesAny(final String fileName) {
		for (final String pattern : extensions) {
			if (fileName.endsWith(pattern)) {
				return true;
			}
		}
		return false;
	}
}