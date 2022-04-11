package software.parable.services.catan.constraints.model

data class CatanResourceDeckState(val maxCardCount: Int = 19) {
    var brickCount = maxCardCount
    var stoneCount = maxCardCount
    var woodCount = maxCardCount
    var wheatCount = maxCardCount
    var sheepCount = maxCardCount
}
