#include <bits/stdc++.h>
using namespace std;
#define pb push_back
#define pf push_front
#define mp make_pair
#define F first
#define S second
#define oo 1e9+7

const int N = 1001;
int c[N][N], fx[N], fy[N], matchX[N], matchY[N], trace[N], n,m,t, start, finish;
pair<int,int> d[N];
queue<int> q;
int C (int x, int y) {
    return c[x][y] - fx[x] - fy[y];
}
void in(){
	memset(fx, 0, sizeof fx);
	memset(fy, 0, sizeof fy);
	memset(matchX, 0, sizeof matchX);
	memset(matchY, 0, sizeof matchY);
	
	cin>>n>>m;
	n=n>m?n:m;
	for(int u=1;u<=n;u++)
		for(int v=1;v<=n;v++)
			c[u][v] = oo;
	int a=1,b=1,z;
	while(a!=0 && b!=0){
		cin>>a>>b>>z;
		c[a][b] =oo-z;
	}
}
void initBFS() {
    q = queue<int>(); q.push(start);
    memset(trace, 0, sizeof trace);
   	for(int v=1;v<=n;v++) d[v] = mp(C(start, v), start);
    finish = 0;
}
void BFS() {
    do {
        int u = q.front(); q.pop();
        for(int v=1;v<=n;v++) if (trace[v] == 0) {
            int w = C(u,v);
            if (w == 0) {
                trace[v] = u;
                if (matchY[v] == 0) {
                    finish = v;
                    return;
                } else q.push(matchY[v]);
            }
            if (d[v].F > w) d[v] = mp(w, u);
        }
    } while (!q.empty());
}
void Adjust() {
    int delta = oo;
    for(int v=1;v<=n;v++) if (trace[v] == 0) delta = min(delta, d[v].F);
    fx[start] += delta;
    for(int v=1;v<=n;v++) if (trace[v]) {
        fy[v] -= delta; fx[matchY[v]] += delta;
    } else d[v].F -= delta;
    for(int v=1;v<=n;v++) if (trace[v] == 0 && d[v].F == 0) {
        trace[v] = d[v].S;
        if (matchY[v] == 0) {
            finish = v;
            return;
        } else q.push(matchY[v]);
    }
}
void Enlarge() {
    do {
        int u = trace[finish], next = matchX[u];
        matchX[u] = finish;
        matchY[finish] = u;
        finish = next;
    } while (finish);
}
void solve (){
	for(int u=1;u<=n;u++){
        start = u;
        initBFS();
        do {
            BFS();
            if (!finish) Adjust();
        } while (!finish);
        Enlarge();
	}
}
void out(){
	int w = 0 , k = 0;
	for(int u=1;u<=n;u++) if (c[u][matchX[u]] < oo)  w += (oo-c[u][matchX[u]]);
	cout<<w<<endl;
}
int main() {
	int test;cin>>test;
	while(test--){
		in();
		solve();
		out();
	}
}
