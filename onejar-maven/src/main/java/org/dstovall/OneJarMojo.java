package org.dstovall;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * Creates an executable one-jar version of the project's normal jar, including all dependencies.
 *
 * @goal one-jar
 * @phase package
 * @requiresProject
 * @requiresDependencyResolution runtime
 */
public class OneJarMojo extends AbstractMojo {
	
	private static final String MF_REQUIRED_IMPL_VERSION = "ImplementationVersion";
    private static final String MF_OPTION_MAIN_CLASS = "One-Jar-Main-Class";
    private static final String MF_OPTION_SPLASH_SCREEN_IMAGE = "SplashScreen-Image";

	/**
     * All the dependencies including trancient dependencies.
     *
     * @parameter default-value="${project.artifacts}"
     * @required
     * @readonly
     */
    private Collection<Artifact> artifacts;

    /**
     * All declared dependencies in this project, including system scoped dependencies.
     *
     * @parameter default-value="${project.dependencies}"
     * @required
     * @readonly
     */
    private Collection<Dependency> dependencies;

    /**
     * FileSet to be included in the "binlib" directory inside the one-jar. This is the place to include native
     * libraries such as .dll files and .so files. They will automatically be loaded by the one-jar.
     *
     * @parameter
     */
    private FileSet[] binlibs;

    /**
     * FileSet to be included in the "lib" directory inside the one-jar. This is the place to include other
     * files. They will automatically be loaded by the one-jar.
     *
     * @parameter
     */
    private FileSet[] additionalLibs;

    /**
     * The directory for the resulting file.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    /**
     * Name of the main JAR.
     *
     * @parameter expression="${project.build.finalName}.jar"
     * @readonly
     * @required
     */
    private String mainJarFilename;

    /**
     * Name of the generated JAR.
     * Will default to the final name if left unspecified.
     * Include the classifier if required, it will not be added automatically.
     *
     * @parameter expression="${project.build.finalName}.one-jar.jar"
     * @required
     */
    private String filename;

    // TODO: do we want to be strict and do a mutual exclusive check for version and local template?
    /**
     * The version of one-jar to use.  Has a default, so typically no need to specify this.
     *
     * @parameter expression="${onejar-version}" default-value="0.97"
     */
    private String onejarVersion;

    /**
     * A custom one-jar artifact to start from (RC build for example).
     * This must be a "one-jar-boot.jar" style artifact. 
     * If you specify this option, the onejarVersion will have no effect anymore.
     * This should be a path relative to the project basedir.
     * 
     * @parameter 
     */
    private String localOneJarTemplate;
    
    /**
     * Whether to attach the generated one-jar to the build. You may also wish to set <code>classifier</code>.
     *
     * @parameter default-value=false
     */
    private boolean attachToBuild;

    /**
     * Classifier to use, if the one-jar is to be attached to the build.
     * Set <code>&lt;attachToBuild&gt;true&lt;/attachToBuild&gt; if you want that.
     *
     * @parameter default-value="onejar" 
     */
    private String classifier;

    /**
     * This Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * For attaching artifacts etc.
     *
     * @component
     * @readonly
     */
    private MavenProjectHelper projectHelper;

    /**
     * The main class that one-jar should activate
     *
     * @parameter expression="${onejar-mainclass}"
     */
    private String mainClass;

    /**
     * The splash screen image.
     * This should be a path relative to the project basedir.
     * Adds the option to the manifest and copies and checks the image.
     * 
     * @parameter expression="${onejar-splashScreen}"
     */
    private String splashScreen;
    
    /**
     * Implementation Version of the jar. Defaults to the build's version.
     *
     * @parameter expression="${project.version}"
     * @required
     */
    private String implementationVersion;
    
	/**
	 * The entries to include as-is in the one-jar manifest.
	 * This is optional.
	 * Values used here will are leading and will not be overridden by other options.
	 * 
	 * @see <a href="http://one-jar.sourceforge.net/index.php?page=details&file=manifest">Documentation one the one-jar manifest options</a>.
	 * @parameter
	 */
	private Map<String,String> manifestEntries;
    
    public void execute() throws MojoExecutionException {

        // Show some info about the plugin.
        displayPluginInfo();

        JarOutputStream out = null;
        JarInputStream template = null;
        File onejarFile = null;
        try {
        	// Create the target file
            onejarFile = new File(outputDirectory, filename);

            // Prepare the onejar manifest file content
            Manifest manifest = prepareManifest();
            
            // Open a stream to write to the target file
			out = new JarOutputStream(new FileOutputStream(onejarFile, false), manifest);
        	
			// Add files (based on options)
            addFilesToArchive(out);
            
            // Finalize the onejar archive
            template = openOnejarTemplateArchive();
            copyTemplateFilesToArchive(template, out);
            
        } catch (IOException e) {
            error(e);
            throw new MojoExecutionException("One-jar Mojo failed.", e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(template);
        }

        // Attach the created one-jar to the build.
        if (attachToBuild) {
            projectHelper.attachArtifact(project, "jar", classifier, onejarFile);
        }
    }

	private void copyTemplateFilesToArchive(JarInputStream template,
			JarOutputStream out) throws IOException {
		// One-jar stuff
        debug("Adding one-jar components...");
		
		ZipEntry entry = null;
        while ((entry = template.getNextEntry()) != null) {
            // Skip the manifest file, no need to clutter...
            if (!"boot-manifest.mf".equals(entry.getName())) {
                addToZip(out, entry, template);
            }
        }
	}

	private void addFilesToArchive(JarOutputStream out) throws IOException, MojoExecutionException {
		final List<File> dependencyJars = Collections.unmodifiableList(extractDependencyFiles(artifacts));
        final List<File> systemDependencyJars = Collections.unmodifiableList(extractSystemDependencyFiles(dependencies));
		
        // Main jar
        debug("Adding main jar main/[%s]", mainJarFilename);
        addToZip(new File(outputDirectory, mainJarFilename), "main/", out);

        // Add all dependencies, including transient dependencies, but excluding system scope dependencies
        debug("Adding [%s] dependency libraries...", dependencyJars.size());
        for (File jar : dependencyJars) {
            addToZip(jar, "lib/", out);
        }

        // Add system scope dependencies
        debug("Adding [%s] system dependency libraries...", systemDependencyJars.size());
        for (File jar : systemDependencyJars) {
            addToZip(jar, "lib/", out);
        }

        // Add native libraries
        if (binlibs != null) {
            for (FileSet eachFileSet : binlibs) {
                List<File> includedFiles = toFileList(eachFileSet);
                debug("Adding [%s] native libraries...", includedFiles.size());
                for (File eachIncludedFile : includedFiles) {
                    addToZip(eachIncludedFile, "binlib/", out);
                }
            }
        }

        // Add native libraries
        if (additionalLibs != null) {
            for (FileSet eachFileSet : additionalLibs) {
                List<File> includedFiles = toFileList(eachFileSet);
                debug("Adding [%s] additional libraries...", includedFiles.size());
                for (File eachIncludedFile : includedFiles) {
                    addToZip(eachIncludedFile, "lib/", out);
                }
            }
        }

        // Add splash screen image
        if (splashScreen != null) {
        	File splashFile = new File(project.getBasedir(), splashScreen);
        	if (splashFile.exists()) {
        		debug("Adding splash screen image [%s]", splashScreen);
        		addToZip(out, new ZipEntry(splashScreen), new FileInputStream(splashFile));
        	} else {
        		throw new MojoExecutionException("Could not find splash screen image defined in pom.");
        	}
        }

	}

	private void displayPluginInfo() {
        info("Using One-Jar to create a single-file distribution");
        info("Implementation Version: %s", implementationVersion);
        if (localOneJarTemplate != null) {
        	info("Using local One-Jar template: %s", localOneJarTemplate);
        } else {
        	info("Using One-Jar version: %s", onejarVersion);
        }
        info("More info on One-Jar: http://one-jar.sourceforge.net/");
        info("License for One-Jar:  http://one-jar.sourceforge.net/one-jar-license.txt");
        info("One-Jar file: %s", outputDirectory.getAbsolutePath() + File.separator + filename);
    }

    // ----- One-Jar Template ------------------------------------------------------------------------------------------

    private String getOnejarArchiveName() {
        return "one-jar-boot-" + onejarVersion + ".jar";
    }

    private JarInputStream openOnejarTemplateArchive() throws IOException {
    	if (localOneJarTemplate != null) {
    		return new JarInputStream(new FileInputStream(new File(project.getBasedir(), localOneJarTemplate)));
    	} else {    	
    		return new JarInputStream(getClass().getClassLoader().getResourceAsStream(getOnejarArchiveName()));
    	}
    }
    
    private class AttributeEntry extends AbstractMap.SimpleEntry<String, String> {
		private static final long serialVersionUID = 843323303047092453L;
    	public AttributeEntry(String key, String value) {
			super(key, value);
		}
	}
    
    private Manifest prepareManifest() throws IOException {
        // Copy the template's boot-manifest.mf file
        ZipInputStream zipIS = openOnejarTemplateArchive();
        Manifest manifest = new Manifest(getFileBytes(zipIS, "boot-manifest.mf"));
        IOUtils.closeQuietly(zipIS);

        Attributes mainAttributes = manifest.getMainAttributes();
        // first add the custom specified entries
        addExplicitManifestEntries(mainAttributes);

        // If the client has specified an implementationVersion argument, add it also
        // (It's required and defaulted, so this always executes...)
        //
        // TODO: The format of this manifest entry is not "hard and fast".  Some specs call for "implementationVersion",
        // some for "implemenation-version", and others use various capitalizations of these two.  It's likely that a
        // better solution then this "brute-force" bit here is to allow clients to configure these entries from the
        // Maven POM.
        setRequired(mainAttributes,
        		new AttributeEntry(MF_REQUIRED_IMPL_VERSION, implementationVersion),
        		MF_REQUIRED_IMPL_VERSION);

        // If the client has specified a splashScreen argument, add the proper entry to the manifest
        setOptional(splashScreen, mainAttributes,
        		new AttributeEntry(MF_OPTION_SPLASH_SCREEN_IMAGE, splashScreen),
        		MF_OPTION_SPLASH_SCREEN_IMAGE);
        
        // If the client has specified a mainClass argument, add the proper entry to the manifest
        // to be backwards compatible, add mainclass as simple option when not already set in manifestEntries
        setOptional(mainClass, mainAttributes,
        		new AttributeEntry(MF_OPTION_MAIN_CLASS, mainClass),
        		MF_OPTION_MAIN_CLASS);

        return manifest;
    }

    private void setRequired(Attributes mainAttributes,
			AttributeEntry keyPairToSet, String... entryNamesForValue) {
    	// just call as if it were optional, but always applies
    	setOptional(Boolean.TRUE, mainAttributes, keyPairToSet, entryNamesForValue);
	}

	/**
     * Adds option to manifest entries, if applicable.
     * @param optionToCheck to perform null check, if null then nothing added
     * @param mainAttributes to add to
     * @param keyPairToSet both the key and value to add if applicable
     * @param entryNamesForValue keys to look for to prevent duplicating or overwriting keys
     */
    private void setOptional(Object optionToCheck, Attributes mainAttributes, AttributeEntry keyPairToSet,
			String... entryNamesForValue) {
		if (optionToCheck != null) {
			for (String keyToCheck : entryNamesForValue) {
				if (mainAttributes.containsKey(keyToCheck)) {
					// key is already set, don't override
					return;
				}
			}
			// if key not found, add it
			mainAttributes.putValue(keyPairToSet.getKey(), keyPairToSet.getValue());
		}
	}

	private void addExplicitManifestEntries(Attributes mainAttributes) {
    	// add explicitly specified manifest entries
        if (manifestEntries != null) {
	        for (Entry<String, String> entry : manifestEntries.entrySet()) {
	        	debug("adding entry [%s:%s] to the one-jar manifest", entry.getKey(), entry.getValue());
	        	mainAttributes.putValue(entry.getKey(), entry.getValue());
	        }
        }
	}

	// ----- Zip-file manipulations ------------------------------------------------------------------------------------

    private void addToZip(File sourceFile, String zipfilePath, JarOutputStream out) throws IOException {
        addToZip(out, new ZipEntry(zipfilePath + sourceFile.getName()), new FileInputStream(sourceFile));
    }

    private final AtomicInteger alternativeEntryCounter = new AtomicInteger(0);
    private void addToZip(JarOutputStream out, ZipEntry entry, InputStream in) throws IOException {
        try{
            out.putNextEntry(entry);
            IOUtils.copy(in, out);
            out.closeEntry();
        }catch(ZipException e){
            if (e.getMessage().startsWith("duplicate entry")){
                // A Jar with the same name was already added. Let's add this one using a modified name:
                final ZipEntry alternativeEntry = new ZipEntry(entry.getName() + "-DUPLICATE-FILENAME-" + alternativeEntryCounter.incrementAndGet() + ".jar");
                addToZip(out, alternativeEntry, in);
            }else{
                throw e;
            }
        }
    }

    private InputStream getFileBytes(ZipInputStream is, String name) throws IOException {
        ZipEntry entry = null;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals(name)) {
                byte[] data = IOUtils.toByteArray(is);
                return new ByteArrayInputStream(data);
            }
        }
        return null;
    }

    /**
     * Returns a {@link File} object for each artifact.
     *
     * @param artifacts Pre-resolved artifacts
     * @return <code>File</code> objects for each artifact.
     */
    private List<File> extractDependencyFiles(Collection<Artifact> artifacts) {
        List<File> files = new ArrayList<File>();

        if (artifacts == null){
            return files;
        }

        for (Artifact artifact : artifacts) {
            File file = artifact.getFile();

            if (file.isFile()) {
                files.add(file);
            }

        }
        return files;
    }

    /**
     * Returns a {@link File} object for each system dependency.
     * @param systemDependencies a collection of dependencies
     * @return <code>File</code> objects for each system dependency in the supplied dependencies.
     */
    private List<File> extractSystemDependencyFiles(Collection<Dependency> systemDependencies) {
        final ArrayList<File> files = new ArrayList<File>();

        if (systemDependencies == null){
            return files;
        }

        for (Dependency systemDependency : systemDependencies) {
            if (systemDependency != null && "system".equals(systemDependency.getScope())){
                files.add(new File(systemDependency.getSystemPath()));
            }
        }
        return files;
    }

    @SuppressWarnings("unchecked")
	private static List<File> toFileList(FileSet fileSet)
            throws IOException {
        File directory = new File(fileSet.getDirectory());
        String includes = toString(fileSet.getIncludes());
        String excludes = toString(fileSet.getExcludes());
        return FileUtils.getFiles(directory, includes, excludes);
    }

    private static String toString(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(string);
        }
        return sb.toString();
    }

    private void error(IOException e) {
		getLog().error(e);
	}

	private void debug(String msgTemplate, Object... values) {
    	if (getLog().isDebugEnabled()) {
            getLog().debug(String.format(msgTemplate, values));
        }
	}

    private void info(String msgTemplate, Object... values) {
        getLog().info(String.format(msgTemplate, values));
	}
    
}
