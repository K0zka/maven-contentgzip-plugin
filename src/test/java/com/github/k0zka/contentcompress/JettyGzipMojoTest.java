package com.github.k0zka.contentcompress;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.k0zka.contentcompress.JettyGzipMojo;

public class JettyGzipMojoTest {

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
		new JettyGzipMojo().seekAndGzip(basedir);
	}
	
}
