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

public class DotNotFilterTest {

	File testDir;
	File gitDir;
	File cssDir;

	@Before
	public void setup() throws IOException {
		testDir = new File("target", "testdir-" + UUID.randomUUID());
		testDir.mkdirs();
		gitDir = new File(testDir, ".git");
		gitDir.mkdirs();
		cssDir = new File(testDir, "css");
		cssDir.mkdirs();
	}

	@After
	public void cleanup() throws IOException {
		FileUtils.deleteDirectory(testDir);
	}

	@Test
	public void accept() {
		Assert.assertThat(
				"should not accept a .something directory, it is considered a 'hidden' directory on unix-like systems",
				new DotNotFilter().accept(gitDir), CoreMatchers.is(false));
		Assert.assertThat("should accept directoryes not starting with dot", new DotNotFilter().accept(cssDir),
				CoreMatchers.is(true));
	}
}
