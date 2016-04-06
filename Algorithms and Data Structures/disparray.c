#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Elem {
        struct Elem *next;
        long v;
        long number;
};

void InitHashTable(long m, struct Elem **hashtable)
{
	long i;
	for(i = 0; i < m; i++) {
		hashtable[i] = (struct Elem*)malloc(sizeof(struct Elem));
		hashtable[i]->next = NULL;
		hashtable[i]->number = -1;
	}
}
void Insert(long i, long v, long m, struct Elem **hashtable)
{
	long n = i % m;
	struct Elem *p, *l;
	p = hashtable[n];
	//printf("%ld\n", n);
	while (p->next != NULL) {
			if (p->number == i) {
			//printf("voshel %ld\n", v);
			p->v = v;
			return;
		}
		p = p->next;
	} 
	if (p->number == i) {
			//printf("voshel %ld\n", v);
			p->v = v;
			return;
	}
	p->next = (struct Elem*)malloc(sizeof(struct Elem));
	p->next->next = NULL;
	p->next->v = v;
	p->next->number = i;
}
void At(long i, long m, struct Elem **hashtable)
{
	long n = i % m;
	struct Elem *p;
	p = hashtable[n];
	while (p->next != NULL) {
		if (p->number == i) {
			printf("%ld\n", p->v);
			return;
		}
		else
			p = p->next;
	}
	if (p->number == i) {
			printf("%ld\n", p->v);
			return;
	}
	printf("%d\n", 0);
}
void Delete(long m, struct Elem **hashtable)
{
        long i;
	struct Elem *p, *q;
	for (i = 0; i < m; i++) {
		p = hashtable[i];
		while (p->next != NULL) {
			q = p->next;
			free(p);
                        p = q;
		}
		free(p);
	}
}	
int main(int argc, char **argv)
{
	long i, n, m;
	scanf("%ld %ld", &n, &m);
	struct Elem *hashtable[m];
	InitHashTable(m, hashtable);
	char a[10];
	long q, v;
	for(i = 0; i < n; i++) {
		scanf("%s", a);
		if (a[1] == 'T') {
			scanf("%ld", &q);
			At(q, m, hashtable);
		}
		else {
			scanf("%ld %ld", &q, &v);
			Insert(q, v, m, hashtable);
		}
	}
	Delete(m, hashtable);
	return 0;
}
