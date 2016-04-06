#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Spisok {
        int volume;
        int n;
	int *mas;
} **a;

struct Spisok* InitSpisok(int n) {
	struct Spisok *l = calloc(n, sizeof(struct Spisok));
	l->n = n;
	l->mas = malloc(n * sizeof(int));
	return l;
}

void DFS(struct Spisok **a, int *test, int v) {
	test[v] = 1;
	int i;
	for(i = 0; i < a[v]->volume; i++) {
		if (test[a[v]->mas[i]] != 1)
			DFS(a, test, a[v]->mas[i]);
	}
}

int main(int argc, char **argv)
{
	int n, m, i;
	scanf("%d %d", &n, &m);
	a = malloc(sizeof(struct Spisok*) * n);
	int *test = (int*)malloc(n * sizeof(int));
	for(i = 0; i < n; i++) {
		test[i] = 0;
		a[i] = InitSpisok(200);
	}
	int x1, x2;
	for(i = 0; i < m; i++) {
		scanf("%d %d", &x1, &x2);
		a[x1]->mas[a[x1]->volume++] = x2;
		a[x2]->mas[a[x2]->volume++] = x1;
	}
	int compset = 0;
	for(i = 0; i < n; i++) {
		if (test[i] != 1) {
			DFS(a, test, i);
			compset++;
		}
	}
	printf("%d\n", compset);
	free(test);
	for(i = 0; i < n; i++) {
		free(a[i]->mas);
		free(a[i]);
	}
	free(a);

	return 0;
}
