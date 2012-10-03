package org.dstovall;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.DefaultMavenProjectHelper;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Ignore;

/**
 * Convenient testing variation of the one-jar mojo.
 * 
 * @author bwijsmuller
 */
@Ignore
class TestableMojo extends OneJarMojo {
	
	private static final String defaultFinalName = "default-finalname";
	
	private final MavenProject project;
	private final MavenProjectHelper projectHelper;
	private final List<Dependency> dependencies;
	private final List<Artifact> artifacts;
	private File defaultArtifact;
	private File projectTargetForTest;
	
	TestableMojo() {
		projectTargetForTest = new File(System.getProperty("java.io.tmpdir")+"/one-jar-test/");
		projectTargetForTest.mkdirs();
		projectTargetForTest.deleteOnExit();
		
		project = new MavenProject();
		projectHelper = new DefaultMavenProjectHelper();
		artifacts = new ArrayList<Artifact>();
		dependencies = new ArrayList<Dependency>();
		
		set("mainJarFilename", defaultFinalName + ".jar");

		set("dependencies", dependencies);
		set("implementationVersion", "1.0-test");
		set("outputDirectory", projectTargetForTest);
		set("filename", defaultFinalName+".one-jar.jar");
		set("classifier", "onejar");
		set("attachToBuild", false);
		set("onejarVersion", "0.97");
		set("project", project);
		set("projectHelper", projectHelper);
	}
	
	void addArtifacts(List<Artifact> arts) {
		artifacts.addAll(arts);
	}
	
	void addDependencies(List<Dependency> deps) {
		dependencies.addAll(deps);
	}
	
	/**
	 * Sets a (private) field on the mojo using reflection.
	 * 
	 * @param optionName the field name
	 * @param value the value to set
	 */
	void set(String optionName, Object value) {
		try {
			ReflectionUtils.setVariableValueInObject(this, optionName, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public MavenProject getProject() {
		return project;
	}
	
	public MavenProjectHelper getProjectHelper() {
		return projectHelper;
	}
	
	/**
	 * Produces a file in the test target directory that can act as
	 * the default artifact generated from the maven build.
	 * @return this mojo
	 */
	public TestableMojo useDefaultTestArtifact() {
		defaultArtifact = new File(projectTargetForTest, defaultFinalName+".jar");
		try {
			defaultArtifact.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("Could not create default test artifact.", e);
		}
		return this;
	}
	
}