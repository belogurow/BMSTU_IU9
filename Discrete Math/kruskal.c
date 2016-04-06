#include <stdio.h>
#include <stdlib.h>
#include <math.h>


struct Unit {
        int x, y, dpth;
	struct Unit *nit;
} **a;

struct Unit* InitUnit(int x, int y) {
	struct Unit *l = malloc(sizeof(struct Unit));
	l->x = x;
	l->y = y;
	l->dpth = 0;
	l->nit = l;
	return l;
}

struct RoadBtwnUnits {
	int x, y;
	double lgth;
} *b;

struct RoadBtwnUnits InitRBU(int x, int y, float lgth) {
	struct RoadBtwnUnits l;
	l.x = x;
	l.y = y;
	l.lgth = lgth;
	//printf("%.2f\n", lgth);
	return l;
}

struct Unit* Find(struct Unit* a) {
	return (a->nit == a) ? a->nit : (a->nit = Find(a->nit));
}

void Union(struct Unit *a, struct Unit *b) {
	struct Unit *ma = Find(a), *mb = Find(b);
	if (ma->dpth < mb->dpth)
		ma->nit = mb;
	else {
		mb->nit = ma;
		if ((ma->nit == mb->nit) && (ma != mb))
			ma->dpth++;
	}
}

int compare_RBU(const void *a, const void *b) {
	const struct RoadBtwnUnits *ma = a;
	const struct RoadBtwnUnits *mb = b;
	if (ma->lgth == mb->lgth)
		return 0;
	return ma->lgth < mb->lgth ? -1 : 1;
}

double mst_Kruskal(struct Unit **u, struct RoadBtwnUnits *r, int n) {
	qsort(r, n, sizeof(struct RoadBtwnUnits), compare_RBU);
	int i = 0;
	double a = 0;
	for(i = 0; i < n; i++) {
		if (Find(u[r[i].x]) != Find(u[r[i].y])) {
			a += r[i].lgth;
			Union(u[r[i].x], u[r[i].y]);
		}
	} 
	return a;
}

int main(int argc, char **argv)
{
	int n, x, y, i, j;
	scanf("%d", &n);
	a = malloc(n * sizeof(struct Unit*));
	
	for(i = 0; i < n; i++) {
		scanf("%d %d", &x, &y);
		a[i] = InitUnit(x, y);
	}	
	b = malloc(((n-1)*n/2) * sizeof(struct RoadBtwnUnits));
	y = 0;
	for(i = 0; i < n; i++) 
		for(j = i + 1; j < n; j++) {
			b[y] = InitRBU(i, j, sqrt(pow(a[i]->x - a[j]->x, 2) + pow(a[i]->y - a[j]->y, 2)));
			y++;
		}
	printf("%.2f\n", mst_Kruskal(a, b, y));
	

	//for(i = 0; i < y; i++)	
	//	printf("%.2f\n", b[i].lgth);
	
	for(i = 0; i < n; i++)
		free(a[i]);
	return 0;
}