/**
 * Треугольник
 * @constructor
 */
function Triangle() {
    /**
     * Ребра треугольника
     * @type {Array<Rib>}
     */
    this.ribs = new Array(3);

    /**
     * Вершины треугольника
     * @type {Array<Point>}
     */
    this.verticies = new Array(3);
}

/**
 * Найти противоположное ребро к переданной вершине
 * @param {Point} vertex Вершина треугольника
 * @returns {Rib} Противоположное ребро к вершине
 */
Triangle.prototype.getOppositeRib = function(vertex) {
    var i = 0;
    for (; i < 3; i++) {
        if (equals(this.verticies[i], vertex)) {
            break
        }
    }

    switch (i) {
        case 0: return this.ribs[1];
        case 1: return this.ribs[2];
        case 2: return this.ribs[0];
    }

    throw new Error("ArgumentException");
};

/**
 * Найти противоположную вершину к переданному ребру
 * @param {Rib} rib Ребро треугольника
 * @returns {Point} Противоположная вершина к ребру
 */
Triangle.prototype.getOppositeNode = function(rib) {
    var i = 0;
    for (; i < 3; ++i)
        if (equals(this.ribs[i], rib)) {
            break;
        }

    switch (i) {
        case 0: return this.verticies[2];
        case 1: return this.verticies[0];
        case 2: return this.verticies[1];
    }

    throw new Error("ArgumentException");
};

/**
 * Найти индекс смежного ребра к переданному треугольнику
 * @param {Triangle} T Смежный треугольник
 * @returns {number} Индекс
 */
Triangle.prototype.getAdjacentRibIndex = function(T) {
    for (var i = 0; i < 3; i++) {
        var rib = this.ribs[i];
        if (equals(rib.T1, T) || equals(rib.T2, T)) {
            return i;
        }
    }
    throw new Error("ArgumentException");
};

/**
 * Найти индекс противоположноой вершины к переданному ребру
 * @param {number} ribIndex Индекс ребра
 * @returns {number} Индекс
 */
Triangle.prototype.getOppositeNodeIndex = function(ribIndex) {
    switch (ribIndex) {
        case 0: return 2;
        case 1: return 0;
        case 2: return 1;
    }
    throw new Error("ArgumentException");
};

/**
 * Найти индекс вершины
 * @param {Point} vertex Вершина треугольника
 * @returns {number} Индекс
 */
Triangle.prototype.getVertexIndex = function(vertex) {
    for (var i = 0; i < 3; i++) {
        if (equals(this.verticies[i], vertex)) {
            return i;
        }
    }
    throw new Error("ArgumentException");
};

Triangle.prototype.getRibIndex = function(rib) {
    for (var i = 0; i < 3; i++) {
        if (equals(this.ribs[i], rib)) {
            return i;
        }
    }
    throw new Error("ArgumentException");
};

/**
 * Найти индекс ребра, которое содержит переданные вершины
 * @param {number} A Индекс вершины треугольника
 * @param {number} B Индекс вершины треугольника
 * @returns {number} Индекс ребра
 */
Triangle.prototype.getRibIndexForPoints = function(A, B) {
    var a = Math.min(A, B);
    var b = Math.max(A, B);
    // a == 0.
    // b == 1 | 2.
    if (a === 0) {
        if (b === 1)
            return 0;
        // b == 2.
        return 2;
    }
    // a == 1.
    // b == 2.
    return 1;
};

/**
 * Обновить ссылка на новое ребро по передаваемому индексу
 * @param {number} index Индекс изменяего ребра
 * @param {Rib} newRib Новое ребро
 */
Triangle.prototype.updateRibWithIndex = function(index, newRib) {
    this.ribs[index] = newRib;
};

/**
 * Найти ребро по двум вершинами
 * @param {Point} A Вершина треугольника
 * @param {Point} B Вершина треугольника
 * @returns {Rib} Ребро
 */
Triangle.prototype.getRib = function(A, B) {
    for (var i = 0; i < this.ribs.length; i++) {
        var rib = this.ribs[i];

        if (equals(rib.A, A) && equals(rib.B, B) || equals(rib.A, B) && equals(rib.B, A))
            return rib;
    }

    throw new Error("ArgumentException");
};

/**
 * Обновить ссылку старого ребра на новое ребро
 * @param {Rib} oldRib Старое ребро треугольника
 * @param {Rib} newRib Новое ребро треугольника
 */
Triangle.prototype.updateRib = function(oldRib, newRib) {
    if (equals(this.ribs[0], oldRib))
        this.ribs[0] = newRib;
    else if (equals(this.ribs[1], oldRib))
        this.ribs[1] = newRib;
    else if (equals(this.ribs[2], oldRib))
        this.ribs[2] = newRib;
    else
        throw new Error("ArgumentException");
};

/**
 * Определяет, является ли переданный треугольник смежным к текущему
 * @param {Triangle} T Треугольник
 * @return {boolean} True - если T явялется смежным, иначе False
 */
Triangle.prototype.isAdjacent = function(T) {
    for (var i = 0; i < 3; i++) {
        var rib = this.ribs[i];
        if (equals(rib.T1, T) || equals(rib.T2, T)) {
            return true;
        }
    }
    return false;
};

/**
 * Обновить расположение вершин и ребер треугольника
 */
Triangle.prototype.update = function () {
    var a = this.ribs[0];
    var b = null;
    var c = null;
    var A = a.A;
    var B = a.B;
    var C = new Point();
    var r1 = this.ribs[1];

    if (equals(r1.A, A)) {
        b = this.ribs[2];
        c = r1;
        C = r1.B;
    } else if (equals(r1.B, A)) {
        b = this.ribs[2];
        c = r1;
        C = r1.A;
    } else if (equals(r1.A, B)) {
        b = r1;
        c = this.ribs[2];
        C = r1.B;
    } else if (equals(r1.B, B)) {
        b = r1;
        c = this.ribs[2];
        C = r1.A;
    }

    this.verticies[0] = A;
    this.verticies[1] = B;
    this.verticies[2] = C;

    this.ribs[0] = a;
    this.ribs[1] = b;
    this.ribs[2] = c;
};

/**
 * Обновить массив треугольников
 * @param {array}triangles
 */
Triangle.updateTriangles = function (triangles) {
    triangles.forEach(function (tr) {
        tr.update();
    })
};

/**
 * Определить ребра треугольника
 * @param {Rib} R1 Ребро
 * @param {Rib} R2 Ребро
 * @param {Rib} R3 Ребро
 */
Triangle.prototype.setRibs = function (R1, R2, R3) {
    this.ribs[0] = R1;
    this.ribs[1] = R2;
    this.ribs[2] = R3;
};

