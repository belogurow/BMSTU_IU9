/**
 * Вектор
 * @param {Point} A
 * @param {Point} B
 * @constructor
 */
function Vector(A, B) {
    this.X = B.X - A.X;
    this.Y = B.Y - A.Y;
}