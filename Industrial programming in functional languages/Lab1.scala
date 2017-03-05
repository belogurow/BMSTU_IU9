/**
  * Created by alexbelogurow on 18.02.17.
  * 
  * Функция peaks: List[Int] => List[Int], формирующая список индексов пиков последовательности 
  * (пик – такой элемент, что соседние элементы его не превышают).
  *
  */
  
object Lab1 {
  def main(args: Array[String]) {
    println("Hello, world!")

    val peak_help: (List[Int],Int) => List[Int] = {
      case (a :: Nil,0) => List(0);
      case (a :: b :: list, 0) if (a>b) => 0 :: peak_help(a :: b :: list, 1);
      case (a :: b :: list, 0) => peak_help(a :: b :: list, 1);

      case (a :: b :: c :: list,x) if (b>c) && (b>a) => x :: peak_help(b::c::list,x+1);
      case (a :: b :: c :: list,x) => peak_help(b::c::list,x+1);

      case (a :: b :: Nil, x) if (b>a) => x :: peak_help(Nil,x+1);
      case (a :: b :: Nil, x) => peak_help(Nil,x+1);
      case _ => Nil
    }

    val peaks: List[Int] => List[Int] = {
      case x => peak_help(x,0)
    }

    var list = List(5,11)

    var s = peaks(list)

    println(s)

  }
}
