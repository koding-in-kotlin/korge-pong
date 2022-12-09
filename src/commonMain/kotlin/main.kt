import com.soywiz.klock.timesPerSecond
import com.soywiz.klogger.Console
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.input.Input
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import kotlin.collections.set
import kotlin.random.Random

suspend fun main() = Korge(title = "Ideal gas here we go", width = 768, height = 768, bgcolor = Colors["#003049"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ GasBox() })
}

const val DONT_GO_SLOWER_THAN = 1.0
const val SPEED_DELTA = 2
val MY_COLORS = listOf(
    Colors.HOTPINK,
    Colors.HOTPINK,
    Colors.HOTPINK,
    Colors["#F77F00"],
    Colors.AQUA,
    Colors.DARKRED,
    Colors.DARKKHAKI,
)

val CIRCLE_TO_PARTICLE = hashMapOf<Circle, GasParticle>()

data class Vector2(
    var x: Double,
    var y: Double,
) {
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

operator fun Double.times(other: Vector2): Vector2 {
    return other * this
}

class GasParticle(
    scene: Scene,
    var velo: Vector2,
    var mass: Double,
    defaultX: Double? = null,
    defaultY: Double? = null
) {
    val sc = scene.sceneContainer

    val circle = sc.circle(mass, stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = MY_COLORS.random())

    val energy get() = mass * velo.length2 / 2.0

    fun die() {
        sc.removeChild(circle)
        CIRCLE_TO_PARTICLE.remove(circle)
    }

    init {
        circle.x = defaultX ?: Random.nextDouble(circle.radius, sc.width - circle.radius)
        circle.y = defaultY ?: Random.nextDouble(circle.radius, sc.height - circle.radius)
        circle.anchor(.5, .5)

        registerUpdaters(scene.input, sc.height, sc.width)
        CIRCLE_TO_PARTICLE[circle] = this

        circle.onCollision(filter = { it is Circle }, kind = CollisionKind.SHAPE) {
            handleCollisions(it)
        }
    }

    private fun View.handleCollisions(it: View) {
        // see https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional_collision_with_two_moving_objects
        val me = CIRCLE_TO_PARTICLE[this]!!
        val other = CIRCLE_TO_PARTICLE[it]!!
        val distance = kotlin.math.hypot(x - it.x, y - it.y)
        val neededDistance = me.circle.radius + other.circle.radius
        if (distance < neededDistance) {
            val (dx, dy) = Vector2(it.x - x, it.y - y).normalize() * (neededDistance - distance)
            it.x += dx
            it.y += dy
        }
        val u1 = me.velo
        val u2 = other.velo
        val x1 = Vector2(this.x, this.y)
        val x2 = Vector2(it.x, it.y)

        val mu1 = 2 * other.mass / (me.mass + other.mass)
        val mu2 = 2 * me.mass / (me.mass + other.mass)
        val f1 = (u1 - u2).dot(x1 - x2) / (x1 - x2).length2
        val f2 = (u2 - u1).dot(x2 - x1) / (x2 - x1).length2

        me.velo = u1 - mu1 * f1 * (x1 - x2)
        other.velo = u2 - mu2 * f2 * (x2 - x1)
    }

    fun registerUpdaters(input: Input, maxHeight: Double, maxWidth: Double, addMouse: Boolean = false) {
        inputHandler(input)
        positionUpdater(maxHeight, maxWidth)
        if (addMouse) mouseXUpdater(input)
        wallDestructingUpdater()
    }

    private fun wallDestructingUpdater() {
        circle.addUpdater {
        }
    }

    private fun positionUpdater(maxHeight: Double, maxWidth: Double) {
        circle.addFixedUpdater(60.timesPerSecond) {
            var change = false
            x += velo.x

            if ((x + radius) > maxWidth) {
                x = maxWidth - radius
                velo.x = -velo.x
                change = true
            } else if (x < radius) {
                x = radius
                velo.x = -velo.x
                change = true
            }

            y += velo.y
            if ((y + radius) > maxHeight) {
                y = maxHeight - radius
                velo.y = -velo.y
                change = true
            } else if (y < radius) {
                y = radius
                velo.y = -velo.y
                change = true
            }
            if (change) {
                // hmmmm
//                var newColor = MY_COLORS.random()
//                while (newColor == this.color)
//                    newColor = MY_COLORS.random()
//                this.color = newColor
                velo *= 0.95
            }
        }
    }

    private fun mouseXUpdater(input: Input) {
        circle.addUpdater {
            val xy = input.mouse
            x = xy.x
        }
    }

    private fun inputHandler(input: Input) {
        // Maybe do something like newSpeed = sign(oldSpeed) * ( |oldSpeed| + delta)
        circle.addUpdater {
            if (input.keys.justPressed(Key.UP)) {
                velo.x += SPEED_DELTA
                velo.y += SPEED_DELTA
            }
            if (input.keys.justPressed(Key.DOWN)) {
                velo.x -= SPEED_DELTA
                velo.y -= SPEED_DELTA
            }

            if (input.keys.justReleased(Key.ENTER))
                Console.info("circle coords x=$x y=$y vx=${velo.x} vy=${velo.y} speed=${velo.length} c=${circle.color}")
        }
    }
}

class GasBox : Scene() {
    lateinit var textView: Text
    override suspend fun SContainer.sceneInit() {
        println("Hello there, humans!")
        println(listOf(Colors.PINK, Colors.AQUA, Colors.DARKRED, Colors.DARKKHAKI)[0])

        textView = text("It's hot!", 32.0) {
            x = 100.0
            y = 100.0
            alignment = TextAlignment.LEFT
        }
    }

    val particles = arrayListOf<GasParticle>()

    // TODO: make something nice
    override suspend fun SContainer.sceneMain() {
        // maybe doublerange here?
        (DONT_GO_SLOWER_THAN.toInt()..20).forEach {
            val v = Vector2(
                x = Random.nextDouble(DONT_GO_SLOWER_THAN, 10.0),
                y = Random.nextDouble(DONT_GO_SLOWER_THAN, 10.0),
            )
            particles.add(
                GasParticle(this@GasBox, v, mass = it.toDouble())
            )
        }
        // maybe extract it
        this.addUpdater {
            if (input.keys.justPressed(Key.SPACE)) {
                val v = Vector2(
                    x = Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                    y = Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                )
                particles.add(
                    GasParticle(this@GasBox, v, mass = 60.0)
                )
            }
            if (input.keys.justPressed(Key.BACKSPACE)) {
                particles.removeLastOrNull()?.die()
            }

            val energy = particles.sumOf { it.energy }.toInt()

            textView.text = """N = ${particles.size}
Energy = $energy"""
        }
    }
}
