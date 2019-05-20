package com.github.k0zka.contentcompress

import java.io.File
import java.io.IOException
import java.util.UUID

import org.apache.commons.io.FileUtils
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DotNotFilterTest {

	private lateinit var testDir: File
	private lateinit var gitDir: File
	private lateinit var cssDir: File

	@Before
	@Throws(IOException::class)
	fun setup() {
		testDir = File("target", "testdir-" + UUID.randomUUID())
		testDir.mkdirs()
		gitDir = File(testDir, ".git")
		gitDir.mkdirs()
		cssDir = File(testDir, "css")
		cssDir.mkdirs()
	}

	@After
	@Throws(IOException::class)
	fun cleanup() {
		FileUtils.deleteDirectory(testDir)
	}

	@Test
	fun accept() {
		Assert.assertThat(
				"should not accept a .something directory, it is considered a 'hidden' directory on unix-like systems",
				DotNotFilter().accept(gitDir), CoreMatchers.`is`(false))
		Assert.assertThat(
				"should accept directoryes not starting with dot", DotNotFilter().accept(cssDir),
				CoreMatchers.`is`(true))
	}
}
