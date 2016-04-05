#include <stdio.h>
int main(int argc, char **argv)
{
        unsigned int a = 0, b = 0, k = 1, n, i, x, maxa, maxb;
	scanf("%d", &n);
	for (i = 0; i < n; ++i)
	{
		scanf("%d", &x);
		if (i == 0)
			maxa = x;
		else
		{
			if (x > maxa)
				maxa = x;
		}
		a += k << x;
	}
	scanf("%d", &n);
	for (i = 0; i < n; ++i)
	{
		scanf("%d", &x);
		if (i == 0)
			maxb = x;
		else
		{
			if (x > maxb)
				maxb = x;
		}
		b += k << x;
	}
	unsigned int ab;
	ab = a & b;	
	if (maxa > maxb)
		maxa = maxb;
	for (i = 0; i <= maxa; ++i)
		if (ab & (k << i))
			printf("%d ", i);
	printf("\n");
	
	
	return 0;
}