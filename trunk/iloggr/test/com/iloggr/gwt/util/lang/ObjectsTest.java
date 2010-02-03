package com.iloggr.gwt.util.lang;

import junit.framework.Assert;

import com.iloggr.gwt.util.UtilUITest;

public class ObjectsTest extends UtilUITest {

	public void testPreconditions() {
		try {
			Objects.isInstance(null, new Object());
			Assert.fail("A null type should not be allowed");
		} catch (NullPointerException e) {
			// expected
		}
		try {
			Objects.isInstance(Runnable.class, new Object());
			Assert.fail("Interfaces should not be allowed");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testIsInstance() {
		Assert.assertFalse(Objects.isInstance(String.class, null));
		Assert.assertTrue(Objects.isInstance(Object.class, new Object()));
		Assert.assertTrue(Objects.isInstance(Object.class, "jake"));
		Assert.assertTrue(Objects.isInstance(Object.class, new Object() { /* anonymous type */ }));
	}
}
