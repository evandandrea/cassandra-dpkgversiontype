package com.canonical.dpkgversiontype;
import org.apache.cassandra.db.marshal.*;

public class DpkgVersion
{
    long epoch;
    String version;
    String revision;

    DpkgVersion(String s)
    {
        /* Trim leading and trailing space. */
        s = s.trim();
        int end = s.indexOf(':');
        if (end == s.length()-1) {
            throw new MarshalException ("Nothing after colon in version number.");
        }

        if (end > 0) {
            try
            {
                this.epoch = Long.parseLong(s.substring(0, end));
            }
            catch (NumberFormatException e) {
                throw new MarshalException("Epoch in version is not a number.");
            }
            if (this.epoch < 0)
            {
                throw new MarshalException("Epoch in version is negative.");
            }
            if (this.epoch > Integer.MAX_VALUE)
            {
                throw new MarshalException("Epoch in version is too big.");
            }
            this.version = s.substring(end+1, s.length());
        } else {
            this.epoch = 0;
            this.version = s;
        }

        int hyphen = this.version.indexOf('-');
        if (hyphen > 0) {
            this.revision = this.version.substring(hyphen+1, this.version.length());
            this.version = this.version.substring(0, hyphen);
        }
    }
    public int compare(DpkgVersion rhs)
    {
        int r;

        if (this.epoch > rhs.epoch) {
            return 1;
        }
        if (this.epoch < rhs.epoch) {
            return -1;
        }

        r = verrevcmp(this.version, rhs.version);
        if (r != 0)
            return r;
        return verrevcmp(this.revision, rhs.revision);
    }

    private static int verrevcmp(String lhs, String rhs)
    {
        int i = 0;
        int j = 0;
        int first_diff = 0;

        while (i < lhs.length() || j < rhs.length())
        {
            while ((i < lhs.length() && !cisdigit(lhs.charAt(i))) ||
                   (j < rhs.length() && !cisdigit(rhs.charAt(j))))
            {
                int ac = 0;
                int bc = 0;
                if (i < lhs.length()) {
                    ac = order(lhs.charAt(i));
                }
                if (j < rhs.length()) {
                    bc = order(rhs.charAt(j));
                }
                if (ac != bc) {
                    return ac - bc;
                }
                i++;
                j++;
            }
            while (i < lhs.length() && lhs.charAt(i) == '0') {
                i++;
            }
            while (j < rhs.length() && rhs.charAt(j) == '0') {
                j++;
            }
            while ((i < lhs.length() && cisdigit(lhs.charAt(i))) &&
                   (j < rhs.length() && cisdigit(rhs.charAt(j))))
            {
                if (first_diff == 0) {
                    first_diff = lhs.charAt(i) - rhs.charAt(j);
                }
                i++;
                j++;
            }

            if (i < lhs.length() && cisdigit(lhs.charAt(i))) {
                return 1;
            }
            if (j < rhs.length() && cisdigit(rhs.charAt(j))) {
                return -1;
            }
            if (first_diff != 0) {
                return first_diff;
            }
        }
        return 0;
    }

    private static boolean cisdigit(int c)
    {
        return (c >= '0' && c <= '9');
    }
    private static boolean cisalpha(int c)
    {
        return ((c >= 'a' &&  c <= 'z') || (c >= 'A' && c <= 'Z'));
    }
    private static int order(int c)
    {
        if (cisdigit(c))
        {
            return 0;
        }
        if (cisalpha(c))
        {
            return c;
        }
        if (c == '~')
        {
            System.out.println("hi" + c);
            return -1;
        }
        if (c != 0)
        {
            return c + 256;
        } else {
            return 0;
        }
    }
}
