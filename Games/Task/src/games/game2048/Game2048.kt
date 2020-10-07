package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {

    val initCell = initializer.nextValue(this)

    initCell
            .takeUnless { pair -> pair == null  }
            ?.let{ this[it.first] = it.second}
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {

    val listOfValuesToMerge:List<Int?> = rowOrColumn
            .map { this[it] }
            .toList()
            .moveAndMergeEqual { it * 2 }

    return when (listOfValuesToMerge) {
        listOfValuesToMerge -> {

            var didBoardChange = false

            for ((index, cell ) in rowOrColumn.withIndex()) {

                this[cell] = if (index < listOfValuesToMerge.count() && this[cell] == listOfValuesToMerge[index]) {
                    this[cell]
                }else if (index < listOfValuesToMerge.count() && this[cell] != listOfValuesToMerge[index]) {
                    didBoardChange = true
                    listOfValuesToMerge[index]
                }else {
                    null
                }
            }

            didBoardChange
        }
        else -> false
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {

    var didMove: Boolean

    didMove = when(direction) {

        Direction.RIGHT -> {
            var moveRight = mutableListOf<Boolean>()
            for (row in 1 .. width) {
                moveRight.add(moveValuesInRowOrColumn(getRow(row , width downTo  1)))
            }
            moveRight.any { it == true }
        }

        Direction.LEFT -> {
            var moveLeft = mutableListOf<Boolean>()
            for (row in 1 .. width) {
                moveLeft.add(moveValuesInRowOrColumn(getRow(row , 1 .. width)))
            }
            moveLeft.any { it == true }
        }

        Direction.UP -> {
            var moveUp = mutableListOf<Boolean>()
            for (column in 1 .. width) {
                moveUp.add(moveValuesInRowOrColumn(getColumn(1 .. width, column)))
            }
            moveUp.any { it == true }
        }

        Direction.DOWN -> {
            var moveDown = mutableListOf<Boolean>()
            for (column in 1 .. width) {
                moveDown.add(moveValuesInRowOrColumn(getColumn(width downTo  1, column)))
            }
            moveDown.any { it == true }
        }

    }

    return didMove
}