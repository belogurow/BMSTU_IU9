#include <stdio.h>
#include <stdlib.h>

int max(int, int);
void build(int*, int, int, int, int*);
int segmenttree_query(int*, int, int, int);
int query(int*, int, int, int, int, int);
void update(int, int, int, int, int, int*);


int main(int argc, char **argv)
{
        int i, n;
	scanf("%d", &n);
	int *a = (int*)malloc(n*sizeof(int));
	int *t = (int*)malloc(4*n*sizeof(int));
	for(i = 0; i < n; i++)
		scanf("%d", &a[i]);
	build(a, 0, n-1, 0, t);
	int m;
	char s[4];
	scanf("%d", &m);
	for(i = 0; i < m; i++) {
		int x1, x2;
		scanf("%s %d %d", s, &x1, &x2);
		if (s[0] == 'M') 
			printf("%d\n", segmenttree_query(t, n, x1, x2));
		else
			update(0, 0, n-1, x1, x2, t);
	}
	//printf("%d\n", segmenttree_query(t, n, 2, 3));
	free(a);
	free(t);
	return 0;
}
int max(int x1, int x2)
{
        return (x1 > x2) ? x1 : x2; 
} 
void build(int *mas, int a, int b, int v, int *t) 
{
	if (a == b) {
		t[v] = mas[a];
		//printf("t[%d] - %d\n", v, t[v]);
	}
	else {
		int m = (a + b) / 2;
		build(mas, a, m, 2*v+1, t);
		build(mas, m + 1, b, v*2+2, t);
		t[v] = max(t[v*2+1], t[v*2 + 2]);
		//printf("t[%d] - %d\n", v, t[v]);
	}
}
int segmenttree_query(int *t, int n, int l, int r)
{
	return query(t, l, r, 0, n-1, 0);
}
int query(int *t, int l, int r, int a, int b, int v)
{
	if ((l == a) && (r == b))
		return t[v];
	else {
		int m = (a + b)/2;
		if (r <= m)
			return query(t, l, r, a, m, 2*v+1);
		else {
			if (l > m)
				return query(t, l, r, m+1, b, 2*v+2);
			else
				return max(query(t, l, m, a, m, 2*v+1), query(t, m+1, r, m+1, b, 2*v+2));
		}
	}
}
void update(int v, int a, int b, int i, int new, int *t)
{
	if (a == b)
		t[v] = new;
	else {
		int m = (a + b) / 2;
		if (i <= m)
			update(v*2+1, a, m, i, new, t);
		else 
			update(v*2+2, m+1, b, i, new, t);
		t[v] = max(t[v*2+1], t[v*2+2]);
	}
}
 