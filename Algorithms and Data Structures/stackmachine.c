#include <stdio.h>
#include <string.h>
#include <stdlib.h>
struct s {
        long *data;
	long top;
	long cap;
};
void CONST(struct s *stack, long x)
{
	if (stack->top == stack->cap) {
		stack->cap *= 2;
		stack->data = realloc(stack->data, stack->cap*sizeof(long));
	}
	stack->data[stack->top] = x;
	stack->top++;
}
void ADD(struct s *stack)
{
	stack->data[stack->top-2] += stack->data[stack->top-1];
	stack->top--;
}
void SUB(struct s *stack)
{
	stack->data[stack->top-2] = stack->data[stack->top-1] - stack->data[stack->top-2];
	stack->top--;
}
void MUL(struct s *stack)
{
	stack->data[stack->top-2] *= stack->data[stack->top-1];
	stack->top--;
}
void MAx(struct s *stack)
{
	if (stack->data[stack->top-2] < stack->data[stack->top-1])
		stack->data[stack->top-2] = stack->data[stack->top-1];
	stack->top--;
}
void MIn(struct s *stack)
{
	if (stack->data[stack->top-2] > stack->data[stack->top-1])
		stack->data[stack->top-2] = stack->data[stack->top-1];
	stack->top--;
}
void DIV(struct s *stack)
{
	stack->data[stack->top-2] = stack->data[stack->top-1] / stack->data[stack->top-2];
	stack->top--;
}
void NEG(struct s *stack)
{
	stack->data[stack->top-1] *= (-1);
}
void SWAP(struct s *stack)
{
	int swap = stack->data[stack->top-1];
	stack->data[stack->top-1] = stack->data[stack->top-2];
	stack->data[stack->top-2] = swap;
}
int main(int argc, char **argv)
{
	struct s stack;
	struct s *ss=&stack;
	stack.top = 0;
	stack.cap = 4;
	ss->data = malloc((stack.cap)*sizeof(long));
	long n, i, x;
	scanf("%ld", &n);
	char a[7];
	for(i = 0; i < n; i++) {
		scanf("%s", a);
		if (a[0] == 'C') {
			scanf("%ld", &x);
			CONST(&stack, x);
			continue;
		}
		if (a[0] == 'A') {
			ADD(&stack);
			continue;
		}
		if (a[0] == 'S') {
			if (a[1] == 'U') {
				SUB(&stack);
				continue;
			}
			if (a[1] == 'W') {
				SWAP(&stack);
				continue;
			}
		}
		if (a[0] == 'M') {
			if (a[1] == 'U') {
				MUL(&stack);
				continue;
			}
			if (a[1] == 'A') {
				MAx(&stack);
				continue;
			}
			if (a[1] == 'I') {
				MIn(&stack);
				continue;
			}
		}
		if (a[0] == 'D') {
			if (a[1] == 'I') {
				DIV(&stack);
				continue;
			}
			if (a[1] == 'U') {
				CONST(&stack, stack.data[stack.top-1]);
				continue;
			}
		}
		if (a[0] == 'N') {
			NEG(&stack);
			continue;
		}			
	}
	//for(i = 0; i < stack.top; i++)
	//	printf("%lld ", stack.data[i]);
	//printf("\nTOP - %lld\n", stack.top);     
	printf("%ld\n", stack.data[stack.top-1]);
	free(ss->data);	
	return 0;
}