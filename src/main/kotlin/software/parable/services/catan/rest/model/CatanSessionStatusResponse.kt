package software.parable.services.catan.rest.model

import software.parable.services.catan.gameflow.model.CatanBoardTurnState

class CatanSessionStatusResponse: CatanJacksonModelResponse<CatanBoardTurnState, String, String>() {
    override fun createJacksonFriendlyModel(model: CatanBoardTurnState): HashMap<String, String> {
        return hashMapOf(
            "status" to model.status.toString(),
            "playerTurn" to model.playerTurn.toString(),
            "roadsPlaced" to model.roadsPlaced.toString(),
            "settlementsPlaced" to model.settlementsPlaced.toString(),
            "diceRollsSoFar" to model.diceRollsSoFar.toString()
        )
    }
}