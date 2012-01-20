package de.bmw.carit.jnario.tests.util;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.junit.Assert;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;

import de.bmw.carit.jnario.common.test.util.IUriProvider;

/**
 * @author Sebastian Benz
 */
public class ModelStore implements Iterable<EObject> {

	
	private XtextResourceSet resourceSet = new XtextResourceSet();

	public ModelStore() {
		resourceSet.setClasspathURIContext(getClass());
	}
	
	public List<Resource> load(IUriProvider uriProvider) {
		for (URI uri : uriProvider.allUris()) {
			resourceSet.getResource(uri, true);
		}
		Resources.addContainerStateAdapter(resourceSet);
		EcoreUtil.resolveAll(resourceSet);
		return resourceSet.getResources();
	}

	public void add(Resource... resources) {
		resourceSet.getResources().addAll(asList(resources));
	}

	public Resource parse(String name, String... lines) {
		Resource resource = createResource(name);
		String inputString = Joiner.on("\n").join(lines);
		StringInputStream inputStream = new StringInputStream(inputString);
		try {
			resource.load(inputStream, emptyMap());
		} catch (IOException ex) {
			ex.printStackTrace();
			Assert.fail(ex.getMessage());
		}
		return resource;
	}

	private Resource createResource(String name) {
		URI uri = URI.createURI(name);
		Resource resource = resourceSet.getResource(uri, false);
		if (resource != null) {
			resource.unload();
		} else {
			resource = resourceSet.createResource(uri);
		}
		return resource;
	}

	public Iterator<EObject> iterator() {
		return Iterators.filter(resourceSet.getAllContents(), EObject.class);
	}

	public static ModelStore create() {
		return new ModelStore();
	}

	public Resource parseScenario(String...strings) {
		return parse("Scenario" + resourceSet.getResources().size() + ".jnario", strings);
	}
	
	public Resource parseSpec(String...strings) {
		return parse("Spec" + resourceSet.getResources().size() + ".spec", strings);
	}
	
	public Resource parseScenario(CharSequence input) {
		return parse("Scenario" + resourceSet.getResources().size() + ".jnario", input.toString());
	}
	
	public Resource parseSpec(CharSequence input) {
		return parse("Spec" + resourceSet.getResources().size() + ".spec", input.toString());
	}

}