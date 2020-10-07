package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.game2048.Game2048
import games.game2048.Game2048Initializer
import games.game2048.addNewValue
import games.game2048.moveValues

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val list = initializer.initialPermutation

        var y = 0
        for (cell in board.getAllCells() ) {

            if(y >= 15) {
                continue;
            }
           board[cell] = list[y++]
        }
    }

    override fun canMove():Boolean = true

    override fun hasWon():Boolean {

        var sorted = true
        with(board) {
            val cells = getAllCells()

            var currentLowestValue:Int? = null

            for (cell in cells) {
                if(currentLowestValue == null){
                    currentLowestValue = get(cell)
                    continue
                }

                val comparison = get(cell)?.compareTo(currentLowestValue)
                if (comparison !== null){
                    sorted = when (comparison){
                        1 -> {
                            currentLowestValue = get(cell)
                            true
                        }
                        -1 -> false
                        else -> true
                    }
                }

            }
        }

        return sorted
    }

    override fun processMove(direction: Direction) {

        with(board) {

            val emptyCell = board.find { value -> value == null } as Cell

            val neighbor = emptyCell.getNeighbour(direction.reversed()) as Cell
            this[emptyCell] = this[neighbor]
            this[neighbor] = null

        }

    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

