/**
 * Динамический кэш
 * @param {number} minX Минимальное значение по координате X
 * @param {number} maxX Максимальное значение по координате X
 * @param {number} minY Минимальное значение по координате Y
 * @param {number} maxY Максимальное значение по координате Y
 * @constructor
 */
function DynamicCache(minX, maxX, minY, maxY) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;

    this.xUnitSize = undefined;
    this.yUnitSize = undefined;

    // Размер кэша
    this.m = 2;
    // Количество вершин
    this.n = 0;
    // Степень роста
    this.growthRate = 6;
    this.updateUnitSize();

    // 2D Кэш
    this.cache = new Array(this.m);
    for (var i = 0; i < this.m; i++) {
        this.cache[i] = new Array(this.m);
    }
    this.limit = this.growthRate * this.m * this.m;
}

/**
 * Обновить размер ячейки в кэше
 */
DynamicCache.prototype.updateUnitSize = function() {
    this.xUnitSize = (this.maxX - this.minX) / this.m;
    this.yUnitSize = (this.maxY - this.minY) / this.m;
};

/**
 * Увеличене числа вершин
 * @param {number} count
 */
DynamicCache.prototype.incrementNodeCount = function (count) {
    this.n += count;
    if (this.n >= this.limit) {
        this.increaseSize();
    }
};

/**
 * Инициализация кэша 2х2
 */
DynamicCache.prototype.initialize = function(t00, t01, t10, t11) {
    this.cache[0][0] = t00;
    this.cache[0][1] = t01;
    this.cache[1][0] = t10;
    this.cache[1][1] = t11;
};

/**
 * Помещает новый треугольник в кэш
 * @param {Triangle} T Треугольник
 */
DynamicCache.prototype.update = function (T) {
    var xSum = 0;
    var ySum = 0;
    for (var i = 0; i < T.verticies.length; i++) {
        var vert = T.verticies[i];
        xSum += vert.X;
        ySum += vert.Y;
    }
    var row = this.getRow(ySum / 3);
    var col = this.getCol(xSum / 3);
    this.cache[row][col] = T;
};

/**
 * Найти треугольник в кэше по точке
 * @param {Point} node Точка
 * @return {Triangle} Треугольник из кэша
 */
DynamicCache.prototype.get = function(node)
{
    var row = this.getRow(node.Y);
    var col = this.getCol(node.X);
    return this.cache[row][col];
};

/**
 * Найти индекс столбца кэша по переданному значению
 * @param {number} value
 * @return {number} Индекс столбца кеша
 */
DynamicCache.prototype.getCol = function(value) {
    return Math.floor((value - this.minX) / this.xUnitSize);
};

/**
 * Найти индекс строки кэша по переданному значению
 * @param {number} value
 * @return {number} Индекст строки кэша
 */
DynamicCache.prototype.getRow = function(value) {
    return Math.floor((value - this.minY) / this.yUnitSize);
};

/**
 * Увеличить размер кэша
 */
DynamicCache.prototype.increaseSize = function() {
    var newSize = this.m * 2;
    var newCache = new Array(newSize);
    for (var i = 0; i < newSize; ++i) {
        newCache[i] = new Array(newSize);
    }
    // c[x,y] -> c[2x,2y],c[2x+1,2y],c[2x,2y+1],c[2x+1,2y+1].
    for (i = 0; i < this.m; ++i) {
        for (var j = 0; j < this.m; ++j) {
            var doubleI = i * 2;
            var doubleJ = j * 2;
            var value = this.cache[i][j];
            newCache[doubleI][doubleJ] = value;
            newCache[doubleI][doubleJ + 1] = value;
            newCache[doubleI + 1][doubleJ] = value;
            newCache[doubleI + 1][doubleJ + 1] = value;
        }
    }

    this.cache = newCache;
    this.m = newSize;
    this.limit = this.growthRate * this.m * this.m;
    this.updateUnitSize();
};