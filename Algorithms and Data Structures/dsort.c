#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void distributionsort(char *a, int n, char *d) 
{
        char key[26] = "abcdefghijklmnopqrstuvwxyz";
	int count[26] = {0};
	int j = 0, i = 1, m = 26, k;
	while (j < n) {
		for (k = 0; k < m; k++)
			if (a[j] == key[k]) {
				count[k]++;
				break;
			}
		j++;
	}
	while (i < m) {
		count[i] += count[i-1];
		i++;
	} 
	j = n - 1;
	while (j >= 0) {
		for(k = 0; k < 26; k++)
			if (a[j] == key[k]) 
				break;
		
		i =  count[k] - 1;
		count[k] = i;
		d[i] = a[j];
		j = j - 1;
	}
	d[n] = '\0';
}
int main(int argc, char **argv)
{
	char *a = (char*)malloc(1000000*sizeof(char));
	scanf("%s", a);
	int n;
	n = strlen(a);
	char *d = (char*)malloc((n+1)*sizeof(char));
	distributionsort(a, n, d);
	printf("%s\n", d);
	free(a);
	free(d);	

	return 0;
}