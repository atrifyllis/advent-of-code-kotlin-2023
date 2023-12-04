fun main() {
    fun part1(input: List<String>): Int {
        return  CubeGame().calculate(input)
    }

    fun part2(input: List<String>): Int {
        return CubeGame().calculate2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class CubeGame {
    companion object {
        val TOTAL_CUBES: Map<String, Int> = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
    }

    fun calculate(lines: List<String>): Int {
        return lines.sumOf { calculateGame(it) }
    }

    private fun calculateGame(line: String): Int {
        val (gameIdString, roundsString) = line.split(":")
        val gameId = gameIdString.substringAfter(' ').toInt()
        val rounds: List<String> = roundsString.split(';')
        return if (rounds.all { calculateRound(it) }) gameId else 0
    }

    private fun calculateRound(round: String): Boolean {
        val balls: List<String> = round.split(',')
        return balls.all { calculateBall(it.trim()) }
    }

    private fun calculateBall(ballPair: String): Boolean {
        val (totalBalls, ballColor) = ballPair.split(' ')
        return TOTAL_CUBES[ballColor]!! >= totalBalls.toInt()
    }

    fun calculate2(lines: List<String>): Int {
        return lines.sumOf { calculateGame2(it) }
    }

    private fun calculateGame2(game: String): Int {
        val (_, roundsString) = game.split(":")
        val rounds: List<String> = roundsString.split(';')
        val flatTotals = rounds.flatMap { it.split(',') }
        val (red, green, blue) = calculateMaxColors(flatTotals)
        return red * green * blue
    }

    private fun calculateMaxColors(rounds: List<String>): Triple<Int, Int, Int> {
        var red = 1
        var green = 1
        var blue = 1
        rounds.forEach {
            val (totalBalls, ballColor) = it.trim().split(' ')
            when (ballColor) {
                "red" -> if (red < totalBalls.toInt()) red = totalBalls.toInt()
                "green" -> if (green < totalBalls.toInt()) green = totalBalls.toInt()
                "blue" -> if (blue < totalBalls.toInt()) blue = totalBalls.toInt()

            }
        }
        return Triple(red, green, blue)
    }

}
