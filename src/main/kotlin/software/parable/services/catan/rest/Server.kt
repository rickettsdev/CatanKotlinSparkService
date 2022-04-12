package software.parable.services.catan.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gameDataObjects.board.strategy.CatanBoardLayoutStrategyFirst
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.types.*
import software.parable.services.catan.gameflow.CatanTurnManager
import software.parable.services.catan.rest.model.*
import spark.Request
import spark.Spark.*

fun main(args: Array<String>) {

    handleCORS()

    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    val BLUE_PLAYER = CatanPlayer(CatanColor.BLUE)
    val RED_PLAYER = CatanPlayer(CatanColor.RED)

    val boardStrategy =
        CatanBoardLayoutStrategyFirst(
            CatanResourceHexagonTileFactory(false),
            CatanNumberCirclePieceFactory(false)
        )
    val turnManager = CatanTurnManager
    val playerOrdering = setOf(CatanColor.RED, CatanColor.BLUE, CatanColor.YELLOW)
    val board = boardStrategy.strategyImplementation(playerOrdering)

    turnManager.startGame(playerOrdering)
    path("/catan") {

        get("/status") { req, res ->
            println("Status")
            jacksonObjectMapper().writeValueAsString(
                CatanSessionStatusResponse().translateModel(
                    turnManager.turnState
                )
            )
        }

        get("/roads") { req, res ->
            println("Roads")
            jacksonObjectMapper().writeValueAsString(
                    CatanPlayerRoadPieceLocations().translateModel(
                        board.getBoardRoadPieceLocations()
                )
            )
        }
        get("/settlements") { req, res ->
            println("Settlements")
            jacksonObjectMapper().writeValueAsString(
                    CatanPlayerSettlementPieceLocations().translateModel(
                        board.getBoardGamePieceLocations()
                )
            )
        }

        get("/resourceTiles") { req, res ->
            println("ResourceTiles")
            jacksonObjectMapper().writeValueAsString(
                CatanPlayerResourceTileLocations().translateModel(
                    board.getResourceTileInfo()
                )
            )
        }

        get("/playerResources") { req, res ->
            println("Player Resources")
            jacksonObjectMapper().writeValueAsString(
                CatanPlayerResourceCardsResponse().translateModel(
                    board.getPlayerResourceCards()
                )
            )
        }

        get("/rollDice") { req, res ->
            println("RollingDice")
            val diceRoll = (2..12).random()
            val resourceAllocations = board.numberRolled(diceRoll)
            turnManager.incrementDiceRollsSoFar()

            // TODO: handle tallying up used resources.

            jacksonObjectMapper().writeValueAsString(
                CatanDiceRollResponse().translateModel(
                    diceRoll
                )
            )
        }

        post("/addRoad") {request, response ->
            val x = request.headers("x").toInt()
            val y = request.headers("y").toInt()
            val x1 = request.headers("x1").toInt()
            val y1 = request.headers("y1").toInt()
            val color = CatanColor.valueOf(request.headers("color"))
            println("Placing road at x: ${x}, y: ${y}, x1: ${x1}, y1: ${y1}, color: ${color.name}")
            board.placeRoad(
                CatanRoadCoordinates(
                    listOf(CatanCoordinate(x,y), CatanCoordinate(x1,y1)
                    )
                ),
                color
            )
            turnManager.incrementRoad()
            response.status(200)
            "ok"
        }
        post("/addSettlement") {request, response ->
            val x = request.headers("x").toInt()
            val y = request.headers("y").toInt()
            val color = CatanColor.valueOf(request.headers("color"))
            println("Placing settlement at x: ${x}, y: ${y}, color: ${color.name}")
            board.placeSettlement(CatanCoordinate(x, y), CatanGamePiece(color, CatanPiece.SETTLEMENT))
            turnManager.incrementSettlement()
            response.status(200)
            "ok"
        }
        post("/resetTurnManager") {request, response ->
            turnManager.startGame(playerOrdering)
            response.status(200)
            "ok"
        }

        post("/endTurn") {request, response ->
            val color = CatanColor.valueOf(request.headers("color"))
            if (color != turnManager.turnState.playerTurn) {
                throw Exception("Not your turn.")
            }
            turnManager.endTurn()
            response.status(200)
            "ok"
        }
    }
}

private fun handleCORS() {
    options(
        "/*"
    ) { request, response ->
        val accessControlRequestHeaders = request
            .headers("Access-Control-Request-Headers")
        if (accessControlRequestHeaders != null) {
            response.header(
                "Access-Control-Allow-Headers",
                accessControlRequestHeaders
            )
        }
        val accessControlRequestMethod = request
            .headers("Access-Control-Request-Method")
        if (accessControlRequestMethod != null) {
            response.header(
                "Access-Control-Allow-Methods",
                accessControlRequestMethod
            )
        }
        "OK"
    }

    before({ request, response -> response.header("Access-Control-Allow-Origin", "*") })
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object

