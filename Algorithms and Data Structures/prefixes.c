#include <stdio.h>
#include <string.h>
#include <stdlib.h>
int nod(int a, int b)
{
        int c;
	while (b) {
		c = a % b;
		a = b;
		b = c;
	}
	return a;
}
void prefix(char *s, int *pi)
{
	int t = 0, i = 1;
	pi[0] = 0;
	while (i < strlen(s)) {
		while ((t > 0) && (s[t] != s[i]))
			t = pi[t-1];
		if (s[t] == s[i])
			t++;
		pi[i] = t;
		i++;
	}
}
int main(int argc, char **argv)
{
	int n, i, j = 0, k, test;
	n = strlen(argv[1]);
	char a[n];
	int pi[n];
	strcpy(a, argv[1]);
	prefix(a, pi);
	for (i = 1; i < n; i++) {
		if (pi[i] != 0)
			j = nod(pi[i], i+1);
		test = 1;
		for (k = 0; k + j <= i; k++)
			if (a[k] != a[k+j])
				test = 0;
		
		if ((test == 1) && (j != 0))
			printf("%d %d\n", i+1, (i+1)/j);
	}			
	
	return 0;
}
