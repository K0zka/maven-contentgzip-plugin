package com.github.k0zka.contentcompress

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

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.ObjectUtils
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

/**
 * This goal creates a gzip-ed file from each file in the webapp that matches
 * the filename pattern.
 *
 * @goal process
 *
 * @phase prepare-package
 */
@Mojo(name = "process", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresProject = true, requiresOnline = false)
class ContentGzipMojo : AbstractMojo() {
	/**
	 * Location of the webapp.
	 *
	 * @parameter expression="src/main/webapp"
	 * @required
	 */
	@Parameter
	internal var webappDirectory: File? = null

	/**
	 * Location of the file.
	 *
	 * @parameter
	 * expression="${project.build.directory}/${project.build.finalName}"
	 * @required
	 */
	@Parameter
	internal var outputDirectory: File? = null
	/**
	 * File name suffixes to handle with the gzip compression.
	 *
	 * @parameter
	 */
	@Parameter
	internal var extensions = defaultExtensions

	/**
	 * The minimal size for files to compress.
	 *
	 * @parameter
	 */
	@Parameter
	private val minSize: Long = 0

	/**
	 * An option to stop the plugin doing the compression recursively on the whole webapp directory.
	 *
	 * @parameter
	 */
	@Parameter
	internal var recursive = true

	private val defaultExtensions: List<String>
		get() =
			listOf(".css", ".txt", ".html", ".htm", ".js", ".xml", ".xls", ".ico", ".svg", ".pdf", ".doc")

	@Throws(MojoExecutionException::class)
	override fun execute() {
		log.info("Compressing static resources with gzip...")
		try {
			if (!ObjectUtils.equals(webappDirectory, outputDirectory)) {
				FileUtils.copyDirectory(
						webappDirectory!!, outputDirectory!!,
						DotNotFilter())
			}
			seekAndGzip(outputDirectory!!)
		} catch (e: IOException) {
			throw MojoExecutionException(
					"IO exception when gzipping files", e)
		}

	}

	@Throws(IOException::class)
	internal fun seekAndGzip(directory: File) {
		val fileNames = directory.list(
				FilesToGzipFilter(
						minSize, extensions))
		if (fileNames == null) {
			log.error(
					"Directory does not exist: " + directory.absolutePath)
			return
		}
		for (fileName in fileNames) {
			compress(directory, fileName)
		}
		if (recursive) {
			val subDirs = directory.list(SubdirsFilter())
			for (subDir in subDirs!!) {
				seekAndGzip(File(directory, subDir))
			}
		}
	}

	@Throws(IOException::class)
	internal fun compress(directory: File, fileName: String) {
		val sourceFile = File(directory, fileName)
		val gzippedFile = File(directory, "$fileName.gz")
		if (gzippedFile.exists() && gzippedFile.lastModified() > sourceFile.lastModified()) {
			log.info("Skipped file " + sourceFile.name + ".gz is up to date")
			return
		}
		FileInputStream(sourceFile).use { inputStream ->
			GZIPOutputStream(FileOutputStream(gzippedFile)).use { gzipStream ->
				inputStream.copyTo(gzipStream)
			}
		}
		val sourceLength = sourceFile.length()
		val gzipedLength = gzippedFile.length()
		if (sourceLength > gzipedLength) {
			log.info(
					"Compressed file " + sourceFile.name + ". "
							+ sourceLength + " -> "
							+ gzipedLength)
		} else {
			log.info(
					"Compressed file " + sourceFile.name + ". "
							+ sourceLength + " -> "
							+ gzipedLength + " removing, gzipped version is bigger")
			gzippedFile.delete()
		}
	}

}
