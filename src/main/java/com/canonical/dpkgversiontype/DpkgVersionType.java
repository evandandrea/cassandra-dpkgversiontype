/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canonical.dpkgversiontype;
import org.apache.cassandra.db.marshal.*;

import java.nio.ByteBuffer;

import org.apache.cassandra.cql.jdbc.JdbcAscii;

public class DpkgVersionType extends AbstractType<String>
{
    public static final DpkgVersionType instance = new DpkgVersionType();

    DpkgVersionType() {} // singleton

    public String getString(ByteBuffer bytes)
    {
        try
        {
            return JdbcAscii.instance.getString(bytes);
        }
        catch (org.apache.cassandra.cql.jdbc.MarshalException e)
        {
            throw new MarshalException(e.getMessage());
        }
    }

    public int compare(ByteBuffer o1, ByteBuffer o2)
    {
		if (o1.remaining() == 0) {
			System.out.println("Nothing left in o1.");
			return o2.remaining() == 0 ? 0 : -1;
		}
		if (o2.remaining() == 0) {
			System.out.println("Nothing left in o2.");
			return 1;
		}
		System.out.println(o1.position());
		System.out.println(o2.position());
		int end1 = o1.position() + o1.remaining();
		int end2 = o2.position() + o2.remaining();
		for (int i = o1.position(), j = o2.position();
			 i < end1 && j < end2; i++, j++) {
			int a = (o1.get(i) & 0xff);
			int b = (o2.get(j) & 0xff);
			System.out.println("A: " + a + " B: " + b);
			if (a != b)
				return a - b;
		}
		System.out.println("fell through");
        return o1.remaining() - o2.remaining();
    }

    public String compose(ByteBuffer bytes)
    {
        return JdbcAscii.instance.getString(bytes);
    }

    public ByteBuffer decompose(String value)
    {
        return JdbcAscii.instance.decompose(value);
    }

    public ByteBuffer fromString(String source)
    {
        return decompose(source);
    }

    public void validate(ByteBuffer bytes) throws MarshalException
    {
        // 0-127
        for (int i = bytes.position(); i < bytes.limit(); i++)
        {
            byte b = bytes.get(i);
            if (b < 0 || b > 127)
                throw new MarshalException("Invalid byte for ascii: " + Byte.toString(b));
        }
    }
}
