#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

void kadane(float *a, int n)
{
        int l1 = n-1, r1 = n-1, start = n-1, i = n-1;
	float maxsum = a[n-1], sum = 0;
	while (i >= 0) {
		sum += a[i];
		//printf("i - %d, sum=%.0f, maxsum=%.0f\n", i, sum, maxsum);
		//printf("1 %d - %d, 2 %d - %d\n", r1, l1, r2, l2);	
		if (sum >= maxsum) {
			maxsum = sum;
			l1 = start;
			r1 = i;
		}
		//printf("   i - %d, sum=%.0f, maxsum=%.0f\n", i, sum, maxsum);
		//printf("1 %d - %d, 2 %d - %d\n\n", r1, l1, r2, l2);	
		i--;
		if (sum < 0) {
			sum = 0;
			start = i;
		}
	}
	printf("%d %d\n", r1, l1);
}

int main(int argc, char **argv)
{
	int n, i;
	scanf("%d", &n);
	float *a = (float*)malloc(n*sizeof(float));
	float x1, x2;
	for(i = 0; i < n; i++) {
		scanf("%f/%f", &x1, &x2);
		a[i] = log(x1/x2);
		//scanf("%f", &a[i]);
	}
	
	//for(i = 0; i < n; i++)
	//	printf("%f ", a[i]);
	//printf("\n");
	kadane(a, n);
	free(a);
	return 0;
}
