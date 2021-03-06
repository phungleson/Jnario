/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.standalone.tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.Files;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.feature.FeatureStandaloneSetup;
import org.jnario.jnario.test.util.ExtendedSuiteInjectorProvider;
import org.jnario.jnario.test.util.ModelStore;
import org.jnario.spec.SpecStandaloneSetup;
import org.jnario.suite.compiler.SuiteBatchCompiler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(ExtendedSuiteInjectorProvider.class)
@SuppressWarnings("all")
public class SuiteBatchCompilerTest {
  @Inject
  private SuiteBatchCompiler batchCompiler;
  
  @Inject
  @Extension
  private ModelStore modelStore;
  
  private static String OUTPUT_DIRECTORY = "./test-result";
  
  private static String XTEND_SRC_DIRECTORY = "./testdata";
  
  private static String TEMP_DIRECTORY = "./test-temp-dir";
  
  @Before
  public void onSetup() {
    try {
      this.batchCompiler.setSourcePath(SuiteBatchCompilerTest.XTEND_SRC_DIRECTORY);
      this.batchCompiler.setOutputPath(SuiteBatchCompilerTest.OUTPUT_DIRECTORY);
      this.batchCompiler.setDeleteTempDirectory(true);
      this.batchCompiler.setUseCurrentClassLoaderAsParent(true);
      File _file = new File(SuiteBatchCompilerTest.OUTPUT_DIRECTORY);
      _file.mkdir();
      File _file_1 = new File(SuiteBatchCompilerTest.OUTPUT_DIRECTORY);
      Files.cleanFolder(_file_1, null, true, false);
      SpecStandaloneSetup.doSetup();
      FeatureStandaloneSetup.doSetup();
      String _plus = (SuiteBatchCompilerTest.XTEND_SRC_DIRECTORY + "/test/Example.feature");
      URI _createURI = URI.createURI(_plus);
      this.modelStore.load(_createURI);
      String _plus_1 = (SuiteBatchCompilerTest.XTEND_SRC_DIRECTORY + "/test/Example.spec");
      URI _createURI_1 = URI.createURI(_plus_1);
      this.modelStore.load(_createURI_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @After
  public void onTearDown() {
    try {
      File _file = new File(SuiteBatchCompilerTest.OUTPUT_DIRECTORY);
      Files.cleanFolder(_file, null, true, true);
      File _file_1 = new File(SuiteBatchCompilerTest.TEMP_DIRECTORY);
      boolean _exists = _file_1.exists();
      if (_exists) {
        File _file_2 = new File(SuiteBatchCompilerTest.TEMP_DIRECTORY);
        Files.cleanFolder(_file_2, null, true, true);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testCompileTestData() {
    final Provider<ResourceSet> _function = new Provider<ResourceSet>() {
        public ResourceSet get() {
          XtextResourceSet _resourceSet = SuiteBatchCompilerTest.this.modelStore.getResourceSet();
          return ((ResourceSet) _resourceSet);
        }
      };
    this.batchCompiler.setResourceSetProvider(_function);
    this.batchCompiler.compile();
    String _plus = (SuiteBatchCompilerTest.OUTPUT_DIRECTORY + "/test");
    File _file = new File(_plus);
    final File outputDir = _file;
    final FilenameFilter _function_1 = new FilenameFilter() {
        public boolean accept(final File dir, final String name) {
          boolean _endsWith = name.endsWith(".java");
          return _endsWith;
        }
      };
    String[] _list = outputDir.list(_function_1);
    int _size = ((List<String>)Conversions.doWrapArray(_list)).size();
    Assert.assertEquals(1, _size);
  }
}
