package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {

    val permutations = permutation.toMutableList()
    val desiredOrdering = permutations.sorted().toList()

    var swaps = 0

    //search the range
    for (i in desiredOrdering.indices) {

        //determine if the given element of the permutation needs to be swapped
        for (j in permutations.indices){

            //lets swap
            if(permutations[j] == desiredOrdering[i] && j != i ){

                permutations[j] = permutations[i]
                permutations[i] = desiredOrdering[i]
                swaps++
                break;
            }
        }
    }

    return swaps % 2 == 0
}