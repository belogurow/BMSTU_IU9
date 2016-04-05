
#include <stdio.h>

int main()
{
        int i, j;
        int a[8], b[8];
	for (i = 0; i < 8; ++i)
		scanf("%d", &a[i]);
	for (i = 0; i < 8; ++i)
		scanf("%d", &b[i]);
	for (i = 0; i < 8; ++i)
		for (j = 0; j < 8; ++j)
			{ 
                                if (a[i] == b[j]) 
                                {
				        a[i] = 0;
				        b[j] = 0;				
				        break; 
                                } 
                        }
	int x = 0;		
	for (i = 0; i < 8 ; ++i)
		if ((a[i] != 0) || (b[i] != 0))
			x = 1;	
	if (x == 1)
		printf("no\n");
	else
		printf("yes\n");  
	return 0;
}
