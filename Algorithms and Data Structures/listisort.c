#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Elem {
        struct Elem *prev, *next;
        int v;
};
struct Elem* InitDoubleLinkedList()
{
        struct Elem *l = (struct Elem*)malloc(sizeof(struct Elem));
	l->next = l;
	l->prev = l;
	return l;
}
void InsertAfter(struct Elem *x, struct Elem *y)
{
	struct Elem *z;
	z = x->next;
	x->next = y;
	y->prev = x;
	y->next = z;
	z->prev = y;
}
void Delete(struct Elem *x)
{
	struct Elem *y;
	y = x->prev;
	struct Elem *z;
	z = x->next;
	y->next = z;
	z->prev = y;
	x->prev = NULL;
	x->next = NULL;
}
/*void ListSearch(struct Elem *spis, struct Elem *spis1, struct Elem *spis2)
{
	while ((spis2 != spis) && (spis1->v < spis2->v))
		spis2 = spis2->prev;
} */
void InsertSort(struct Elem *spis)
{
	struct Elem *spis1, *spis2;
	spis1 = (spis->next)->next;
	//printf("%d\n", spis1->v);
	while (spis1 != spis) {
		spis2 = spis1->prev;
		//ListSearch(spis, spis1, spis2);
		while ((spis2 != spis) && (spis1->v < spis2->v))
			spis2 = spis2->prev;
		//printf("%d - %d\n", spis2->v, spis1->v);
		Delete(spis1);
		InsertAfter(spis2, spis1);
		spis1 = spis1->next;
	}
}
int main(int argc, char **argv)
{
        int i, n;
	scanf("%d", &n);
	struct Elem *nil, *spis;
	nil = InitDoubleLinkedList();
	for(i = 0; i < n; i++) {
		spis = InitDoubleLinkedList();
		scanf("%d", &(spis->v));
		InsertAfter(nil, spis);
	}
	//printf("%d\n", (spis->next)->v);
	InsertSort(nil);
	spis = nil->next;
	struct Elem *help;
	for(i = 0; i < n; i++, spis = spis->next) {
		printf("%d ", spis->v);
		//printf("free %d\n ", (spis->prev)->v);
		if (i)
			free(spis->prev);
	}
        //printf("%d ", (spis->prev)->v);
	printf("\n");
        free(spis->prev);
	free(spis);
	return 0;
}
