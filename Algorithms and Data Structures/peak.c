unsigned long peak(unsigned long nel, 
        int (*less)(unsigned long i, unsigned long j)) 
{ 
        unsigned long low = 0, high = nel - 1, mid = low + (high - low)/2;
        while (low < high)  
	{
		mid = low + (high - low)/2;
		if (less(mid, mid - 1) == 1)
			high = mid;
		else 
		{
			if (less(mid, mid + 1) == 1)
				low = mid;
			else
				break;
		}
	}
	mid = low + (high - low) / 2;
	return mid;
}
