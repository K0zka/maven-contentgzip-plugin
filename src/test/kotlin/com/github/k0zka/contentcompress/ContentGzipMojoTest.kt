package com.github.k0zka.contentcompress

import java.io.File
import java.io.IOException

import org.apache.commons.io.FileDeleteStrategy
import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test

import com.github.k0zka.contentcompress.ContentGzipMojo

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse

class ContentGzipMojoTest {

	internal lateinit var basedir: File

	@Before
	@Throws(IOException::class)
	fun createWebappDirectoryStructure() {
		basedir = File("target/testdir")
		if (basedir.exists()) {
			FileDeleteStrategy.FORCE.delete(basedir)
		}
		basedir.mkdirs()
		FileUtils.copyDirectory(File("src/test/resources"), basedir)
	}

	@Test
	@Throws(IOException::class)
	fun testSeekAndGzip() {
		ContentGzipMojo().seekAndGzip(basedir)
		assertTrue(File(basedir, "skins/default.css.gz").exists())
		assertTrue(File(basedir, "index.html.gz").exists())
	}

	@Test
	@Throws(IOException::class)
	fun testSeekAndGzipNonRecursive() {
		val gzipMojo = ContentGzipMojo()
		gzipMojo.recursive = false
		gzipMojo.seekAndGzip(basedir)
		assertFalse(File(basedir, "skins/default.css.gz").exists())
		assertTrue(File(basedir, "index.html.gz").exists())
	}

}
