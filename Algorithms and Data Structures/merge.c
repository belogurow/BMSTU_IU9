#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void merge(int*, int, int, int);

int main(int argc, char **argv)
{
        int n = 0, i, j, k;
        scanf("%d", &k);
	int number[k];
	for(i = 0; i < k; i++) {
		scanf("%d", &number[i]);
		n += number[i];
	}
	int a[n], q = 0, sum1 = 0, sum2 = 0;
	for(i = 0; i < k; i++) {
		for(j = 0; j < number[i]; j++) {
			scanf("%d", &a[q]);
			q++;
		}
		sum1 = sum2;
		sum2 += number[i];
		if (i > 0)
			merge(a, 0, sum1 - 1, sum2 - 1);
	}
	for(i = 0; i < n; i++)
		printf("%d ", a[i]);
	printf("\n");
	return 0;
}
void merge(int *p, int k, int l, int m)
{
	int t[m-k+1], i = k, j = l + 1, h = 0;
	while (h < m-k+1) {
		if (j <= m && (i == l + 1 || (p[j])<(p[i]))) {
			t[h] = p[j];
			j++;
		}
		else {
			t[h] = p[i];
			i++;
		}
		h++;
	}
	for(i = 0; i <= (m-k); i++)
		p[k + i] = t[i]; 
}