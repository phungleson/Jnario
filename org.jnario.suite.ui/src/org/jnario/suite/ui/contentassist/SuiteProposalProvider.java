/*******************************************************************************
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
/*
* generated by Xtext
*/
package org.jnario.suite.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtend.core.xtend.XtendPackage;
import org.eclipse.xtend.ide.contentassist.ImportingTypesProposalProvider.FQNImporter;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.common.types.xtext.ui.TypeMatchFilters;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal.IReplacementTextApplier;
import org.eclipse.xtext.xbase.XbaseQualifiedNameConverter;
import org.eclipse.xtext.xbase.conversion.XbaseQualifiedNameValueConverter;
import org.jnario.suite.suite.SuitePackage;
/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
@SuppressWarnings("restriction")
public class SuiteProposalProvider extends AbstractSuiteProposalProvider {

	@Override
	public void completeImport_ImportedType(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeJavaTypes(context, XtendPackage.Literals.XTEND_IMPORT__IMPORTED_TYPE, true,
				getQualifiedNameValueConverter(), new TypeMatchFilters.All(IJavaSearchConstants.TYPE), acceptor);
	}
	
	@Override
	public void completeSpecReference_Spec(EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		
		IScope scope = getScopeProvider().getScope(model, SuitePackage.Literals.SPEC_REFERENCE__SPEC);
		XbaseQualifiedNameValueConverter qualifiedNameValueConverter = new XbaseQualifiedNameValueConverter(){
			@Override
			public String toString(String value) {
				int end = value.lastIndexOf('.');
				String result = value.substring(1, end) + ".*";
				return result;
			}
		};
		IQualifiedNameConverter qualifiedNameConverter = new XbaseQualifiedNameConverter(){
			@Override
			public QualifiedName toQualifiedName(String qualifiedNameAsString) {
				int lastIndexOf = qualifiedNameAsString.lastIndexOf(".");
				if(lastIndexOf == -1){
					return super.toQualifiedName(qualifiedNameAsString);
				}
				String[] segments = qualifiedNameAsString.split("\\.");
				if(segments.length <= 1){
					return super.toQualifiedName(qualifiedNameAsString);
				}
				segments[0] = segments[0].substring(1);
				segments[segments.length-1] = "\"" + segments[segments.length-1]; 
				return QualifiedName.create(segments);
			}
		};
		final FQNImporter fqnImporter = new FQNImporter(context.getResource(), context.getViewer(), scope, qualifiedNameConverter, null, qualifiedNameValueConverter);
		final ICompletionProposalAcceptor scopeAware = new ICompletionProposalAcceptor.Delegate(acceptor) {
			@Override
			public void accept(ICompletionProposal proposal) {
				if (proposal instanceof ConfigurableCompletionProposal) {
					((ConfigurableCompletionProposal) proposal).setTextApplier(fqnImporter);
				}
				super.accept(proposal);
			}
		};
		super.completeSpecReference_Spec(model, assignment, context, scopeAware);
	}
	
}
