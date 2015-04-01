package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubdirsFilterTest extends AbstractFilterTest {

	File testDir;
	File webInfDir;
	File testFile;
	File cssDir;

	@Before
	public void setup() throws IOException {
		testDir = new File("target", "testdir-" + UUID.randomUUID());
		testDir.mkdirs();
		webInfDir = new File(testDir, "WEB-INF");
		testFile = new File(testDir, "foo.txt");
		testFile.createNewFile();
		cssDir = new File(testDir, "css");
		cssDir.mkdirs();
	}

	@After
	public void cleanup() throws IOException {
		FileUtils.deleteDirectory(testDir);
	}

	@Test
	public void acceptWebInf() {
		Assert.assertThat("WEB-INF directory content should not be compressed",
				new SubdirsFilter().accept(testDir, "WEB-INF"),
				CoreMatchers.is(false));
	}

	@Test
	public void acceptOtherDirectory() {
		Assert.assertThat("directory content should be accepted",
				new SubdirsFilter().accept(testDir, "css"),
				CoreMatchers.is(true));
	}

}
