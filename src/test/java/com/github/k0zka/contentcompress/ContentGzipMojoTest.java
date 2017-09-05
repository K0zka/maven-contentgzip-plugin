package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.k0zka.contentcompress.ContentGzipMojo;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContentGzipMojoTest {

	File basedir;
	
	@Before
	public void createWebappDirectoryStructure() throws IOException {
		basedir = new File("target/testdir");
		if(basedir.exists()) {
			FileDeleteStrategy.FORCE.delete(basedir);
		}
		basedir.mkdirs();
		FileUtils.copyDirectory(new File("src/test/resources"), basedir);
	}

	@Test
	public void testSeekAndGzip() throws IOException {
		new ContentGzipMojo().seekAndGzip(basedir);
		assertTrue(new File(basedir, "skins/default.css.gz").exists());
		assertTrue(new File(basedir, "index.html.gz").exists());
	}

	@Test
	public void testSeekAndGzipNonRecursive() throws IOException {
		ContentGzipMojo gzipMojo = new ContentGzipMojo();
		gzipMojo.recursive = false;
		gzipMojo.seekAndGzip(basedir);
		assertFalse(new File(basedir, "skins/default.css.gz").exists());
		assertTrue(new File(basedir, "index.html.gz").exists());
	}

}
