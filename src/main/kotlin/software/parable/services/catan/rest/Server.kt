package software.parable.services.catan.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gameDataObjects.board.strategy.CatanBoardLayoutStrategyFirst
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.types.*
import software.parable.services.catan.rest.model.CatanPlayerRoadPieceLocations
import software.parable.services.catan.rest.model.CatanPlayerSettlementPieceLocations
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
            CatanResourceHexagonTileFactory(),
            CatanNumberCirclePieceFactory()
        )
    val board = boardStrategy.strategyImplementation(setOf(CatanColor.BLUE, CatanColor.RED))

    board.placeSettlement(
        CatanCoordinate(1,0),
        CatanGamePiece(CatanColor.BLUE,
            CatanPiece.SETTLEMENT
        )
    )

    board.placeSettlement(
        CatanCoordinate(2,2),
        CatanGamePiece(CatanColor.RED,
            CatanPiece.SETTLEMENT
        )
    )

    board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(3,3))), CatanColor.RED)
    board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(3,3), CatanCoordinate(3,4))), CatanColor.RED)
    board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,2), CatanCoordinate(2,3))), CatanColor.RED)
    board.placeRoad(CatanRoadCoordinates(listOf(CatanCoordinate(2,3), CatanCoordinate(2,4))), CatanColor.RED)

    path("/catan") {
        get("/roads") { req, res ->
            res.type("application/json")
            res.header("Access-Control-Max-Age", "3600")
//            res.header("Access-Control-Allow-Origin", "*")
            res.header("Access-Control-Allow-Methods", "GET")
            jacksonObjectMapper().writeValueAsString(
                    CatanPlayerRoadPieceLocations().translateModel(board.getBoardRoadPieceLocations()
                )
            )
        }
        get("/settlements") { req, res ->
            jacksonObjectMapper().writeValueAsString(
                    CatanPlayerSettlementPieceLocations().translateModel(board.getBoardGamePieceLocations()
                )
            )
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

    before({ request, response -> response.header("Access-Control-Allow-Origin", "http://localhost:3000") })
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object

