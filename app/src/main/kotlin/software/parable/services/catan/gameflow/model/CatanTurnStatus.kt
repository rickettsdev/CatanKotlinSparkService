package software.parable.services.catan.gameflow.model

enum class CatanTurnStatus {
    INITIAL_PLACEMENT_FIRST,
    INITIAL_PLACEMENT_SECOND,
    PLAYER_TURN_IN_PROGRESS,
    PLACE_ROBBER,
    PLAYER_VICTORY,
    ERROR_STATE
}