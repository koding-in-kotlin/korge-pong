import com.soywiz.korio.lang.ASCII
import com.soywiz.korio.lang.toByteArray
import com.soywiz.korio.util.toStringDecimal
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Vector2D

data class GameState(
    private val left: Double,  // vertical position of left paddle
    private val right: Double, // vertical position of right paddle
    private val ballPos: Point,
    private val ballVelocity: Vector2D,
) {
    fun toMessage(): ByteArray {
        fun Double.toPaddedString() = toStringDecimal(2).padStart(8)
        fun Point.toByteArray() =
            x.toPaddedString().toByteArray(charset = ASCII) + y.toPaddedString().toByteArray(charset = ASCII)

        fun Double.toByteArray() = toPaddedString().toByteArray(charset = ASCII)
        return byteArrayOf(
            *left.toByteArray(),
            *right.toByteArray(),
            *ballPos.toByteArray(),
            *ballVelocity.toByteArray()
        )
    }
}
