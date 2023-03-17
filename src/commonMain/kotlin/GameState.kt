import com.soywiz.korio.lang.ASCII
import com.soywiz.korio.lang.toByteArray
import com.soywiz.korio.util.toStringDecimal
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Vector2D

data class GameState(
    val left: Double,  // vertical position of left paddle
    val right: Double, // vertical position of right paddle
    val ballPos: Point,
    val ballVelocity: Vector2D,
    val leftScore: Int,
    val rightScore: Int
) {
    fun toMessage(): ByteArray {
        fun Double.toPaddedString() = toStringDecimal(1).padStart(8)
        fun Point.toByteArray() =
            x.toPaddedString().toByteArray(charset = ASCII) + y.toPaddedString().toByteArray(charset = ASCII)

        fun Double.toByteArray() = toPaddedString().toByteArray(charset = ASCII)
        return byteArrayOf(
            *left.toByteArray(),
            *right.toByteArray(),
            *ballPos.toByteArray(),
            *ballVelocity.toByteArray(),
            //  I'm sorry
            *leftScore.toDouble().toByteArray(),
            *rightScore.toDouble().toByteArray(),
        )
    }
}
