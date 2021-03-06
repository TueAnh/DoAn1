#include<bits/stdc++.h>

using namespace std;

#define C(x,y) (c[x][y]-fx[x]-fy[y])

int n,m,k,nx,ny,ima=0;
int c[101][101];
int fx[101],fy[101];
int Queue[101];
int matchX[101],matchY[101];
int reachX[101],reachY[101];
int trace[101],now[101];
int M=1000001;

void inp(){
	cin>>k;
	for(int i=1;i<=k;i++)
		for(int j=1;j<=k;j++)
			cin>>c[i][j];
}

int findArgumentPath(int x){
	ima++;
	int l=1,r=1;
	nx=ny=0;
	Queue[l]=x;
	while(l<=r){
		int u = Queue[l++];
		reachX[++nx] = u;
		for(int v=1;v<=k;v++){
			if(C(u,v)==0 && now[v]!=ima){
				now[v]=ima;
				reachY[++ny]=v;
				trace[v]=u;
				if(!matchY[v])
					return v;
				Queue[++r] = matchY[v];
			}
		}
	}
	return 0;
}

void argumenting(int y){
	while(now[y]==ima){
		int x = trace[y];
		int next = matchX[x];
		matchX[x] = y;
		matchY[y] = x;
		y=next;
	}
}

void changeEge(){
	int delta=M;
	for(int i=1;i<=nx;i++){
		int u = reachX[i];
		for(int v=1;v<=k;v++)
			if(now[v]!=ima)
				delta=min(C(u,v),delta);
	}
	for(int i=1;i<=nx;i++)
		fx[reachX[i]]+=delta;
	for(int i=1;i<=ny;i++)
		fy[reachY[i]]-=delta;
}

void solve(){
	for(int i=1;i<=k;i++){
		while(1){
			int y = findArgumentPath(i);
			if(y){
				argumenting(y);
				break;
			}
			changeEge();
		}
	}
}

void out(){
	int tong=0;
	for(int u=1;u<=k;u++)
		tong+=c[u][matchX[u]];

	cout<<tong<<endl;
}

int main(){
	inp();
	solve();
	out();	
}
