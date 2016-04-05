#include <stdio.h>
int main(int argc, char **argv)
{
        int a[10][10], i, j, str, stl, maxstr[10], minstl[10];
	scanf("%d", &str);
	scanf("%d", &stl);
	for (i = 0; i < str; i++)
		for (j = 0; j < stl; j++) {
			scanf("%d", &a[i][j]);		
			if (j == 0) 
				maxstr[i] = a[i][j];
			else {
				if (a[i][j] > maxstr[i])
					maxstr[i] = a[i][j];
			}
		/* printf("maxstr[%d] = %d\n", i, maxstr[i]); */
		}
	for (j = 0; j < stl; j++) 
		for (i = 0; i < str; i++) {
			if (i == 0)
				minstl[j] = a[i][j];
			else {
				if (a[i][j] < minstl[j])
					minstl[j] = a[i][j];
			}
		/* printf("minstl[%d] = %d\n", j, minstl[j]); */
		}
	int k = 0;
	for (i = 0; i < str; i++) {
		for (j = 0; j < stl; j++)
			if (maxstr[i] == minstl[j]) {
				printf("%d %d\n", i, j);
				k = 1;
				}				
		
	}
	if (k == 0)
		printf("none\n");
	
	return 0;
}