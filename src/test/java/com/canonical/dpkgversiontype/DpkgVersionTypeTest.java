package com.canonical.dpkgversiontype;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.nio.ByteBuffer;
import org.apache.cassandra.db.marshal.*;

public class DpkgVersionTypeTest {
	public DpkgVersionTypeTest() {
	}

	@Test
	public void testBasic() {
		System.out.println(
		DpkgVersionType.instance.compare(
				DpkgVersionType.instance.decompose("hi2"),
				DpkgVersionType.instance.decompose("hi"))
		);
	}
}
