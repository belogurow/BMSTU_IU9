unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i)) 
{ 
        unsigned long low = 0, high = nel - 1, mid;
        while (low <= high)  {
		mid = (low + high)/2;
		if (compare(mid) == -1)
			low = mid + 1;
		if (compare(mid) == 1)
			high = mid - 1;
		if (compare(mid) == 0)
			return mid;
		}
	return nel;
}