package de.bmw.carit.jnario.spec.validation;
 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xtend2.validation.Xtend2JavaValidator;

public class AbstractSpecJavaValidator extends Xtend2JavaValidator {

	@Override
	protected List<EPackage> getEPackages() {
	    List<EPackage> result = new ArrayList<EPackage>();
	    result.add(de.bmw.carit.jnario.spec.spec.SpecPackage.eINSTANCE);
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/xtext/Xtend2"));
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/xtext/xbase/Xbase"));
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/Xtext/Xbase/XAnnotations"));
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/xtext/common/JavaVMTypes"));
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/xtext/xbase/Xtype"));
		return result;
	}

}
