// Нахождение точек для выпуклой оболочки
function findConvexHull(points_) {
    var points = points_.slice(0);

    // Находим точку с максимальым значением по оси ординат
    var firstPointIndex = findTopPointIndex(points);
    var firstPoint = points[firstPointIndex];
    var tempFirstPoint = copyPoint(firstPoint);
    tempFirstPoint.x -= 5;

    // создаем массив с точками выпуклой области
    var hullArray = [firstPoint];
    var indexMinAngle = -1;
    // points.splice(firstPointIndex, 1);

    while (hullArray.length === 1 || getElement(hullArray, -1) !== firstPoint) {
        var angle = Number.NEGATIVE_INFINITY;
        if (hullArray.length === 1) {
            for (var i = 0; i < points.length; i++) {
                var newAngle = angleBetweenVectors(new Vector(firstPoint, tempFirstPoint),
                    new Vector(firstPoint, points[i]));

                if (newAngle > angle) {
                    angle = newAngle;
                    indexMinAngle = i;
                }
            }
        } else {
            for (i = 0; i < points.length; i++) {
                newAngle = angleBetweenVectors(new Vector(getElement(hullArray, -2), getElement(hullArray, -1)),
                    new Vector(getElement(hullArray, -1), points[i]));

                if (newAngle > angle) {
                    angle = newAngle;
                    indexMinAngle = i;
                }
            }
        }
        hullArray.push(points[indexMinAngle]);
        points.splice(indexMinAngle, 1);
    }


    return hullArray;
}

function findTopPointIndex(points) {
    var resultIndex = 0;

    for (var i = 1; i < points.length; i++) {
        if (points[i].y > points[resultIndex].y ||
            (points[i].y === points[resultIndex].y && points[i].x < points[resultIndex].x)) {
            resultIndex = i;
        }

    }
    return resultIndex;
}