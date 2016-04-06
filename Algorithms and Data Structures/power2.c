#include <stdio.h>
int main(int argc, char **argv)
{
        int i, n, j, k = 0, p;
        scanf("%d", &n);
	int posl[n];
	for(i = 0; i < n; i++)
		scanf("%d", &posl[i]);
	int b[n], sum;
	for(i = 0; i < n; i++)
		b[i] = 0;	
	i = 0;
	do {
		sum = 0;		
		for(j = 0; j < n; j++) 
			if (b[j] == 1)
				sum += posl[j];
		if (sum)
			k += !(sum & (sum-1));
		i++;
		p = 0;
		j = i;
		while (j % 2 == 0) {
			j /= 2;
			p++;
		}
		if (p < n)
			b[p] = 1 - b[p];
	}
	while (p < n);
	printf("%d\n", k);

	return 0;
} 