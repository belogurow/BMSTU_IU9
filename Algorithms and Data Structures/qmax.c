#include <stdio.h>
#include <string.h>
#include <stdlib.h>
struct q {
        long count;
	long tail1;
	long tail2;
	long cap;
	long *stack1;
	long *stack2;
	long *max1;
	long *max2;
};
long max(long x1, long x2)
{
        return (x1 > x2) ? x1 : x2; 
} 
void Max(struct q *q)
{
	if ((q->tail1 != 0) && (q->tail2 != q->cap)) {
		printf("%ld\n", max(q->max1[q->tail1-1], q->max2[q->tail2+1]));
		return;
	}
	if (q->tail1 != 0) {
		printf("%ld\n", q->max1[q->tail1-1]);
		return;
	}
	printf("%ld\n", q->max2[q->tail2+1]);
}
void queueempty(struct q *q)
{
	if (q->count == 0) 
		printf("true\n");
	else
		printf("false\n");
} 
void enqueue(struct q *q, long x)
{
	q->stack1[q->tail1] = x;
	if (q->tail1 == 0)
		q->max1[q->tail1] = x;
	else
		q->max1[q->tail1] = max(x, q->max1[q->tail1-1]);
	q->tail1++;
	q->count++;
}
void dequeue(struct q *q)
{
	if (q->tail2 == q->cap)
		while (q->tail1 > 0) {
			q->tail1--;
			q->stack2[q->tail2] = q->stack1[q->tail1];
			if (q->tail2 == q->cap)
				q->max2[q->tail2] = q->stack2[q->tail2];
			else
				q->max2[q->tail2] = max(q->max2[q->tail2+1], q->stack2[q->tail2]);
			q->tail2--;
		}
	q->tail2++;
	printf("%ld\n", q->stack2[q->tail2]);
	q->count--;
}
int main(int argc, char **argv)
{
	struct q q;
	struct q *qq=&q;
	qq->stack1 = malloc(100000*sizeof(long));
	qq->stack2 = malloc(100000*sizeof(long));
	qq->max1 = malloc(100000*sizeof(long));
	qq->max2 = malloc(100000*sizeof(long));
	q.count = 0;
	q.tail1 = 0;
	q.cap = 99999;
	q.tail2 = 99999;
	long n, i, x;
	char a[7];
	scanf("%ld", &n);
	for(i = 0; i < n; i++) {
		scanf("%s", a);
		if (a[0] == 'E') {
			if (a[1] == 'M') {
				queueempty(&q);
				continue;
			}
			if (a[1] == 'N') {
				scanf("%ld", &x);
				enqueue(&q, x);
				continue;
			}
		}
		if (a[0] == 'D') {
			dequeue(&q);
			continue;
		}
		if (a[0] == 'M') {
			Max(&q);
			continue;
		}
	}
	free(qq->stack1);
	free(qq->stack2);
	free(qq->max1);
	free(qq->max2);	
	return 0;
}