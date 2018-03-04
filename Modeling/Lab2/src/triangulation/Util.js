/**
 * Проверяет, находится ли одна точка в эпсилон-окрестности другой
 * @return {boolean}
 */
function isInEpsilonArea(x, y) {
    var epsilon = 10e-6;
    return Math.abs(x - y) <= epsilon;
}

/**
 * Вычисляет векторное произведение
 * @return {number}
 */
function crossProductZ(aStart, aEnd, bStart, bEnd) {
    return (aEnd.X - aStart.X) * (bEnd.Y - bStart.Y) - (aEnd.Y - aStart.Y) * (bEnd.X - bStart.X);
}

/**
 * Проверяет направление точек
 * @param {Point} A
 * @param {Point} B
 * @param {Point} C
 * @return {boolean} True - по часовой стрелке
 */
function isClockWiseOrdered(A, B, C) {
    return this.crossProductZ(C, A, C, B) < 0;
}

/**
 * Определяет взаимное расположение точек относительно линии
 * @param {Point} O Точка линин OA
 * @param {Point} A Точка линии OA
 * @param {Point} X
 * @param {Point} Y
 * @return {boolean} True - если точки X, Y с одной стороны относительно линии OA
 */
function isSeparated(O, A, X, Y) {
    // Use sign of pseudoscalar vector product - it defines half-plane.
    // If sign of OA ^ OX is same as sign of OA ^ OY - points X & Y lays on same half-plane, otherwise not.
    var OA = new Vector(O, A);
    var oxSign = Math.sign(this.pseudoscalarVectorProduct(OA, new Vector(O, X)));
    var oySign = Math.sign(this.pseudoscalarVectorProduct(OA, new Vector(O, Y)));
    // If sign is equal to 0 then one of points lays on the line, therefore points are not separated.
    if (oxSign === 0 || oySign === 0)
        return false;
    return oxSign !== oySign;
}

/**
 * Вычисляет pseudoscalar векторное произведение
 * @param {Vector} a
 * @param {Vector} b
 * @return {number}
 */
function pseudoscalarVectorProduct(a, b) {
    return a.X * b.Y - a.Y * b.X
}

/**
 * Вычисляет дистанцию от точки P до линии OX
 * @param {Point} O
 * @param {Point} X
 * @param {Point} P
 * @return {number}
 * @constructor
 */
function distanceToLine(O, X, P) {
    var A = O.Y - X.Y;
    var B = X.X - O.X;
    var C = O.X * X.Y - X.X * O.Y;

    return Math.abs((A * P.X + B * P.Y + C) / Math.sqrt(A * A + B * B));
}

/**
 * Вычисляет равенство двух объектов
 * @param x
 * @param y
 * @return {boolean}
 */
function equals(x, y) {
    // return JSON.stringify(obj1) === JSON.stringify(obj2);
    if ( x === y ) return true;
    // if both x and y are null or undefined and exactly the same

    if ( ! ( x instanceof Object ) || ! ( y instanceof Object ) ) return false;
    // if they are not strictly equal, they both need to be Objects

    if ( x.constructor !== y.constructor ) return false;
    // they must have the exact same prototype chain, the closest we can do is
    // test there constructor.

    for ( var p in x ) {
        if ( ! x.hasOwnProperty( p ) ) continue;
        // other properties were tested using x.constructor === y.constructor

        if ( ! y.hasOwnProperty( p ) ) return false;
        // allows to compare x[ p ] and y[ p ] when set to undefined

        if ( x[ p ] === y[ p ] ) continue;
        // if they have the same strict value or identity then they are equal

        if ( typeof( x[ p ] ) !== "object" ) return false;
        // Numbers, Strings, Functions, Booleans must be strictly equal

        if ( ! this.equals( x[ p ],  y[ p ] ) ) return false;
        // Objects and Arrays must be tested recursively
    }

    for ( p in y ) {
        if ( y.hasOwnProperty( p ) && ! x.hasOwnProperty( p ) ) return false;
        // allows x[ p ] to be set to undefined
    }
    return true;
}

function isLeftHanded(A, O, B) {
    return this.crossProductZ(O, B, O, A) < 0;
}

function createThreePoint(point) {
    return new THREE.Vector3(point.X, point.Y, point.Z);
}

function copyPoint(point) {
    return new THREE.Vector3(point.x, point.y, point.z);
}

function getElement(array, index) {
    return array.slice(index)[0]
}

function removeElementFromArray(arr, item) {
    for (var i = arr.length; i--;) {
        if(equals(arr[i], item)) {
            arr.splice(i, 1);
        }
    }
}

function containsElementInArray(arr, item) {
    return arr.indexOf(item) !== -1;

}

function angleBetweenVectors(vectorA, vectorB) {
    // 2-D
    var scalar = vectorA.x * vectorB.x + vectorA.y * vectorB.y;
    return scalar / (vectorA.length() * vectorB.length());
}

