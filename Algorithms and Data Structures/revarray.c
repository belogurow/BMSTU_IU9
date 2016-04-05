
void revarray(void *base, unsigned long nel, unsigned long width) 
{ 
        int i, j, t;
	for(i = 0; i < nel/2; i++)
		for(j = 0; j < width; j++)
		{
			t = *((char*)base + i*width + j);
			*((char*)base + i*width + j) = *((char*)base + width*(nel - i - 1) + j);
			*((char*)base + width * (nel - i - 1) + j) = t;
		}
	
}
