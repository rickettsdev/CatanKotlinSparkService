package software.parable.services.catan.rest.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class CatanJacksonModelResponse<T, K, V> {
    fun translateModel(model: T): String {
        val jacksonFriendlyModel = createJacksonFriendlyModel(model)
        return jacksonObjectMapper().writeValueAsString(jacksonFriendlyModel)
    }
    protected abstract fun createJacksonFriendlyModel(model: T): HashMap<K, V>
}