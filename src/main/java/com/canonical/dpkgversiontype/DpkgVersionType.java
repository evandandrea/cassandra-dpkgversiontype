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

import com.google.common.base.Charsets;

import java.nio.ByteBuffer;

import org.apache.cassandra.cql.jdbc.JdbcAscii;
import org.apache.cassandra.utils.ByteBufferUtil;

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
			return o2.remaining() == 0 ? 0 : -1;
		}
		if (o2.remaining() == 0) {
			return 1;
		}
        DpkgVersion a = new DpkgVersion(getString(o1));
        DpkgVersion b = new DpkgVersion(getString(o2));
        return a.compare(b);
    }

    public String compose(ByteBuffer bytes)
    {
        return JdbcAscii.instance.getString(bytes);
    }

    public ByteBuffer decompose(String value)
    {
        return ByteBufferUtil.bytes(value, Charsets.US_ASCII);
    }

    public ByteBuffer fromString(String source)
    {
        return decompose(source);
    }

    public void validate(ByteBuffer bytes) throws MarshalException
    {
        new DpkgVersion(getString(bytes));
    }
}
