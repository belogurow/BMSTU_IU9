#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void swap(void *a, void *b, int width)
{
        int i;
	char sw[width];
	char *a1 = a, *b1 = b;
	for(i = 0; i < width; i++) {
		sw[i] = a1[i];
		a1[i] = b1[i];
		b1[i] = sw[i];
	}
}
int compare(const void *a, const void *b)
{
	char *ma =(char*)a;
	char *mb =(char*)b;
	int i, va = 0, vb = 0;
	for(i = 0; ma[i] != '\0'; i++)
		if (ma[i] == 'a')
			va++;
	for(i = 0; mb[i] != '\0'; i++)
		if (mb[i] == 'a')
			vb++;
	if (va == vb)
		return 0;
        return va < vb ? -1 : 1; 
} 
void heapify(void *base, size_t i, size_t n, size_t width)
{
	for(;;) {
		int l = 2*i + 1, r = l + 1, j = i;
		if ((l < n) && (compare(base + i*width,base + l*width) == -1))
			i = l;
		if ((r < n) && (compare(base + i*width,base + r*width) == -1))
			i = r;
		if (i == j)
			break;
		swap(base + i*width, base + j*width, width);
	}
}
void buildheap(void *base, size_t n, size_t width)
{
	int i = n/2 - 1;
	while (i >= 0) {
		heapify(base, i, n, width);
		i--;
	}
}
void hsort(void *base, size_t nel, size_t width, 
        int (*compare)(const void *a, const void *b)) 
{ 
	buildheap(base, nel, width);
	int i = nel - 1;
	while (i > 0) {
		swap(base, base + i*width, width);
		heapify(base, 0, i , width);
		i--;
	}
} 
int main(int argc, char **argv)
{
	int n, i;
	scanf("%d", &n);
	char base[n][100];
	for(i = 0; i < n; i++) 
		scanf("%s", base[i]);
	hsort(base, n, 100, compare);
	for(i = 0; i < n; i++)
		printf("%s\n", base[i]);
	
	
	return 0;
}
