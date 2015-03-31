package com.github.k0zka.contentcompress;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class FilesToGzipFilterTest {
	
	@Test
	public void matchesAny() {
		Assert.assertThat(new FilesToGzipFilter(0, Arrays.asList(".svg")).matchesAny("kakukk.svg"), CoreMatchers.is(true));
		Assert.assertThat(new FilesToGzipFilter(0, Arrays.asList(".txt")).matchesAny("kakukk.svg"), CoreMatchers.is(false));
	}

}
