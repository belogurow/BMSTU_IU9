#include <stdio.h>
#include <string.h>
#include <stdlib.h>
struct q {
        long count;
        long head;
	long tail;
	long cap;
	long *data;
};
void queueempty(struct q *q)
{
	if (q->count == 0) 
		printf("true\n");
	else
		printf("false\n");
} 
void initqueue(struct q *q)
{
	long b = q->cap;
	q->cap *= 2;
	long *a = (long*)malloc((q->cap)*sizeof(long));
	long i, j;
	for(i = 0; i < (b - q->head); i++)
		a[i] = q->data[q->head + i];
	q->head = 0;
	for(j = 0; j < q->tail; j++)
		a[j+i] = q->data[j];
	q->tail = j+i;
	free(q->data);
	q->data = a; 
}
void enqueue(struct q *q, long x)
{
	if (q->count == q->cap)
		initqueue(q);	
	q->data[q->tail] = x;
	q->tail++;
	if (q->tail == q->cap)
		q->tail = 0;
	q->count++;
}
void dequeue(struct q *q)
{
	printf("%ld\n", q->data[q->head]);
	q->head++;
	if (q->head == q->cap)
		q->head = 0;
	q->count--;
}
int main(int argc, char **argv)
{
	struct q q;
	struct q *qq=&q;
	q.count = 0;
	q.head = 0;
	q.tail = 0;
	q.cap = 4;
	qq->data = malloc((q.cap)*sizeof(long));
//	long *a = (long*)malloc(4*sizeof(long));
//	qq->data[0] = 40;
//	a[0] = qq->data[0]+10;	
//	qq->data = a;	
//	for(i = 0; i < 4; i++) {
//		q.data[i] = i;
//		printf("%ld\n", qq->data[0]);
//	}
//	free(q.data);
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
	}
        free(qq->data);
	return 0;
}