package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.FilenameFilter;

final class SubdirsFilter implements FilenameFilter {

	public boolean accept(final File dir, final String name) {
		return new File(dir, name).isDirectory() && !"WEB-INF".equals(name);
	}

}