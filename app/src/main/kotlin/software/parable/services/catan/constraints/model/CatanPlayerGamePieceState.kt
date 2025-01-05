package software.parable.services.catan.constraints.model

data class CatanPlayerGamePieceState(
        val maxRoadPieceCount: Int = 15,
        val maxCityPieceCount: Int = 4,
        val maxSettlementPieceCount: Int = 5) {
    var roadPieceCount: Int = maxRoadPieceCount
    var cityPieceCount: Int = maxCityPieceCount
    var settlementPieceCount: Int = maxSettlementPieceCount
}
