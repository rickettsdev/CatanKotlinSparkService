package software.parable.services.catan.gameflow.model

enum class CatanTurnStatus {
    INITIAL_PLACEMENT_FIRST,
    INITIAL_PLACEMENT_SECOND,
    PLACE_ROBBER,
    OVER_7_DISCARD,
    PLAYER_VICTORY
}