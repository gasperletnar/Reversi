package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class VsiTesti {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(VsiTesti.class.getName());
		suite.addTestSuite(TestLogikaIgre.class);
		return suite;
	}
}
