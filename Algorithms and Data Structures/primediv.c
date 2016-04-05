#include <stdio.h>
#include <math.h>

int main()
{
        long x, i, j;
        scanf("%ld", &x);
        x = abs(x);
        long y = sqrt(x);
	char a[y];
	for (i = 0; i<= y; ++i)
		if (i <= 1)
			a[i] = 0;
		else
			a[i] = 1;
	for (i = 2; i*i <= y; ++i)
	{
		if (a[i] == 1)
		{	
			for (j = i*i; j <= y; j = j + i)
				a[j] = 0;  
		}	
	}
	for (i = 0; i <= sqrt(x) ; ++i)
		if ((a[i] == 1) && (x % i == 0)) {
			x = x / i;
			i = 0;
		}
			

			printf("%ld\n", x); 
	return 0;
}
 