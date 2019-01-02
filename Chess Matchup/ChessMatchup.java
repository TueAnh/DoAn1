import java.io.*;
import java.util.*;
public class ChessMatchup{
	static int gcd(int u, int v) {
		return (v != 0) ? gcd(v, u % v) : u;
	}
 
	int INF = 10000000;
	int cost[][];
	int n, max_match;
	int lx[], ly[];
	int xy[];
	int yx[];
	boolean S[], T[];
	int slack[];
	int slackx[];
	int prv[];
 
	void init_labels() {
 
		lx = new int[n];
		ly = new int[n];
		xy = new int[n];
		yx = new int[n];
		S = new boolean[n];
		T = new boolean[n];
		slack = new int[n];
		slackx = new int[n];
		prv = new int[n];
		Arrays.fill(lx, 0);
		Arrays.fill(ly, 0);
		for (int x = 0; x < n; x++)
			for (int y = 0; y < n; y++)
				lx[x] = Math.max(lx[x], cost[x][y]);
	}
 
	void update_labels() {
		int x, y, delta = INF;
		for (y = 0; y < n; y++) 
			if (!T[y])
				delta = Math.min(delta, slack[y]);
		for (x = 0; x < n; x++) 
			if (S[x])
				lx[x] -= delta;
		for (y = 0; y < n; y++) 
			if (T[y])
				ly[y] += delta;
		for (y = 0; y < n; y++) 
			if (!T[y])
				slack[y] -= delta;
	}
 
	void add_to_tree(int x, int prevx)
	{
		S[x] = true; 
		prv[x] = prevx; 
		for (int y = 0; y < n; y++) 
			if (lx[x] + ly[y] - cost[x][y] < slack[y]) {
				slack[y] = lx[x] + ly[y] - cost[x][y];
				slackx[y] = x;
			}
	}
 
	void augment() {
		if (max_match == n)
			return;
		int x, y, root = 0;
		int q[], wr = 0, rd = 0;
 
		q = new int[n];
		Arrays.fill(S, false);
		Arrays.fill(T, false);
		Arrays.fill(prv, -1);
		for (x = 0; x < n; x++)
			if (xy[x] == -1) {
				q[wr++] = root = x;
				prv[x] = -2;
				S[x] = true;
				break;
			}
		for (y = 0; y < n; y++) {
			slack[y] = lx[root] + ly[y] - cost[root][y];
			slackx[y] = root;
		}
 
		while (true) {
			while (rd < wr) {
				x = q[rd++];
				for (y = 0; y < n; y++)
					if (cost[x][y] == lx[x] + ly[y] && !T[y]) {
						if (yx[y] == -1)
							break;
						T[y] = true;
						q[wr++] = yx[y];
						add_to_tree(yx[y], x);
					}
				if (y < n)
					break;
			}
			if (y < n)
				break;
			update_labels();
			wr = rd = 0;
			for (y = 0; y < n; y++)
 
				if (!T[y] && slack[y] == 0) {
					if (yx[y] == -1) {
						x = slackx[y];
						break;
					} else {
						T[y] = true;
						if (!S[yx[y]]) {
							q[wr++] = yx[y];
							add_to_tree(yx[y], slackx[y]);
						}
					}
				}
			if (y < n)
				break;
		}
		if (y < n) {
			max_match++;
			for (int cx = x, cy = y, ty; cx != -2; cx = prv[cx], cy = ty) {
				ty = xy[cx];
				yx[cy] = cx;
				xy[cx] = cy;
			}
			augment();
		}
	}
 
	public int hungarian() {
		int ret = 0;
		max_match = 0;
		init_labels();
		Arrays.fill(xy, -1);
		Arrays.fill(yx, -1);
		augment();
		for (int x = 0; x < n; x++)
			ret += cost[x][xy[x]];
		return ret;
	}
	public  int maximumScore(int[] us, int[] them)
	{
		cost = new int[us.length][them.length];
		for (int i = 0; i < us.length; i++)
			for (int j = 0; j < them.length; j++)
			{
				if (us[i] > them[j]) 
					cost[i][j] = 2;
				else if (us[i] == them[j])
					cost[i][j] = 1;
				else cost[i][j] = 0;
			}
		return hungarian();
	}
	public static void main(String args[]){
		
 System.out.println();
	}
}
