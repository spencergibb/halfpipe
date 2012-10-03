package org.dstovall;

import java.util.HashMap;

import org.junit.Test;

public class OneJarMojo_ManifestEntriesTest {

	/**
	 * we might want to use more sensible tests,
	 * but for now I would just like to see whether new changes
	 * break older options.
	 */
	
	@Test
	public void whenManifestEntriesSpecifiedThenNoFailure() throws Exception {
		TestableMojo mojo = new TestableMojo().useDefaultTestArtifact();
		mojo.set("manifestEntries", new HashMap<String, String>());
		mojo.execute();
	}
	
	@Test
	public void whenManifestEntriesMissingThenNoFailure() throws Exception {
		TestableMojo mojo = new TestableMojo().useDefaultTestArtifact();
		mojo.execute();
	}
	
}
