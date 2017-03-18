/**
  * Created by alexbelogurow on 18.03.17.
  *
  * Вектор с вещественными коэффициентами в трёхмерном пространстве с операциями сложения («+»),
  * вычитания («-»), скалярного произведения («*»), векторного произведения («**»),
  * а также умножения на число («*»).
  *
  */

class CustomVector(ax: Double, bx: Double, cx: Double) {
  val a = ax
  val b = bx
  val c = cx

  def + (v: CustomVector) = new CustomVector(a + v.a, b + v.b, c + v.c)
  def - (v: CustomVector) = new CustomVector(a - v.a, b - v.b, c - v.c)

  def ** (v: CustomVector) = new CustomVector(b * v.c - c * v.b, c * v.a - a * v.c, a * v.b - b * v.a)

  def * (v: CustomVector) = new CustomVector(a * v.a, b * v.b, c * v.c)
  def * (k: Int) = new CustomVector(a * k, b * k, c * k)

  override def toString: String = s"($a, $b, $c)"
}

class CustomVectorFactor(k: Int) {
  def * (v: CustomVector) = v * k
}

object Lab2 {
  def main(args: Array[String]) {

    implicit def intToFactor(i : Int): CustomVectorFactor = new CustomVectorFactor(i)

    val vector = new CustomVector(5, 1, 3)
    val vector2 = new CustomVector(3.1, 0, -1)
    println(vector + vector2)
    println(2 * (vector - vector2))

  }
}
