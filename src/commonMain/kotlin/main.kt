import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import kotlin.math.*
import kotlin.random.*

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

data class Velocity(
    var x: Double,
    var y: Double,
) {
    val speed: Double
    get() = kotlin.math.sqrt(x * x + y * y)

    fun normalize(): Velocity {
        return Velocity(x / speed, y / speed)
    }

    operator fun times(other: Double): Velocity {
        return Velocity(x * other, y * other)
    }

    operator fun plus(other: Velocity): Velocity {
        return Velocity(x + other.x, y + other.y)
    }
}

class GasParticle(
    scene: Scene,
    var velo: Velocity,
    startX: Double = 100.0,
    startY: Double = 100.0,
    var mass: Double,
) {
    val sc = scene.sceneContainer
    val circle = sc.circle(17.0, stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = MY_COLORS.random()) {}

    fun die() {
        sc.removeChild(circle)
    }
    init {
//        circle.x = Random.nextDouble(circle.radius, sc.width - circle.radius)
//        circle.y = Random.nextDouble(circle.radius, sc.height - circle.radius)
        circle.x = startX
        circle.y = startY
        registerUpdaters(scene.input, sc.height, sc.width)
        CIRCLE_TO_PARTICLE[circle] = this

        circle.onCollision(filter = { it is Circle }, kind = CollisionKind.SHAPE) {
            asIwantItToBeRenamed(it)
        }
    }

    private fun View.asIwantItToBeRenamed(it: View) {
        val me = CIRCLE_TO_PARTICLE[this]!!
        val other = CIRCLE_TO_PARTICLE[it]!!

        val u1 = me.velo
        val u2 = other.velo

        val M = (me.mass + other.mass)
        val mu11 = (me.mass - other.mass) / M
        val mu12 = 2 * other.mass / M
        val mu21 = 2 * me.mass / M
        val mu22 = (other.mass - me.mass) / M

        me.velo = u1 * mu11 + u2 * mu12
        other.velo = u1 * mu21 + u2 * mu22
    }

    fun registerUpdaters(input: Input, maxHeight: Double, maxWidth: Double, addMouse: Boolean = false) {
        inputHandler(input)
        positionUpdater(maxHeight, maxWidth)
        if (addMouse) mouseXUpdater(input)
    }

    private fun positionUpdater(maxHeight: Double, maxWidth: Double) {
        circle.addFixedUpdater(60.timesPerSecond) {
            var change = false
            x += velo.x
            if ((x + 2 * radius) > maxWidth) {
                velo.x = -velo.x
                change = true
            } else if (x < 0) {
                velo.x = -velo.x
                change = true
            }

            y += velo.y
            if ((y + 2 * radius) > maxHeight) {
                velo.y = -velo.y
                change = true
            } else if (y < 0) {
                velo.y = -velo.y
                change = true
            }
            if (change) {
                var newColor = MY_COLORS.random()
                while (newColor == this.color)
                    newColor = MY_COLORS.random()
                this.color = newColor
                velo = velo * 0.95
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
                Console.info("circle coords x=$x y=$y vx=${velo.x} vy=${velo.y} speed=${velo.speed} c=${circle.color}")
        }
    }
}

class GasBox : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("Hello there, humans!")
        println(listOf(Colors.PINK, Colors.AQUA, Colors.DARKRED, Colors.DARKKHAKI)[0])
    }

    val particles = arrayListOf<GasParticle>()

    // TODO: make something nice
    override suspend fun SContainer.sceneMain() {
        // maybe doublerange here?
        (DONT_GO_SLOWER_THAN.toInt()..2).forEach {
            val v = Velocity(
                x=Random.nextDouble(DONT_GO_SLOWER_THAN, 20.0),
                y=Random.nextDouble(DONT_GO_SLOWER_THAN, 20.0),
            )
            particles.add(
                GasParticle(this@GasBox, v, mass=it.toDouble())
            )
        }
//        particles.add(GasParticle(this@GasBox, Velocity(5.0, 10.0), 200.0, 200.0))
//        particles.add(GasParticle(this@GasBox, Velocity(1.0, 3.0), 300.0, 200.0))
        // maybe extract it
        this.addUpdater {
            if (input.keys.justPressed(Key.SPACE)) {
                val v = Velocity(
                    x=Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                    y=Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                )
                particles.add(
                    GasParticle(this@GasBox, v, mass = Random.nextDouble(10.0, 20.0))
                )
            }
            if (input.keys.justPressed(Key.BACKSPACE)) {
                particles.removeLastOrNull()?.die()
            }

        }
    }
}
