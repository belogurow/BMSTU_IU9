void bubblesort(unsigned long nel, 
        int (*compare)(unsigned long i, unsigned long j), 
        void (*swap)(unsigned long i, unsigned long j)) 
{ 
        int tr, tl, boundr, boundl = 0, i, q = 1;
        tr = nel - 1;
	tl = 0;
	while (tr - tl != 0) {
		boundr = tr;
		boundl = tl;
		if (q == 1) {
			tr = boundl;
			i = boundl;
		}
		else {
			tl = boundr;
			i = boundr;
		}
		//printf("q = %d, boundr = %d, boundl = %d\n", q, boundr, boundl);
		if (q == 1) {
			while (i < boundr) {
				if (compare(i+1, i) == -1) {
					swap(i+1, i);
					tr = i;
				}
			i++;
			}
		}
		else {
			while (i > boundl) {
				if (compare(i-1, i) == 1) {
					swap(i-1, i);
					tl = i;
				}
			i--;
			}
		}
		if (q == 1) 
			q = -1;
		else  
			q = 1;
	}
}				