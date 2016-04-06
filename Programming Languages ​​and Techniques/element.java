public class Element<T> { 

        private T unit;
        private int rank;
	private Element<T> par;

        // ������ ����� ������� �� ��������� x 
        public Element(T x) 
        { 
		unit = x;
		rank = 0;
		par = this; 
        } 
 
        //���������� �������� �������� 
        public T x() 
        { 
                return unit; 
        } 
	
	public Element<T> findElem() 
	{
		if (this == this.par)
			return this;
		else
			return this.par = this.par.findElem(); 
	}

        // ����������, ����������� �� ������� ������� 
        //���� �� ���������, ��� � ������� elem 
        public boolean equivalent(Element<T> elem) 
        { 
        	return (this.findElem() == elem.findElem());
        } 
 	
        // ���������� ���������, �������� ����������� ������� 
        // �������, � ����������, �������� ����������� 
        // ������� elem 
        public void union(Element<T> elem) 
        { 
        	Element<T> one = findElem(), two = elem.findElem();
		if (one.rank < two.rank) 
			one.par = two;
		else {
			two.par = one;
			if (one.rank == two.rank)
				++one.rank;
		}
		        
        } 
}