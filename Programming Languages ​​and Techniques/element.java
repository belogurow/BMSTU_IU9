public class Element<T> { 

        private T unit;
        private int rank;
	private Element<T> par;

        // Создаёт новый элемент со значением x 
        public Element(T x) 
        { 
		unit = x;
		rank = 0;
		par = this; 
        } 
 
        //Возвращает значение элемента 
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

        // Определяет, принадлежит ли текущий элемент 
        //тому же множеству, что и элемент elem 
        public boolean equivalent(Element<T> elem) 
        { 
        	return (this.findElem() == elem.findElem());
        } 
 	
        // Объединяет множество, которому принадлежит текущий 
        // элемент, с множеством, которому принадлежит 
        // элемент elem 
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