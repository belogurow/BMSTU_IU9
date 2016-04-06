#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

void DIV(long x, long z, long y, unsigned long *a, int *k) {
        a[*k] = y;
	//printf("a[%d] = %ld\n", *k, a[*k]);
	(*k)++;
	if (z != y) {
		for(y++; y * y <= x && x % y != 0; y++);
		if (y <= sqrt(x)) 
			DIV(x, x/y, y, a, k);
		a[*k] = z;
		//printf("a[%d] = %ld\n", *k, a[*k]);
		(*k)++;
	}
}

int main(int argc, char **argv)
{	
	unsigned long *a = (unsigned long*)malloc(sizeof(unsigned long)*6000);
	long i, x;
	scanf("%ld", &x);
	int k = 0, p = 0;
	DIV(x, x, 1, a, &k);
	printf("graph {\n");
	unsigned long *div1 = (unsigned long*)malloc(sizeof(unsigned long)*11000);
	unsigned long *div2 = (unsigned long*)malloc(sizeof(unsigned long)*11000);
	for(i = k-1; i >= 0; i--)
		printf("\t %ld\n", a[i]);
	for(i = k-1; i >= 0; i--) {
		int q;
		for(q = i-1; q >= 0; q--) {
			if(a[i] % a[q] == 0) {
				int j, test = 1;
				for(j = i-1; j > q; j--) {
					if (a[i] % a[j] == 0 && a[j] % a[q] == 0) {
						test = 0;
						break;
					}
				}
				if (test) {
					div1[p] = a[i];
					div2[p] = a[q];
					p++;
				}
			}
		}
	}
	for(i = 0; i < p; i++)
		printf("\t %ld -- %ld\n", div1[i], div2[i]);
	printf("}");	
	free(div1);
	free(div2);
	free(a);


	return 0;
}	