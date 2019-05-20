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

class SubdirsFilterTest : AbstractFilterTest() {

	private lateinit var testDir: File
	private lateinit var webInfDir: File
	private lateinit var testFile: File
	private lateinit var cssDir: File

	@Before
	@Throws(IOException::class)
	fun setup() {
		testDir = File("target", "testdir-" + UUID.randomUUID())
		testDir.mkdirs()
		webInfDir = File(testDir, "WEB-INF")
		testFile = File(testDir, "foo.txt")
		testFile.createNewFile()
		cssDir = File(testDir, "css")
		cssDir.mkdirs()
	}

	@After
	@Throws(IOException::class)
	fun cleanup() {
		FileUtils.deleteDirectory(testDir)
	}

	@Test
	fun acceptWebInf() {
		Assert.assertThat(
				"WEB-INF directory content should not be compressed",
				SubdirsFilter().accept(testDir, "WEB-INF"),
				CoreMatchers.`is`(false))
	}

	@Test
	fun acceptOtherDirectory() {
		Assert.assertThat(
				"directory content should be accepted",
				SubdirsFilter().accept(testDir, "css"),
				CoreMatchers.`is`(true))
	}

}
