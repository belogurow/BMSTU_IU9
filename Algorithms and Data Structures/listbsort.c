#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Elem {
        struct Elem *next;
        char *word;
} *head, *tail, *new;

struct Elem* InitSingleLinkedList()
{
        struct Elem *l = (struct Elem*)malloc(sizeof(struct Elem));
        l->next = NULL;
	l->word = NULL;
	return l;
} 
void Swap(struct Elem *x) {
	char *swap;
	swap = x->word;
	x->word = x->next->word;
	x->next->word = swap;
} 
int compare(struct Elem *x, struct Elem *y)
{
	return (strlen(x->word) > strlen(y->word));
}
int ListLength(struct Elem *l) 
{
	int len = 0;
	struct Elem *x;
	x = l;
	while (x != NULL) {
		len++;
		x = x->next;
	}
	return len;
}
struct Elem *bsort(struct Elem *list)
{
	struct Elem *p;
	int t, i, bound;
	t = ListLength(list->next) - 1;
	while (t > 0) {
		bound = t;
		t = 0;
		p = list->next;
		i = 0;
		while (i < bound) {
			if (compare(p, p->next) == 1)
				Swap(p);
			t = i;
			p = p->next;
			i++;
			}
	}
}
int main(int argc, char **argv)
{
	head = InitSingleLinkedList();
	tail = head;//InitSingleLinkedList();	
	char *a = (char*)malloc(sizeof(char)*100000);
	char *b;
	gets(a);
	int n;
	b = strtok(a," ");
	while (b != NULL)
	{
		new = InitSingleLinkedList();
		new->word = b;
		if (head->next == NULL) 
			head->next = new;
		else
			tail->next = new;
		tail = new;		
		n = strlen(b);		  
		b = strtok(NULL," ");
	}
	bsort(head);
	for(new = head->next; new != NULL;) {
		printf("%s ", new->word);
		tail = new;
		//free(new->word);
		new = new->next;		
		free(tail);	
	}
	printf("\n");
	free(a);
	free(head);
	return 0;
}
