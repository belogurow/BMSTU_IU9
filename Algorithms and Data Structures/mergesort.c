#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void insert(int*, int, int);
void merge(int*, int, int, int);
void mergesort(int*, int);
void mergesortrec(int*, int, int);

int main(int argc, char **argv)
{
        int n, i;
	scanf("%d", &n);
	int a[n];
	for(i = 0; i < n; i++)
		scanf("%d", &a[i]);
	mergesort(a, n);
	for(i = 0; i < n; i++)
		printf("%d ", a[i]);
	printf("\n");
	return 0;
}
void merge(int *p, int k, int l, int m)
{
	int t[m-k+1], i = k, j = l + 1, h = 0;
	while (h < m-k+1) {
		if (j <= m && (i == l + 1 || abs(p[j])<abs(p[i]))) {
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
void mergesort(int *p, int n)
{
	mergesortrec(p, 0, n - 1);
}
void mergesortrec(int *p, int low, int high)
{
	int med;	
	if (low<high) {
		med = (low+high)/2;
		if (med < 5) {
			insert(p, low, med);
			insert(p, med+1, high);
		}
		else {
			mergesortrec(p, low, med);
			mergesortrec(p, med+1, high);
		}		
		merge(p, low, med, high);
	}
}
void insert(int *base, int a, int b)
{
	int i = a + 1, loc, swap;
	while (i <= b) {
		loc = i - 1;
		while ((loc >= a) && (abs(base[loc+1]) < abs(base[loc]))) {
			swap = base[loc+1];
			base[loc+1] = base[loc];
			base[loc] = swap;
			loc--;
		}
		i++;
	}
}