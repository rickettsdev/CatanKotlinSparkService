package gameDataObjects.factory

class CatanNumberCirclePieceFactory {
    private var availableNumberCircles: MutableList<Int> = mutableListOf(
            2,3,3,
           4,4,5,5,
          6,6,8,8,9,
          9,10,10,11,
           11,12 /*DESERT*/).shuffled().toMutableList()

    fun getNumberTile(): Int {
        println("circle size: ${availableNumberCircles.size}")
        return availableNumberCircles.removeAt(0)
    }
}