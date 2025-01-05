package gameDataObjects.board.theme

import gameDataObjects.types.CatanColor

class CatanThemedPrinter {
    companion object {
        private val ANSI_RESET = "\u001B[0m";
        private val ANSI_RED = "\u001B[31m";
        private val ANSI_GREEN = "\u001B[32m";
        private val ANSI_YELLOW = "\u001B[33m";
        private val ANSI_BLUE = "\u001B[34m";
        private val ANSI_WHITE = "\u001B[37m";
        private val ANSI_CYAN = "\u001B[36m"
    }

    fun printTheme(message: String, player: CatanColor): String {
        val ansiColor = when (player) {
            CatanColor.BLUE -> ANSI_BLUE
            CatanColor.RED -> ANSI_RED
            CatanColor.WHITE -> ANSI_WHITE
            CatanColor.YELLOW -> ANSI_YELLOW
            CatanColor.UNASSIGNED -> ANSI_GREEN
            CatanColor.ERROR -> ANSI_CYAN
        }
        return "$ansiColor$message$ANSI_RESET"
    }
}