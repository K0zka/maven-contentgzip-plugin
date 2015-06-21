package com.github.k0zka.contentcompress;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * This goal creates a gzip-ed file from each file in the webapp that matches
 * the filename pattern.
 * 
 * @goal process
 * 
 * @phase prepare-package
 */
public class ContentGzipMojo extends AbstractMojo {
	/**
	 * Location of the webapp.
	 * 
	 * @parameter expression="src/main/webapp"
	 * @required
	 */
	private File webappDirectory;

	/**
	 * Location of the file.
	 * 
	 * @parameter 
	 *            expression="${project.build.directory}/${project.build.finalName}"
	 * @required
	 */
	private File outputDirectory;
	/**
	 * File name suffixes to handle with the gzip compression.
	 * 
	 * @parameter
	 */
	private List<String> extensions = getDefaultExtensions();

	/**
	 * The minimal size for files to compress.
	 * 
	 * @parameter
	 */
	private long minSize = 0;

	public void execute() throws MojoExecutionException {
		getLog().info("Compressing static resources with gzip...");
		try {
			if (!ObjectUtils.equals(webappDirectory, outputDirectory)) {
				FileUtils.copyDirectory(webappDirectory, outputDirectory,
						new DotNotFilter());
			}
			seekAndGzip(outputDirectory);
		} catch (IOException e) {
			throw new MojoExecutionException(
					"IO exception when gzipping files", e);
		}
	}

	List<String> getDefaultExtensions() {
		final ArrayList<String> ret = new ArrayList<String>();
		ret.add(".css");
		ret.add(".txt");
		ret.add(".html");
		ret.add(".htm");
		ret.add(".js");
		ret.add(".xml");
		ret.add(".xls");
		ret.add(".ico");
		ret.add(".svg");
		ret.add(".pdf");
		ret.add(".doc");
		return ret;
	}

	void seekAndGzip(final File directory) throws IOException {
		final String[] fileNames = directory.list(new FilesToGzipFilter(
				minSize, extensions));
		if (fileNames == null) {
			getLog().error(
					"Directory does not exist: " + directory.getAbsolutePath());
			return;
		}
		for (final String fileName : fileNames) {
			compress(directory, fileName);
		}
		final String[] subDirs = directory.list(new SubdirsFilter());
		for (final String subDir : subDirs) {
			seekAndGzip(new File(directory, subDir));
		}
	}

	void compress(final File directory, final String fileName) throws IOException {
		final File sourceFile = new File(directory, fileName);
		final File gzippedFile = new File(directory, fileName.concat(".gz"));
		if (gzippedFile.exists() && gzippedFile.lastModified() > sourceFile.lastModified()) {
			getLog().info("Skipped file " + sourceFile.getName() +" .gz is up to date");
			return;
		}
		final FileInputStream inputStream = new FileInputStream(sourceFile);
		final FileOutputStream fileStream = new FileOutputStream(
				gzippedFile);
		final GZIPOutputStream gzipStream = new GZIPOutputStream(fileStream);
		IOUtils.copy(inputStream, gzipStream);
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(gzipStream);
		IOUtils.closeQuietly(fileStream);
		final long sourceLength = sourceFile.length();
		final long gzipedLength = gzippedFile.length();
		if(sourceLength > gzipedLength) {
			getLog().info(
					"Compressed file " + sourceFile.getName() + ". "
							+ sourceLength + " -> "
							+ gzipedLength);
		} else {
			getLog().info(
					"Compressed file " + sourceFile.getName() + ". "
							+ sourceLength + " -> "
							+ gzipedLength + " removing, gzipped version is bigger");
			gzippedFile.delete();
		}
	}

}
