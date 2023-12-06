fun main() {
    fun part1(input: List<String>): Int {
        return  GearRatios().calculate(input)
    }

    fun part2(input: List<String>): Int {
        return GearRatios().calculate2(input)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day03_test")
//    check(part1(testInput) == 8)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

class GearRatios {

    fun calculate(lines: List<String>): Int {
        var sum = 0
        lines.forEachIndexed { index, line ->
            sum += sum(line, index, lines)
        }

        return sum
    }

    fun calculate2(lines: List<String>): Int {
        val (numbers, symbols) = parseInput(lines) { it == '*' }
        return symbols
                .sumOf { symbol ->
                    val neighbors = numbers.filter { it.isAdjacentTo(symbol) }
                    if(neighbors.size == 2) {
                        neighbors.first().toInt() * neighbors.last().toInt()
                    } else 0
                }
    }

    private fun parseInput(
            input: List<String>,
            takeSymbol: (Char) -> Boolean = { it != '.' }
    ): Pair<Set<NumberLocation>, Set<Point2D>> {
        val numbers = mutableSetOf<NumberLocation>()
        val symbols = mutableSetOf<Point2D>()
        var workingNumber = NumberLocation()

        input
                .forEachIndexed { y, row ->
                    row.forEachIndexed { x, c ->
                        if (c.isDigit()) {
                            workingNumber.add(c, Point2D(x, y))
                        } else {
                            if (workingNumber.isNotEmpty()) {
                                numbers.add(workingNumber)
                                workingNumber = NumberLocation()
                            }
                            if(takeSymbol(c)) {
                                symbols.add(Point2D(x, y))
                            }
                        }
                    }
                    // Check at end of row that we don't miss a number.
                    if (workingNumber.isNotEmpty()) {
                        numbers.add(workingNumber)
                        workingNumber = NumberLocation()
                    }
                }
        return numbers to symbols
    }

    private class NumberLocation {
        val number = mutableListOf<Char>()
        val locations = mutableSetOf<Point2D>()

        fun add(c: Char, location: Point2D) {
            number.add(c)
            locations.addAll(location.neighbors())
        }

        fun isNotEmpty() =
                number.isNotEmpty()

        fun isAdjacentToAny(points: Set<Point2D>): Boolean =
                locations.intersect(points).isNotEmpty()

        fun isAdjacentTo(point: Point2D): Boolean =
                point in locations

        fun toInt(): Int =
                number.joinToString("").toInt()
    }

    data class Point2D(val x: Int, val y: Int) {
        fun neighbors(): Set<Point2D> =
                setOf(
                        Point2D(x - 1, y - 1),
                        Point2D(x, y - 1),
                        Point2D(x + 1, y - 1),
                        Point2D(x - 1, y),
                        Point2D(x + 1, y),
                        Point2D(x - 1, y + 1),
                        Point2D(x, y + 1),
                        Point2D(x + 1, y + 1)
                )
    }

    private fun sum(line: String, rowIndex: Int, lines: List<String>): Int {
        var sum = 0
        var number = ""
        var hasAdjacentSymbol = false
        line.forEachIndexed { lineIndex, c ->
            if (c.isDigit()) {
                number += c
                hasAdjacentSymbol = hasAdjacentSymbol || hasAdjacentSymbol(rowIndex, lineIndex, lines)
            } else {
                if (hasAdjacentSymbol) {
                    sum += number.toInt()
                }
                number = ""
                hasAdjacentSymbol = false
            }
        }
        // case where number was at the end of the line:
        if (hasAdjacentSymbol) {
            sum += number.toInt()
        }
        return sum
    }

    private fun hasAdjacentSymbol(rowIndex: Int, lineIndex: Int, lines: List<String>): Boolean {
        var hasAdjacent = false
        if (rowIndex != 0) {
            hasAdjacent = checkAdjacentLine(lineIndex, lines[rowIndex-1])
        }
        hasAdjacent = hasAdjacent || checkCurrentLine(lineIndex, lines[rowIndex])
        if (rowIndex != lines.size - 1) {
            hasAdjacent = hasAdjacent || checkAdjacentLine(lineIndex, lines[rowIndex+1])
        }
        return hasAdjacent
    }

    private fun checkAdjacentLine(lineIndex: Int, line: String): Boolean {
        var hasAdjacent = false

        if (lineIndex != 0) {
            hasAdjacent = isSymbol(line[lineIndex - 1])

        }

        hasAdjacent = hasAdjacent || isSymbol(line[lineIndex])

        if (lineIndex != line.length - 1) {
            hasAdjacent = hasAdjacent || isSymbol(line[lineIndex + 1])
        }

        return hasAdjacent
    }

    private fun isSymbol(c: Char): Boolean {
        return !c.isDigit() && c != '.'
    }

    private fun checkCurrentLine(lineIndex: Int, line: String): Boolean {
        var hasAdjacent = false

        if (lineIndex != 0) {
            hasAdjacent = isSymbol(line[lineIndex - 1])
        }
        if (lineIndex != line.length - 1) {
            hasAdjacent = hasAdjacent || isSymbol(line[lineIndex + 1])
        }
        return hasAdjacent
    }
}
