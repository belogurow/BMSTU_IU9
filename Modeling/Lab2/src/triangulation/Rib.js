/**
 * Ребро треугольника
 * @param {Point} A Вершина ребра
 * @param {Point} B Вершина ребра
 * @param {Triangle} T1 Ссылка на смежный треугольник
 * @param {Triangle} T2 Ссылка на смежный треугольник
 * @constructor
 */
function Rib(A, B, T1, T2) {
    this.A = A;
    this.B = B;
    this.T1 = T1;
    this.T2 = T2;

    this.triangles = [T1, T2]
}


/**
 * Обновить ссылки на смежные треугольники
 * @param {Triangle} oldTriangle Треугольник, который надо заменить на новый
 * @param {Triangle} newTriangle Новый треугольник
 */
Rib.prototype.update = function(oldTriangle, newTriangle) {
    if (equals(this.T1, oldTriangle)) {
        this.T1 = newTriangle;
    } else if (equals(this.T2, oldTriangle)) {
        this.T2 = newTriangle
    } else {
        throw new Error('ArgumentException')
    }
};

/**
 * Возвращает противоположный треугольник
 * @param {Triangle} T Треугольник, примыкающий к ребру
 * @returns {Triangle} Противоположный треугольник, примыкающий к ребру
 */
Rib.prototype.getAdjacent = function(T) {
    if (equals(this.T1, T)) {
        return this.T2
    }  else if (equals(this.T2, T)) {
        return this.T1
    }

    throw new Error('ArgumentException')
};