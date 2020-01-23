package com.CK;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}

class Solution {
    public List<String> ipToCIDR(String ip, int n) {
        long x = 0;
        String[] parts = ip.split("\\."); // we need \\ here because '.' is a keyword in regex.
        for(int i = 0; i < 4; i++) {
            x = x * 256 + Long.parseLong(parts[i]);
        }

        List<String> res = new ArrayList();
        while(n > 0) {
            long count = x & -x;
            // this count value here means if we don't change the current start ip, how many
            // more ips we can represent with CIDR

            while(count > n) { // for example count is 7 but we only want to 2 more ips
                count /= 2;
            }

            res.add(oneCIDR(x, count));
            n = n - (int)count;
            x = x + (int)count;
        }
        return res;
    }

    private String oneCIDR(long x, long count) {
        int d, c, b, a;
        d = (int) x & 255; // Compute right-most part of ip
        x >>= 8; // throw away the right-most part of ip
        c = (int) x & 255;
        x >>= 8;
        b = (int) x & 255;
        x >>= 8;
        a = (int) x & 255;

        int len = 0;
        // this while loop to know how many digits of count is in binary
        // for example, 00001000 here the len will be 4.
        while(count > 0) {
            count /= 2;
            len++;
        }
        int mask = 32 - (len - 1);
        // Think about 255.0.0.7 -> 11111111 00000000 00000000 00000111
        // x & -x of it is 00000001, the mask is 32. (which is 32 - (1 - 1))

        return new StringBuilder()
                .append(a)
                .append(".")
                .append(b)
                .append(".")
                .append(c)
                .append(".")
                .append(d)
                .append("/")
                .append(mask)
                .toString();
    }
}

class Solution {
    public List<String> ipToCIDR(String ip, int n) {
        long start = ipToLong(ip);
        List<String> ans = new ArrayList();
        while (n > 0) {
            int mask = Math.max(33 - bitLength(Long.lowestOneBit(start)),
                    33 - bitLength(n));
            ans.add(longToIP(start) + "/" + mask);
            start += 1 << (32 - mask);
            n -= 1 << (32 - mask);
        }
        return ans;
    }
    private long ipToLong(String ip) {
        long ans = 0;
        for (String x: ip.split("\\.")) {
            ans = 256 * ans + Integer.valueOf(x);
        }
        return ans;
    }
    private String longToIP(long x) {
        return String.format("%s.%s.%s.%s",
                x >> 24, (x >> 16) % 256, (x >> 8) % 256, x % 256);
    }
    private int bitLength(long x) {
        if (x == 0) return 1;
        int ans = 0;
        while (x > 0) {
            x >>= 1;
            ans++;
        }
        return ans;
    }
}