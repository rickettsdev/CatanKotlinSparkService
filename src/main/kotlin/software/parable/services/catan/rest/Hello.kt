package software.parable.services.catan.rest

import spark.Spark.*
import spark.Request

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gameDataObjects.board.CatanSecondBoard
import gameDataObjects.board.strategy.CatanBoardLayoutStrategyFirst
import gameDataObjects.factory.CatanNumberCirclePieceFactory
import gameDataObjects.factory.CatanResourceHexagonTileFactory
import gameDataObjects.types.CatanColor

fun main(args: Array<String>) {

    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    val userDao = UserDao()
    val boardStrategy =
        CatanBoardLayoutStrategyFirst(
            CatanResourceHexagonTileFactory(),
            CatanNumberCirclePieceFactory()
        )
    val board = boardStrategy.strategyImplementation(setOf(CatanColor.BLUE, CatanColor.RED))

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

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object

