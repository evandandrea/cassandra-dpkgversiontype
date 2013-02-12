package com.canonical.dpkgversiontype;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import java.nio.ByteBuffer;
import org.apache.cassandra.db.marshal.*;

public class DpkgVersionTypeTest {
	public DpkgVersionTypeTest() {
	}

	@Test
	public void testCompare() {
        ByteBuffer initial = DpkgVersionType.instance.decompose("1:2.0-0ubuntu1");
        ByteBuffer bigger = DpkgVersionType.instance.decompose("1:2.0-0ubuntu2");
        ByteBuffer smaller = DpkgVersionType.instance.decompose("1:2.0-0ubuntu1~ev1");
		Assert.assertEquals(0, DpkgVersionType.instance.compare(initial, initial));
		Assert.assertEquals(1, DpkgVersionType.instance.compare(initial, smaller));
		Assert.assertEquals(-1, DpkgVersionType.instance.compare(initial, bigger));
	}
    @Test
    public void testVersion() {
        DpkgVersion v = new DpkgVersion("1:2.0-0ubuntu1");
        Assert.assertEquals(1, v.epoch);
        Assert.assertEquals("2.0", v.version);
        Assert.assertEquals("0ubuntu1", v.revision);
        Assert.assertEquals(1, v.compare(new DpkgVersion("1:2.0-0ubuntu1~ev1")));
        Assert.assertEquals(0, v.compare(new DpkgVersion("1:2.0-0ubuntu1")));
        Assert.assertEquals(1, v.compare(new DpkgVersion("1:2.0-0ubuntu0")));
        Assert.assertEquals(-1, v.compare(new DpkgVersion("1:2.0-0ubuntu2")));
    }
}
