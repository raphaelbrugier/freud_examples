package com.github.freudExamples;

import static org.langera.freud.core.matcher.FreudDsl.no;
import static org.langera.freud.optional.classfile.method.ClassFileMethodDsl.hasMethodInvocation;
import static org.langera.freud.optional.javasource.block.CodeBlockDsl.codeBlockLines;
import static org.langera.freud.optional.javasource.importdecl.ImportDeclarationDsl.importDeclaration;

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
import org.langera.freud.optional.classfile.ClassFile;
import org.langera.freud.optional.classfile.method.ClassFileMethod;
import org.langera.freud.optional.classfile.parser.asm.AsmClassFileParser;
import org.langera.freud.optional.javasource.JavaSource;
import org.langera.freud.optional.javasource.JavaSourceJdom;
import org.langera.freud.optional.javasource.block.CodeBlock;
import org.langera.freud.optional.javasource.block.CodeBlockDsl;
import org.langera.freud.optional.javasource.importdecl.ImportDeclaration;
import org.langera.freud.optional.javasource.packagedecl.PackageDeclaration;
import org.langera.freud.optional.javasource.packagedecl.PackageDeclarationDsl;

public class AppTest {
	
	private static FilenameFilter ACCEPT_ALL_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return true;
		}
	};
	
	AnalysedObjectIterator<JavaSource> javaSources;
	AnalysedObjectIterator<ClassFile> classFiles;
	
	@Before
	public void setup () {
		javaSources = ResourceIterators.filesByPathResourceIterator(JavaSourceJdom.PARSER, ACCEPT_ALL_FILTER , true, new File("src/main/java/").getAbsolutePath());
		classFiles = ResourceIterators.filesByPathResourceIterator(new AsmClassFileParser(null), ACCEPT_ALL_FILTER , true, new File("target/classes/").getAbsolutePath());
	}
	
	@Test
	public void packageDeclaration() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(PackageDeclaration.class).
	        	assertThat(PackageDeclarationDsl.packageDeclaration().matches("com.github.freudExamples.*")).within(javaSources);
			
			AnalysisListener listener = new AssertionErrorAnalysisListener();
			analyser.analyse(listener);
	}
	
	@Test
	public void lessThan250Lines() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(CodeBlock.class).
        	assertThat(codeBlockLines().lessThanOrEqualTo(250)).within(javaSources);
		
		AnalysisListener listener = new AssertionErrorAnalysisListener();
		analyser.analyse(listener);
	}
	
	@Test
	public void noDateUtil() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(ImportDeclaration.class).
	        	assertThat(no(importDeclaration().matches("java.util.Date"))).within(javaSources);
			
			AnalysisListener assertionListener = new AssertionErrorAnalysisListener();
			analyser.analyse(assertionListener);
	}
	
	@Test
	public void noStringBuffer() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(ClassFileMethod.class).
				assertThat(no(hasMethodInvocation(StringBuffer.class, "<init>"))).within(classFiles);
			
			AnalysisListener listener = new AssertionErrorAnalysisListener();
			analyser.analyse(listener);
	}
	
	@Test
	public void noSystemGC() throws Exception {
		FreudAnalyser analyser = Freud.iterateOver(CodeBlock.class).
	        	assertThat(no(CodeBlockDsl.hasMethodCall("System.gc"))).within(javaSources);
			
			AnalysisListener listener = new AssertionErrorAnalysisListener();
			analyser.analyse(listener);
	}
	
}
