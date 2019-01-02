#include<bits/stdc++.h>

using namespace std;

#define C(x,y) (c[x][y]-fx[x]-fy[y])

int n,ima,numx,numy;
int c[205][205];
int fx[205],fy[205];
int Queue[205];
int now[205],matchX[205],matchY[205];
int reachX[205],reachY[205],trace[205];
int M=100001;


void in(){
	ima=0;
	cin>>n;
	for(int i=1;i<=n;i++){
		for(int j=1;j<=n;j++)
			c[i][j]=M;
	}
	int u,v;
	while(cin>>u>>v)
		cin>>c[u][v];
	fill_n(matchX,n+1,0);
	fill_n(matchY,n+1,0);
	fill_n(fx,n+1,0);
	fill_n(fy,n+1,0);
	fill_n(now,n+1,0);
	fill_n(trace,n+1,0);
}

int findargument(int x){
	ima++;
	int l=1,r=1;
	numx=numy=0;	
	Queue[1]=x;
	while(l<=r){
		int u = Queue[l++];
		reachX[++numx] = u;
		for(int v=1;v<=n;v++){
			if( C(u,v)==0 && now[v]!=ima ){
				now[v]=ima;
				reachY[++numy]=v;
				trace[v]=u;
				if(!matchY[v]) 	
					return v;
				Queue[++r]=matchY[v];	
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

void changeEdge(){
	int delta=M;
	for(int i=1;i<=numx;i++){
		int u=reachX[i];
		for(int v=1;v<=n;v++){
			if(now[v] != ima)
				delta=min(delta,C(u,v));
		}
	}
	for(int i=1;i<=numx;i++) 
		fx[reachX[i]]+=delta;
	for(int i=1;i<=numy;i++) 
		fy[reachY[i]]-=delta;
}


void solve(){
	for(int x=1;x<=n;x++){
		while(true){
			int y=findargument(x);
			if(y){
				argumenting(y);
				break;
			}
			changeEdge();
		}
	}
}

void print(){
	int tong=0;
	for(int x=1;x<=n;x++)
		tong+=c[x][matchX[x]];
	cout<<tong<<endl;
	for(int x=1;x<=n;x++) 
		cout<<x<<" "<<matchX[x]<<endl;
}

int main(){
	in();
	solve();
	print();
}


