package com.github.freudExamples;

import static org.langera.freud.core.matcher.FreudDsl.no;
import static org.langera.freud.optional.javasource.JavaSourceDsl.importDeclarations;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Before;
import org.junit.Test;
import org.langera.freud.core.Freud;
import org.langera.freud.core.FreudAnalyser;
import org.langera.freud.core.iterator.AnalysedObjectIterator;
import org.langera.freud.core.iterator.resource.ResourceIterators;
import org.langera.freud.core.listener.AnalysisListener;
import org.langera.freud.core.listener.AssertionErrorAnalysisListener;
import org.langera.freud.optional.javasource.JavaSource;
import org.langera.freud.optional.javasource.JavaSourceJdom;

public class DevoxxExamplesTest {
	private static final boolean RECURSIVE = true;
	private static final String MAIN_SOURCES = new File("src/main/java/").getAbsolutePath();
	private static FilenameFilter ACCEPT_JAVA_FILES_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return  name.endsWith(".java");
		}
	};
	
	AnalysedObjectIterator<JavaSource> javaSources;
	
	@Before
	public void setup () {
		javaSources = ResourceIterators.filesByPathResourceIterator(JavaSourceJdom.PARSER, ACCEPT_JAVA_FILES_FILTER , RECURSIVE, MAIN_SOURCES);
	}
	
	
	@Test
	public void noDateUtil() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(JavaSource.class).
	        	assertThat(no(importDeclarations().matches("java.util.Date")))
	        	.in(javaSources);
		
			AnalysisListener assertionListener = new AssertionErrorAnalysisListener();
			analyser.analyse(assertionListener);
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Test
////	@Ignore
//	public void withTimeProvider() throws Exception {
//		FreudAnalyser analyser = Freud.iterateOver(JavaSource.class).
//			assertThat(no(importDeclarations().matches("java.util.Date")).or(JavaSourceDsl.simpleClassName().matches("TimeProvider"))).in(javaSources);
//		
//			AnalysisListener assertionListener = new AssertionErrorAnalysisListener();
//			analyser.analyse(assertionListener);
//	}
//	
//}
