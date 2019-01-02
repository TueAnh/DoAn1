import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.InputStream;
 
/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author Niyaz Nigmatullin
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        FastScanner in = new FastScanner(inputStream);
        FastPrinter out = new FastPrinter(outputStream);
        Salesman solver = new Salesman();
        solver.solve(1, in, out);
        out.close();
    }
 
    static class Salesman {
        public void solve(int testNumber, FastScanner in, FastPrinter out) {
            int n = in.nextInt();
            int m = in.nextInt();
            int[] a = in.readIntArray(n);
            long[][] f = new long[n][n];
            for (long[] e : f) Arrays.fill(e, Long.MAX_VALUE);
            for (int i = 0; i < n; i++) {
                f[i][i] = a[i];
            }
            for (int i = 0; i < m; i++) {
                int from = in.nextInt() - 1;
                int to = in.nextInt() - 1;
                int cost = in.nextInt();
                //f[from][to] = Math.min(f[from][to], cost);
                f[from][to] = cost;
            }
            for (int k = 0; k < n; k++) {
                long[] fk = f[k];
                for (int i = 0; i < n; i++) {
                    long[] fi = f[i];
                    for (int j = 0; j < n; j++) {
                        if (fi[k] != Long.MAX_VALUE && fk[j] != Long.MAX_VALUE) {
                            fi[j] = Math.min(fi[j], fi[k] + fk[j]);
                        }
                    }
                }
            }
            final long INF = 1L << 50;
            long[][] b = new long[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        b[i][j] = f[i][i];
                    } else if (f[i][j] != Long.MAX_VALUE) {
                        b[i][j] = f[i][j];
                    } else {
                        b[i][j] = INF;
                    }
                }
            }          
            	
            //ArrayUtils.shuffle(b);
//            for (int i = 0; i < n; i++)
//            {
//            	for (int j =0; j < n; j++)
//            	{
//            		System.out.printf("%s ",b[i][j]);
//            	}
//            	System.out.println();
//            }
            int[] p = HungarianAlgorithm.getMatching(b);
            long ans = 0;
            for (int i = 0; i < n; i++) {
                ans += b[i][p[i]];
            }
            out.println(ans);
        }
 
    }
 
    static class FastScanner extends BufferedReader {
        public FastScanner(InputStream is) {
            super(new InputStreamReader(is));
        }
 
 
        public int read() {
            try {
                int ret = super.read();
//            if (isEOF && ret < 0) {
//                throw new InputMismatchException();
//            }
//            isEOF = ret == -1;
                return ret;
            } catch (IOException e) {
                throw new InputMismatchException();
            }
        }
 
        static boolean isWhiteSpace(int c) {
            return c >= 0 && c <= 32;
        }
 
        public int nextInt() {
            int c = read();
            while (isWhiteSpace(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int ret = 0;
            while (c >= 0 && !isWhiteSpace(c)) {
                if (c < '0' || c > '9') {
                    throw new NumberFormatException("digit expected " + (char) c
                            + " found");
                }
                ret = ret * 10 + c - '0';
                c = read();
            }
            return ret * sgn;
        }
 
        public String readLine() {
            try {
                return super.readLine();
            } catch (IOException e) {
                return null;
            }
        }
 
        public int[] readIntArray(int n) {
            int[] ret = new int[n];
            for (int i = 0; i < n; i++) {
                ret[i] = nextInt();
            }
            return ret;
        }
 
    }
 
    static class ArrayUtils {
        static final long seed = System.nanoTime();
        static final Random rand = new Random(seed);
 
        public static <T> void shuffle(T[] a) {
            for (int i = 0; i < a.length; i++) {
                int j = rand.nextInt(i + 1);
                T t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }
 
    }
 
    static class FastPrinter extends PrintWriter {
        public FastPrinter(OutputStream out) {
            super(out);
        }
 
        public FastPrinter(Writer out) {
            super(out);
        }
 
    }
 
    static class HungarianAlgorithm {
        static public int[] getMatching(long[][] a) {
            int n = a.length;
            int[] p1 = new int[n];
            int[] p2 = new int[n];
            long[] row = new long[n];
            long[] col = new long[n];
            long allMin = Long.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    allMin = Math.min(allMin, a[i][j]);
                }
            }
            Arrays.fill(row, allMin);
            Arrays.fill(p1, -1);
            Arrays.fill(p2, -1);
            for (int i = 0; i < n; i++) {
                if (p1[i] >= 0) {
                    continue;
                }
                boolean[] was = new boolean[n];
                int[] from = new int[n];
                long[] min = new long[n];
                Arrays.fill(min, Long.MAX_VALUE);
                int cur = i;
                int curPair = -1;
                do {
                    if (curPair >= 0) {
                        was[curPair] = true;
                        cur = p2[curPair];
                    }
                    long d = Long.MAX_VALUE;
                    int minPair = -1;
                    long[] aCur = a[cur];
                    for (int j = 0; j < n; j++) {
                        if (was[j]) {
                            continue;
                        }
                        long val = aCur[j] - row[cur] - col[j];
                        if (val < min[j]) {
                            min[j] = val;
                            from[j] = curPair;
                        }
                        if (min[j] < d) {
                            d = min[j];
                            minPair = j;
                        }
                    }
                    row[i] += d;
                    for (int j = 0; j < n; j++) {
                        if (was[j]) {
                            col[j] -= d;
                            row[p2[j]] += d;
                        } else {
                            min[j] -= d;
                        }
                    }
                    curPair = minPair;
                } while (p2[curPair] >= 0);
                while (from[curPair] >= 0) {
                    int prev = from[curPair];
                    p2[curPair] = p2[prev];
                    p1[p2[prev]] = curPair;
                    curPair = prev;
                }
                p2[curPair] = i;
                p1[i] = curPair;
            }
            return p1;
        }
 
    }
    
    
}