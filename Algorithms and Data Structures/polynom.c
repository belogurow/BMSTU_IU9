
#include <stdio.h>

int main()
{       
        long a, x, n, p, pr;
        scanf("%ld%ld%ld", &n, &x, &a);
	p = a; 
	pr = a * n;
 	for (n ; n > 0 ; n = n - 1)
	{
		scanf("%ld", &a);
		p = p * x + a;
		if (n == 1) 
			x = 1;
		pr = pr * x + a * (n - 1);			
				
	}
	printf("%ld %ld\n", p, pr); 
	return 0;
}
