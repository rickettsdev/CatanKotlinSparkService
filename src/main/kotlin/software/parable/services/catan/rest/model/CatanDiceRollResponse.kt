package software.parable.services.catan.rest.model

class CatanDiceRollResponse: CatanJacksonModelResponse<Int, String, Int>() {
    override fun createJacksonFriendlyModel(model: Int): HashMap<String, Int> {
        return hashMapOf(
            "diceRoll" to model
        )
    }
}