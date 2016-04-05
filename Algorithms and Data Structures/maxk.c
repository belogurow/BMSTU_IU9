
#include <stdio.h>
int main(int argc, char **argv)
{
        int a[100000], max = 0, test0, test1, n, k, i;
	scanf("%d", &n);
	for (i = 0; i < n; ++i)
		scanf("%d", &a[i]);
	scanf("%d", &k);
	for (i = 0; i < k; ++i)
		max += a[i];
	test0 = max;
	for (i; i < n; ++i)
		{
			test1 = test0 + a[i] - a[i-k];
			if (test1 > max) 
				max = test1;
			test0 = test1;
		} 
	printf("%d\n", max);
	return 0;
}