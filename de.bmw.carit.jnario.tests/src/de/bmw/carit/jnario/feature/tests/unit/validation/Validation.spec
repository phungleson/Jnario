/*******************************************************************************
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package de.bmw.carit.jnario.feature.tests.unit.validation

import com.google.inject.Inject
import de.bmw.carit.jnario.feature.feature.GivenReference
import de.bmw.carit.jnario.runner.InstantiateWith
import de.bmw.carit.jnario.common.test.util.ModelStore
import de.bmw.carit.jnario.common.test.util.SpecTestInstantiator
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.junit4.validation.RegisteredValidatorTester

import static de.bmw.carit.jnario.common.test.util.Query.*

/**
 * @author Birgit Engelmann - Initial contribution and API
 */
@InstantiateWith(typeof(SpecTestInstantiator))
describe "JnarioValidator"{

	@Inject ModelStore modelStore
	
	it "returns an error if a step definition and a reference to this step are in the same scenario"{
		modelStore.parseScenario('
			package bootstrap

			Feature: Validation
				Scenario: step definition and usage
					Given some step
						var x = 3
						x = 5
					Given some step
		')
		val validationResult = validate(typeof(GivenReference))
		validationResult.assertErrorContains("Cannot reference a step");
	}
	
	it "does not return an error if a step definition was in a different scenario than the reference"{
		modelStore.parseScenario('
			package bootstrap

			Feature: Validation
				Scenario: step definition and usage
					Given some step
						var x = 3
						x = 5
				Scenario: other scenario
					Given some step
		')
		val validationResult = validate(typeof(GivenReference))
		validationResult.assertOK();
	} 
	
//	it "returns an error if some variable was not defined"{
//		modelStore.parseScenario('
//			package bootstrap
//
//			Feature: Validation
//				Scenario: 
//					Given I forget to declare a variable
//						x = 3
//			
//		')
//		
//		val validationResult = validate(typeof(Given))
//		validationResult.assertErrorContains("invalid type")
//	}
//	
//	"ignores not found references"{
//	
//	}
	
	def validate(Class<? extends EObject> type){
		val target = query(modelStore).first(type)
		return RegisteredValidatorTester::validateObj(target)
	}
}