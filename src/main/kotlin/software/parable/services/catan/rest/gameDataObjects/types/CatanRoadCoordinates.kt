package gameDataObjects.types

data class CatanRoadCoordinates(val roadCoordinates: List<CatanCoordinate>) {
    fun connectsTo(otherRoadCoordinates: CatanRoadCoordinates): Boolean {
        return roadCoordinates.contains(otherRoadCoordinates.roadCoordinates.first()) ||
                roadCoordinates.contains(otherRoadCoordinates.roadCoordinates.last())
    }

    fun intersectingCoordinate(otherRoadCoordinate: CatanRoadCoordinates): CatanCoordinate? {
        return if(roadCoordinates.first() == otherRoadCoordinate.roadCoordinates.first() || roadCoordinates.first() == otherRoadCoordinate.roadCoordinates.last()) {
            roadCoordinates.first()
        } else if (roadCoordinates.last() == otherRoadCoordinate.roadCoordinates.first() || roadCoordinates.last() == otherRoadCoordinate.roadCoordinates.last()) {
            roadCoordinates.last()
        } else {
            null
        }
    }
}