package software.parable.services.catan.rest.model

import gameDataObjects.types.CatanColor
import gameDataObjects.types.CatanResource

class CatanPlayerResourceCardsResponse: CatanJacksonModelResponse<HashMap<CatanColor, MutableList<CatanResource>>, String, List<String>>() {
    override fun createJacksonFriendlyModel(model: HashMap<CatanColor, MutableList<CatanResource>>): HashMap<String, List<String>> {
        val redModelList = model[CatanColor.RED]?.map { catanResource -> catanResource.toString() } ?: listOf()
        val blueModelList = model[CatanColor.BLUE]?.map { catanResource -> catanResource.toString() } ?: listOf()
        val yellowModelList =  model[CatanColor.YELLOW]?.map { catanResource -> catanResource.toString() } ?: listOf()
        val whiteModelList = model[CatanColor.WHITE]?.map { catanResource -> catanResource.toString() } ?: listOf()

        return hashMapOf(
            "red" to redModelList,
            "blue" to blueModelList,
            "yellow" to yellowModelList,
            "white" to whiteModelList
        )
    }
}