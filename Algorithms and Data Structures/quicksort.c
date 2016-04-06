#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void selectsort(int low, int high, int *p)
{
        int j = high, i, k;
	while (j > low) {
		k = j;
		i = j - 1;
		while (i >= 0) {
			if (p[k] < p[i]) 
				k = i;
			i--;
		}
		int swap = p[j];
		p[j] = p[k];
		p[k] = swap;
		j--;
	}
}

int partition(int low, int high, int *p)
{
	int i = low, j = low, swap;
	while (j < high) {
		if (p[j] < p[high]) {
			swap = p[i];
			p[i] = p[j];
			p[j] = swap;
			i++;
		}
		j++;
	}
	swap = p[i];
	p[i] = p[high];
	p[high] = swap;
	return i;
}
void quicksortrec(int low, int high, int m, int *p)
{
	while (low < high) {
		if (m >= (high - low)) {
			selectsort(low, high, p); 
			return;
			}
		int q = partition(low, high, p);
		if ((low - q) < (high - q)) {
			quicksortrec(low, q-1, m, p);
			low = q+1;
		}
		else {
			quicksortrec(q + 1, high, m, p);
			high = q-1;
		
		}
	}
}
int main(int argc, char **argv)
{
 	int i, n, m;
	scanf("%d %d", &n, &m);
	int a[n];
	for(i = 0; i < n; i++) 
		scanf("%d", &a[i]);
	
	quicksortrec(0, n-1, m, a);
	//selectsort(0, 1, a);
	for(i = 0; i < n; i++)
		printf("%d ", a[i]);      
	return 0;
}
