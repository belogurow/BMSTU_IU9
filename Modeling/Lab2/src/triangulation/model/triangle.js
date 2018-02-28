function isPointInEdge(edgePoint1, edgePoint2, point) {
    return (point.x - edgePoint1.x) / (edgePoint2.x - edgePoint1.x) === (point.y - edgePoint1.y) / (edgePoint2.y - edgePoint1.y)
}