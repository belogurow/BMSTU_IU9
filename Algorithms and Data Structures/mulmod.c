#include <stdio.h>
int main(int argc, char **argv)
{
        long a, b, b1, m, bm[63];
	int n, i = 0;
	scanf("%ld %ld %ld", &a, &b, &m); 
	b1 = b;	
	for (;;) {
		bm[i] = b1 % 2;
		b1 /= 2;
		i++;
		if (b1 == 0)
			break; 
	}
	n = --i;
	a = a % m;
	long p = ((bm[n] % m) * a);
	for (--i; i>= 0; i--) {
		p = ((p % m) * (2 % m)) % m; 
		long ost = ((bm[i] % m) * a) % m;
		p = (p + ost) % m;
	}
	printf("%ld\n", p);
	return 0;
}