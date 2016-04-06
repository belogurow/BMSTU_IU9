void shellsort(unsigned long nel, 
        int (*compare)(unsigned long i, unsigned long j), 
        void (*swap)(unsigned long i, unsigned long j)) 
{ 
        int fib[500], i, fibn;
        for(i = 0; i < 500; i++) {
		if (i <= 1)
			fib[i] = 1;
		else 
			fib[i] = fib[i-1] + fib[i-2];
		if (fib[i] >= nel)
			break;
	}
	fibn = --i; 
	int j, loc;
	for(fibn; fibn >= 1; fibn--) {
		for(j = 0; j + fib[fibn] < nel; j++) {
			i = j + fib[fibn];
			loc = i - fib[fibn];
			while (loc >= 0 && compare(loc, i) == 1) {
				swap(i, loc);
				loc -= fib[fibn];
                                i -=fib[fibn];
			}
			
		}
	}
}