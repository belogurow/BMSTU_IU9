/**
 * Триангуляция
 * @param {Point} topLeft
 * @param {Point} bottomRight
 * @param {array} nodes
 * @constructor
 */
function Triangulation(topLeft, bottomRight, nodes) {
    this.cache = new DynamicCache(topLeft.X, bottomRight.X, topLeft.Y, bottomRight.Y);

    this.triangles = this.createSuperstructure(topLeft, bottomRight);

    // TODO
    // if (nodes === null) ...

    this.addNodes(nodes);
}

/**
 * Создание суперструктуры, внутри которой будет происходить триангуляция
 * @param {Point} topLeft
 * @param {Point} bottomRight
 */
Triangulation.prototype.createSuperstructure = function(topLeft, bottomRight) {
    // Инициализация двух треугольников
    var left = new Triangle();
    var right = new Triangle();

    var bottomLeft = new Point(topLeft.X, bottomRight.Y);
    var topRight = new Point(bottomRight.X, topLeft.Y);

    // Инициализация ребер суперструктуры
    var diagonal = new Rib(topLeft, bottomRight, left, right);
    var leftRib = new Rib(topLeft, bottomLeft, left, null);
    var rightRib = new Rib(topRight, bottomRight, right, null);
    var topRib = new Rib(topLeft, topRight, right, null);
    var bottomRib = new Rib(bottomLeft, bottomRight, left, null);

    // Определение ребер для треугольников
    left.setRibs(leftRib, bottomRib, diagonal);
    right.setRibs(rightRib, topRib, diagonal);

    // Обновлние струтуры треугольников
    left.update();
    right.update();

    // Добавление треугольников в кэш
    this.cache.initialize(left, right, left, right);

    return [left, right];
};

/**
 * Добавление массива вершин в триангуляуию
 * @param {array<Point>} nodes
 */
Triangulation.prototype.addNodes = function(nodes) {
    for (var i = 0; i < nodes.length; i++) {
        this.addNodee(nodes[i])
    }
};

/**
 * Добавление вершины в триангуляцию
 * @param {Point} node
 */
Triangulation.prototype.addNodee = function (node) {

    var uncheckedTriangles = this.calcTriangulation(node);
    if (uncheckedTriangles == null)
        return;

    // Проверка, что все новые\измененные треугольники удовлетворяют условию Делоне
    while (uncheckedTriangles.length > 0) {
        // Берем первый треугольник из списка
        var triangle = uncheckedTriangles.shift();
        this.checkAndFlip(triangle, uncheckedTriangles);
    }
};

/**
 * Вставка новой точки в триангулцию
 * 1) Нахождение треугольника иди ребра, куда попала новая точка
 * 2) Если точка попадает в эпсилон-окрестность любой другой вершины триангуляции - игнорировать ее
 * 3) Если точка попадает на ребро, то треугольники с этим ребром разбиваются на два новых
 * 4) Если точка попадает внутрь треугольника - треугольник разбивается на три новых
 * @param {Point} node Новая точка
 * @returns {array} Массив новых\измененных треугольников
 */
Triangulation.prototype.calcTriangulation = function (node) {
    // Шаг 1)
    var initTriangle = this.cache.get(node);
    var targetTriangle = this.findTriangleBySeparatingRibs(node, initTriangle);

    // Шаг 2)
    for (i = 0; i < targetTriangle.verticies.length; i++) {
        var vert = targetTriangle.verticies[i];
        if (vert.isInEpsilonAreaPoint(node)) {
            return null;
        }
    }

    // Массив новых и измененных треугольников
    var newAndModifiedTriangles = null;
    var newTriangles = null;

    // Шаг 3)
    var targetTriangleRibs = targetTriangle.ribs;
    for (var i = 0; i < targetTriangleRibs.length; i++) {
        if (isInEpsilonArea(distanceToLine(targetTriangleRibs[i].A, targetTriangleRibs[i].B, node), 0)) {
            var temp = this.putPointOnRib(targetTriangleRibs[i], node, newTriangles);
            newAndModifiedTriangles = temp.newAndMdTr;
            newTriangles = temp.newTr;
            break;
        }
    }

    // Шаг 4)
    if (newAndModifiedTriangles == null) {
        var triangles = this.putPointInTriangle(targetTriangle, node, newTriangles);
        newAndModifiedTriangles = triangles.mdTr;
        newTriangles = triangles.newTr;

    }
    // Увеличение кол-ва вершин в кэше
    this.cache.incrementNodeCount(newTriangles.length);

    // Добавление новых треугольников в кэш
    for (i = 0; i < newTriangles.length; i++) {
        this.cache.update(newTriangles[i]);
    }


    // Return set of new and modified triangles.
    return newAndModifiedTriangles;
};

/**
 * Поиск треугольника, в который попала точка
 * @param {Point} node Новая точка
 * @param {Triangle} T Треугольник из кэша
 * @return {Triangle} Треугольник
 */
Triangulation.prototype.findTriangleBySeparatingRibs = function(node, T) {
    var separatingRib = this.getSeparatingRib(T, node);
    while (separatingRib != null) {
        T = separatingRib.getAdjacent(T);
        separatingRib = this.getSeparatingRib(T, node);
    }
    return T;
};

/**
 * Поиск ребра, которое разделяет точку и любую вершину треугольника
 * @param {Triangle} T
 * @param {Point} targetNode
 * @return {*} null - если точка находится внутри треугольника, иначе возвращает ребро
 */
Triangulation.prototype.getSeparatingRib = function(T, targetNode) {
    for (var i = 0; i < T.ribs.length; i++) {
        var rib = T.ribs[i];

        if (isSeparated(rib.A, rib.B, targetNode, T.getOppositeNode(rib))) {
            return rib;
        }
    }

    return null;
};

/**
 * Попадание точки на ребро
 * @param {Rib} rib
 * @param {Point} node
 * @param {Array<Triangle>} newTriangles
 * @return {Array<Triangle>} Новые и измененные треугольники
 */
Triangulation.prototype.putPointOnRib = function(rib, node, newTriangles) {
    newTriangles = null;
    var modifiedTriangles = null;

    var triangles;
    if (rib.triangles.contains(null)) {
        triangles = this.putPointOnOutsideRib(rib, node, newTriangles);
        newTriangles = triangles.newTr;
        modifiedTriangles = triangles.mdTr;
    } else {
        triangles = this.putPointOnInnerRib(rib, node, newTriangles);
        newTriangles = triangles.newTr;
        modifiedTriangles = triangles.mdTr;
    }
    // Добавление новых треугольников в триангуляцию
    if (newTriangles === null || newTriangles.length === 0) {
        throw new Error("putPointOnRib");
    }
    for (var t in newTriangles) {
        this.triangles.push(t);
    }

    var newAndModifiedTriangles = newTriangles.concat(modifiedTriangles);

    return {
        newAndMdTr: newAndModifiedTriangles,
        newTr: newTriangles
    };
};

/**
 * Попадание узла на границы ребра суперструктуры
 * @param {Rib} rib
 * @param {Point} node
 * @param {Array<Triangle>} newTriangles
 * @return {Array<Triangle>} Измененные треугольники
 */
Triangulation.prototype.putPointOnOutsideRib = function(rib, node, newTriangles) {
    // Треугольники
    var T;
    if (rib.T1 != null) {
        T = rib.T1;
    } else if (rib.T2 != null) {
        T = rib.T2;
    } else {
        throw new Error("putPointOnOutsideRib");
    }

    var NT = new Triangle();

    // Вершины ребра.
    var A = rib.A;
    var B = rib.B;

    // Третья вершина треугольника
    var C = T.getOppositeNode(rib);

    // Ребра, которые нужно обновить
    var BC = T.getOppositeRib(A);

    // Ребро
    var OC = new Rib(node, C, T, NT);
    var OB = new Rib(node, B, NT, null);

    // Обновить ссылки ребра
    BC.update(T, NT);

    // Определить новые ребра треугольника
    NT.setRibs(OC, OB, BC);

    // Обновить ребро существующего треугольника
    T.updateRib(BC, OC);

    // Вершину B изменить на node
    rib.B = node;

    // Обновление структур треугольников
    T.update();
    NT.update();

    newTriangles = [NT];
    var modifiedTriangles = [T];

    return {
        newTr: newTriangles,
        mdTr: modifiedTriangles
    }
};

/**
 * Попадание узла внутрь суперструктуры
 * @param {Rib} rib
 * @param {Point} node
 * @param {Array<Triangle>} newTriangles
 * @return {Array<Triangle>} Измененные треугольники
 */
Triangulation.prototype.putPointOnInnerRib = function(rib, node, newTriangles) {
    // Треугольники
    var LT = rib.T1;
    var RT = rib.T2;
    var NLT = new Triangle();
    var NRT = new Triangle();

    // Вершины ребра
    var A = rib.A;
    var B = rib.B;

    // Третья вершина левого треугольника
    var C = LT.getOppositeNode(rib);

    // Третья вершина правого треугольника
    var D = RT.getOppositeNode(rib);

    // Ребра, которые нужно обновить
    var BC = LT.getOppositeRib(A);
    var BD = RT.getOppositeRib(A);

    // Новые ребра
    var OC = new Rib(node, C, LT, NLT);
    var OD = new Rib(node, D, RT, NRT);
    var OB = new Rib(node, B, NLT, NRT);

    // Обновить ссылки ребер
    BC.update(LT, NLT);
    BD.update(RT, NRT);

    // Определить новые ребра треугольников
    NLT.setRibs(OC, OB, BC);
    NRT.setRibs(OD, OB, BD);

    // Обновить ребра существующего треугольника
    LT.updateRib(BC, OC);
    RT.updateRib(BD, OD);

    // Вершину B изменить на node
    rib.B = node;

    // Обновление структур треугольников
    LT.update();
    RT.update();
    NLT.update();
    NRT.update();

    newTriangles = [NLT, NRT];
    var modifiedTriangles = [LT, RT];

    return {
        newTr: newTriangles,
        modifiedTriangles: modifiedTriangles
    }
};

/**
 * Попадание узла внутрь треугольника
 * @param {Triangle} T
 * @param {Point} node
 * @param {Array<Triangle>} newTriangles
 * @return {Array<Triangle>} Измененные треугольники
 */
Triangulation.prototype.putPointInTriangle = function(T, node, newTriangles){
    // Вершины
    // node == O
    var A = T.verticies[0];
    var B = T.verticies[1];
    var C = T.verticies[2];

    // Треугольники
    var LT = new Triangle();
    var RT = new Triangle();

    newTriangles = [LT, RT];

    // Ребра
    var AB = T.getRib(A, B);
    var BC = T.getRib(B, C);
    var AC = T.getRib(A, C);

    // Новые ребра.
    var OA = new Rib(node, A, LT, T);
    var OB = new Rib(node, B, LT, RT);
    var OC = new Rib(node, C, RT, T);

    // Обновление ссылок на смежные треугольники
    AB.update(T, LT);
    BC.update(T, RT);

    // Обновление старых ребер на новые
    T.updateRib(AB, OA);
    T.updateRib(BC, OC);

    // Определение новых ребер треугольника
    LT.setRibs(AB, OB, OA);
    RT.setRibs(BC, OB, OC);

    // Добавление новых треугольников в триангуляцию
    this.triangles.push(LT);
    this.triangles.push(RT);

    // Обновление структур треугольников
    T.update();
    LT.update();
    RT.update();

    var modifiedTriangles = [T, LT, RT];
    // Return new and modified triangles.
    return {
        newTr: newTriangles,
        mdTr: modifiedTriangles
    }
};

/**
 * Проверка условия Делоне для треугольника и выполнение перестроения, если понадобится
 * @param {Triangle} triangle
 * @param {Array<Triangle>} uncheckedTriangles
 */
Triangulation.prototype.checkAndFlip = function (triangle, uncheckedTriangles) {
    // Треугольник, который не удовлетворяет условию Делоне
    var flip = this.flipRequired(triangle);
    var T = flip.T;
    if (!flip.required) {
        // Если треугольник удовлетворяет условиям Делоне, то убираем его из массива
        removeElementFromArray(uncheckedTriangles, triangle);
        return;
    }

    // Выполняем перестроение
    this.flip(triangle, T);

    if (!containsElementInArray(uncheckedTriangles, T))
        uncheckedTriangles.push(T);
};

/**
 * Выполнение проверки Делоне
 * @param {Triangle} T
 * @return {*}
 */
Triangulation.prototype.flipRequired = function(T) {
    var Flip = null;
    for (var i = 0; i < T.ribs.length; i++) {
        var rib = T.ribs[i];

        if (rib == null)
            continue;

        Flip = rib.getAdjacent(T);

        if (Flip === null)
            continue;

        var node = Flip.getOppositeNode(rib);
        var p1 = rib.A;
        var p2 = T.getOppositeNode(rib);
        var p3 = rib.B;

        if (!this.satisfiesDelaunayCondition(p1, p2, p3, node))
            return {
                required: true,
                T: Flip
            }
    }
    return {
        required: false,
        T: Flip
    }
};

/**
 * Перестроение для треугольников T1 и T2
 * @param {Triangle} T1
 * @param {Triangle} T2
 */
Triangulation.prototype.flip = function(T1, T2) {
    var t1AdjacentRibIndex = T1.getAdjacentRibIndex(T2);
    var adjacentRib = T1.ribs[t1AdjacentRibIndex];
    var t2AdjacentRibIndex = T2.getRibIndex(adjacentRib);

    var A = T1.getVertexIndex(T1.ribs[t1AdjacentRibIndex].A);
    var A2 = T2.getVertexIndex(T1.verticies[A]);
    var B = T1.getVertexIndex(T1.ribs[t1AdjacentRibIndex].B);

    var C = T1.getOppositeNodeIndex(t1AdjacentRibIndex);
    var CPoint = T1.verticies[C];
    var D = T2.getOppositeNodeIndex(t2AdjacentRibIndex);
    var DPoint = T2.verticies[D];

    var t1BC = T1.getRibIndexForPoints(B, C);
    var BC = T1.ribs[t1BC];
    var t2AD = T2.getRibIndexForPoints(A2, D);
    var AD = T2.ribs[t2AD];

    // Новое ребро
    var CD = new Rib(CPoint, DPoint, T1, T2);

    // Обновить у ребер ссылки на треугольники
    BC.update(T1, T2);
    AD.update(T2, T1);

    // Обновить у треугольников ссылки на ребра
    T1.updateRibWithIndex(t1AdjacentRibIndex, CD);
    T2.updateRibWithIndex(t2AdjacentRibIndex, CD);
    T1.updateRibWithIndex(t1BC, AD);
    T2.updateRibWithIndex(t2AD, BC);

    // Обновление структур треугольников
    T1.update();
    T2.update();
};


Triangulation.prototype.satisfiesDelaunayCondition = function(p1, p2, p3, node) {
    var sa, sb;
    var angleSum = this.modifiedCheckOfOppositeAnglesSum(node, p1, p2, p3);
    sa = angleSum.sa;
    sb = angleSum.sb;

    if (sa < 0 && sb < 0) return false;
    if (sa >= 0 && sb >= 0) return true;

    return this.oppositeAnglesSum(node, p1, p2, p3, sa, sb) >= 0;
};

Triangulation.prototype.oppositeAnglesSum = function(p0, p1, p2, p3, sa, sb) {
    return Math.abs( ((p0.X - p1.X) * (p0.Y - p3.Y) - (p0.X - p3.X) * (p0.Y - p1.Y)) )* sb +
        sa * Math.abs( ((p2.X - p1.X) * (p2.Y - p3.Y) - (p2.X - p3.X) * (p2.Y - p1.Y)) );
};

Triangulation.prototype.modifiedCheckOfOppositeAnglesSum = function (p0, p1, p2, p3) {
    return {
        sa: (p0.X - p1.X) * (p0.X - p3.X) + (p0.Y - p1.Y) * (p0.Y - p3.Y),
        sb: (p2.X - p1.X) * (p2.X - p3.X) + (p2.Y - p1.Y) * (p2.Y - p3.Y)
    }
};