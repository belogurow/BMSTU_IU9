#include <stdio.h>
#include <string.h>
#define SPACE ' '

void print(char sym, int len)
{
        int i;
	for(i = 1; i <= len; i++)
		putchar(sym);
}
int main(int argc, char **argv)
{
	int i, w, h, mid;
	h = atoi(argv[1]);
	w = atoi(argv[2]);
	if (h % 2 == 1)
		mid = h/2 + 1;
	else 
		mid = h/2;
	if (argv[1] == NULL || argv[2] == NULL || argv[3] == NULL)
		printf("Usage: frame <height> <width> <text>\n");
	else {
		if (strlen(argv[3]) > w-2 || h <= 2)
			printf("Error\n");
		else {
			for(i = 1; i <= h; i++) {
				if (i == 1 || i == h)
					print('*', w);
		
				else 
					if (i == mid) {
						print('*', 1);
						print(SPACE, (w-strlen(argv[3]))/2 - 1);
						printf("%s", argv[3]);
						print(SPACE, w - strlen(argv[3]) - (w-strlen(argv[3]))/2 - 1 );
						print('*', 1);
					}
				else {
						print('*', 1);
						print(SPACE, w-2);
						print('*', 1);
				}
			printf("\n");
			}  
		}
	}
	return 0;
}