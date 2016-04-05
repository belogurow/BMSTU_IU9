
int maxarray(void *base, unsigned long nel, unsigned long width, 
        int (*compare)(void *a, void *b)) 
{ 
        int i, test, nummax = 0, j;
	for (i = 0; i < nel ; i++) {
		if (compare((char*)base + (nummax)*width, (char*)base + (i)*width) < 0)
			nummax = i;
	}
	return nummax;

}
