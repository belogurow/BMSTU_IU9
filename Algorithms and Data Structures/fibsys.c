#include <stdio.h>
int main(int argc, char **argv)
{
        unsigned long x;
        scanf("%lu", &x);
	if (x == 0) 
		printf("%lu\n", x);
	else
	{
		unsigned long fib[500], n, i = -1;
		for (;;) {
			++i;			
			if (i <= 1)
				fib[i] = i;
			else
				fib[i] = fib[i-1] + fib[i-2];
			if (fib[i] > x) {
				n = i;
				break;
			}
		}
		int a[n];
		for (i = 1; i <= n; ++i)
			a[i] = 0;
		while (x > 0) {
			i = 0;		
			for (;;) {
								
				if (fib[++i] > x) {			
					a[--i] = 1;
					break;
				}
			} 
			x -= fib[i];
		}		
		for (i = --n; i > 1; --i)
			printf("%d", a[i]);
			printf("\n");
	}	
	return 0; 
}