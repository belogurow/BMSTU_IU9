#include <stdio.h>
#include <string.h>
#include <stdlib.h>
int nod(int a, int b)
{
        int c;
	a = abs(a);
	b = abs(b);
	while (b) {
		c = a % b;
		a = b;
		b = c;
	}
	return a;
}
void computelog(int m, int *log)
{
	int i, j;
	for (j = 0, i = 1; j < m;)
		if (j < (1 << i)) {
			log[j] = i - 1;
			j++;
		}	
		else
			i++;
		
}
void sparsetable_build(int *v, int n, int m, int **st)
{
	int i = 0, j = 1;
	while (i < n) {
		st[i][0] = v[i];
		i++;
	}
	while (j < m) {
		i = 0;
		while (i <= n - (1 << j)) {
			st[i][j] = nod(st[i][j-1], st[i + (1 << (j-1))][j-1]);
			i++;
		}
	j++;
	}
}
void sparsetable_query(int n, int m, int **st, int l, int r, int *log)
{
	int j = log[r-l+1];
	printf("%d\n", nod(st[l][j], st[r-(1 << j) + 1][j]));
}
int main(int argc, char **argv)
{
	int n, m, i, j;
	scanf("%d", &n);
	int v[n];
	for(i = 0; i < n; i++)
		scanf("%d", &v[i]);
	int log[n + 1];
	computelog(n + 1, log);
	m = log[n] + 1;
	int **st;
	st = (int**)malloc(n*sizeof(int*));
	for(i = 0; i < n; i++)
		st[i] = (int*)malloc(m*sizeof(int));
	sparsetable_build(v, n, m, st);
	int q, x1, x2;
	scanf("%d", &q);
	for(i = 0; i < q; i++) {
		scanf("%d %d", &x1, &x2);
		sparsetable_query(n, m, st, x1, x2, log);
	}
	for(i = 0; i < n; i++)
		free(st[i]);
	free(st); 
	return 0;
}
