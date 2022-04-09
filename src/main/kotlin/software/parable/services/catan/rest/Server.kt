package software.parable.services.catan.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gameDataObjects.board.strategy.CatanBoardLayoutStrategyFirst
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.types.*
import software.parable.services.catan.rest.model.*
import spark.Request
import spark.Spark.*

fun main(args: Array<String>) {

    handleCORS()

    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    val BLUE_PLAYER = CatanPlayer(CatanColor.BLUE)
    val RED_PLAYER = CatanPlayer(CatanColor.RED)

    val userDao = UserDao()
    val boardStrategy =
        CatanBoardLayoutStrategyFirst(
            CatanResourceHexagonTileFactory(false),
            CatanNumberCirclePieceFactory(false)
        )
    val board = boardStrategy.strategyImplementation(setOf(CatanColor.BLUE, CatanColor.RED))

    board.placeSettlement(
        CatanCoordinate(0,0),
        CatanGamePiece(CatanColor.BLUE,
            CatanPiece.SETTLEMENT
        )
    )

    board.placeSettlement(
        CatanCoordinate(1,3),
        CatanGamePiece(CatanColor.RED,
            CatanPiece.SETTLEMENT
        )
    )

    board.numberRolled(4)

    path("/catan") {
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
            board.numberRolled(diceRoll)
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
            response.status(200)
            "ok"
        }
        post("/addSettlement") {request, response ->
            val x = request.headers("x").toInt()
            val y = request.headers("y").toInt()
            val color = CatanColor.valueOf(request.headers("color"))
            println("Placing settlement at x: ${x}, y: ${y}, color: ${color.name}")
            board.placeSettlement(CatanCoordinate(x, y), CatanGamePiece(color, CatanPiece.SETTLEMENT))
            response.status(200)
            "ok"
        }
    }

    path("/users") {

        get("") { req, res ->
            jacksonObjectMapper().writeValueAsString(userDao.users)
        }

        get("/:id") { req, res ->
            userDao.findById(req.params("id").toInt())
        }

        get("/email/:email") { req, res ->
            userDao.findByEmail(req.params("email"))
        }

        post("/create") { req, res ->
            userDao.save(name = req.qp("name"), email = req.qp("email"))
            res.status(201)
            "ok"
        }

        patch("/update/:id") { req, res ->
            userDao.update(
                id = req.params("id").toInt(),
                name = req.qp("name"),
                email = req.qp("email")
            )
            "ok"
        }

        delete("/delete/:id") { req, res ->
            userDao.delete(req.params("id").toInt())
            "ok"
        }

    }

    userDao.users.forEach(::println)

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

