import com.soywiz.korma.geom.IPoint

data class Vector2(
    override var x: Double,
    override var y: Double,
) : IPoint {
    val length: Double
        get() = kotlin.math.sqrt(length2)

    val length2: Double
        get() = x * x + y * y

    fun normalize(): Vector2 {
        return Vector2(x / length, y / length)
    }

    operator fun times(other: Double): Vector2 {
        return Vector2(x * other, y * other)
    }

    operator fun plus(other: Vector2): Vector2 {
        return Vector2(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2): Vector2 {
        return Vector2(x - other.x, y - other.y)
    }

    operator fun Vector2.unaryMinus(): Vector2 {
        return Vector2(-x, -y)
    }

    fun dot(other: Vector2): Double {
        return x * other.x + y * other.y
    }
}
