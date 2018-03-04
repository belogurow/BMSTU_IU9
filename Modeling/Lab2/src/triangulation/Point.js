/**
 * Точка в пространстве
 * @param X Координата X
 * @param Y Координата Y
 * @param Z Координата Z
 * @constructor
 */
function Point(X, Y, Z) {
    this.X = X;
    this.Y = Y;
    this.Z = Z;
}

/**
 * Определяет находится ли передаваемая точка в эпсилон окрестности текущей точки
 * @param {Point} point
 * @returns {boolean} True, если находится внутри эпсилон окрестности, иначе False
 * */
Point.prototype.isInEpsilonAreaPoint = function(point) {
    return isInEpsilonArea(point.X, this.X) && isInEpsilonArea(point.Y, this.Y);
};
