#include <stdio.h>
#include <string.h>
#include <stdlib.h>
char *fibstr(int n) 
{ 
    int i, k = 0, fib0 = 1, fib1 = 1, fib;
        for (i = 1; i <= n; i++) {
		if (i <= 2)
			fib = 1;
		else {
			fib = fib0 + fib1;
			fib0 = fib1;
			fib1 = fib;
		}
	} 
	char *f = (char*)malloc((fib+1)*sizeof(char));
	char *f0 = (char*)malloc((fib+1)*sizeof(char));
	char *f1 = (char*)malloc((fib+1)*sizeof(char));
	strcpy(f0, "a");
	strcpy(f1, "b");
	strcpy(f, "");
	if (n <= 2) {
		if (n == 1) 
			strcpy(f, f0);
		else
			strcpy(f, f1);
	}
	else {
		for(i = 3; i <= n; i++) {
			strcpy(f, f0);			
			strcat(f, f1);
			strcpy(f0, f1);
			strcpy(f1, f);
		}
	}
	free(f0);
        free(f1);
        return f;
}
 

int main(int argc, char **argv)
{
        int n;
	scanf("%d", &n);
	char *fib = fibstr(n);	
	printf("%s\n", fib);
	free(fib);	
	return 0;
	
}