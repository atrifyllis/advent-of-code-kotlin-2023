fun main() {
    fun part1(input: List<String>): Int {
        return Scratchcards().calculate(input)
    }

    fun part2(input: List<String>): Int {
        return Scratchcards().calculate2(input)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day04_test")
//    check(part1(testInput) == 8)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

class Scratchcards {
    fun calculate(lines: List<String>): Int {
        return parse(lines)
                .sumOf {
                    calculateWinningPoints(it)
                }
    }

    fun calculate2(lines: List<String>): Int {
        val cards: List<Card> = parse(lines)
        val cardsWon: MutableMap<Int, Int> = cards.associate { card ->
            card.id to card.cardsWon
        }.toMutableMap()
        cards.forEach {
            val occurrences = calculateOccurrences(it)
            if (occurrences != 0) {
                for (i in 1..occurrences) {
                    cardsWon[it.id + i] = cardsWon[it.id + i]!! + cardsWon[it.id]!! // tricky part to understand that we add the current cards number
                }
            }
        }
        return cardsWon.values.sum()
    }


    private fun calculateWinningPoints(card: Card): Int {
        val occurrences = calculateOccurrences(card)
        if (occurrences == 0) {
            return 0
        }
        return (1..occurrences).reduce { acc, _ -> acc * 2 }
    }

    private fun calculateOccurrences(card: Card) = card.ownNumbers.count { it in card.winningNumbers }

    fun parse(lines: List<String>): List<Card> {
        return lines
                .map { it.substringAfter("Card ") }
                .map {
                    Card(
                            extractId(it),
                            extractWinningNumbers(it.substringAfter(": ")),
                            extractOwnNumbers(it.substringAfter(": "))
                    )
                }
    }

    private fun extractId(card: String): Int {
        return card.substringBefore(":").trim().toInt()
    }

    private fun extractWinningNumbers(card: String): List<Int> {
        return card.substringBefore(" |")
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
    }

    private fun extractOwnNumbers(card: String): List<Int> {
        return card.substringAfter("| ")
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
    }

}

data class Card(
        val id: Int,
        val winningNumbers: List<Int>,
        val ownNumbers: List<Int>,
        val cardsWon: Int = 1
)
