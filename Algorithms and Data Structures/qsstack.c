#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct Task {
        int low, high;
};
struct Stack 
{
	int cap;
	int top;
	struct Task *task;
};
struct Stack* InitStack()
{
	struct Stack *s = (struct Stack*)malloc(sizeof(struct Stack));
	s->cap = 100;
	s->top = 0;
	s->task = (struct Task*)malloc(s->cap*sizeof(struct Task));
	return s;
}
void Push(struct Stack *s, int low, int high)
{
	s->task[s->top].low = low;
	s->task[s->top].high = high;
	s->top++;
}
void Pop(struct Stack *s, struct Task *ts)
{
	s->top--;
	ts->low = s->task[s->top].low;
	ts->high = s->task[s->top].high;
}
void Swap(int x, int y, int *a)
{
	int swap;
	swap = a[x];
	a[x] = a[y];
	a[y] = swap;
}
void QuickSort(int low, int high, int *p)
{
	struct Stack *s;
	s = InitStack();
	struct Task t, *ts=&t;	
	ts->low = low;
	ts->high = high;
	Push(s, low, high);
	int mid, midp, i, j; 
	while (s->top > 0) {
		Pop(s, ts);
		//printf("\npop - %d %d\n", ts->low, ts->high);
		while (ts->low < ts->high) {
			mid = (ts->low + ts->high) / 2;
			//printf("mid %d\n", mid);
			midp = mid[p];
			i = ts->low;
			j = ts->high;
			while (i <= j) {
				while(p[i] < midp)
					i++;
				while(p[j] > midp)
					j--;
				//printf("do ij - %d %d\n", i, j);
				if (i <= j) {
					//printf("1 a[%d] - %d a[%d]- %d\n", i, p[i], j, p[j]);					
					Swap(i, j, p);
					//printf("2 a[%d] - %d a[%d]- %d\n", i, p[i], j, p[j]);					
					i++;
					j--;
				}
			} 
			//int q;	for(q = 0; q < high+1; q++)
			//	printf("%d ", p[q]); 
			//printf("\n");
			//printf("ij - %d %d\n", i, j);
			//printf("1 low high %d %d\n", ts->low, ts->high);
			if (i < mid) {
				if (i < ts->high) 
					Push(s, i, ts->high);
				ts->high = j;
			}
			else {
				if (j > ts->low) 
					Push(s, ts->low, j);
				ts->low = i;
			}
			//printf("2 low high %d %d\n", ts->low, ts->high);
		} 
	} 
	free(s->task);
	free(s);
}
int main(int argc, char **argv)
{
 	int i, n;
	scanf("%d", &n);
	int a[n];
	for(i = 0; i < n; i++) 
		scanf("%d", &a[i]);
	QuickSort(0, n-1, a);
	for(i = 0; i < n; i++)
		printf("%d ", a[i]);      
	return 0;
}
