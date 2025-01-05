package gameDataObjects.factory

class CatanNumberCirclePieceFactory(randomize: Boolean = false) {
    private val availableNumberCircles: List<Int> = listOf(
            2,3,3,
           4,4,5,5,
          6,6,8,8,9,
          9,10,10,11,
           11,12 /*DESERT*/)//.shuffled().toMutableList()

    private val inUseNumberCircles =
        if(randomize) availableNumberCircles.shuffled().toMutableList()
    else availableNumberCircles.toMutableList()

    fun getNumberTile(): Int {
        println("circle size: ${inUseNumberCircles.size}")
        return inUseNumberCircles.removeAt(0)
    }
}