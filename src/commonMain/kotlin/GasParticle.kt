import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.input.Input
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.lang.Cancellable
import com.soywiz.korio.lang.cancel
import kotlin.random.Random

class GasParticle(
    scene: Scene,
    var velo: Vector2,
    var mass: Double,
    defaultX: Double? = null,
    defaultY: Double? = null,
    static: Boolean = false,
    val disableCollision : Boolean = false
) {
    private val sc = scene.sceneContainer
    internal var selected = false

    internal val circle = sc.circle(mass, strokeThickness = 3.0, fill = MY_COLORS.random())

    val energy get() = mass * velo.length2 / 2.0

    fun die() {
        sc.removeChild(circle)
        CIRCLE_TO_PARTICLE.remove(circle)
    }

    private var line: Line

    init {
        circle.x = defaultX ?: Random.nextDouble(circle.radius, sc.width - circle.radius)
        circle.y = defaultY ?: Random.nextDouble(circle.radius, sc.height - circle.radius)
        line = circle.line(.0, .0, .0, .0, Colors["#DE3C4B"])
        circle.anchor(.5, .5)

        registerUpdaters(scene.input, sc.height, sc.width, static = static)
        CIRCLE_TO_PARTICLE[circle] = this

        circle.onCollision(filter = { it is Circle }, kind = CollisionKind.SHAPE) {
            handleCollisions(it)
        }

        circle.onCollision(filter = { it is Circle && it.radius > 100.0 }, kind = CollisionKind.SHAPE) {
            (this as Circle).fill = Colors.RED
        }
    }

    private fun View.handleCollisions(it: View) {
        if (disableCollision) return

        // see https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional_collision_with_two_moving_objects
        val me = CIRCLE_TO_PARTICLE[this]!!
        val other = CIRCLE_TO_PARTICLE[it]!!
        val distance = kotlin.math.hypot(x - it.x, y - it.y)
        val neededDistance = me.circle.radius + other.circle.radius
//        if (distance < neededDistance) {
//            val (dx, dy) = Vector2(it.x - x, it.y - y).normalize() * (neededDistance - distance)
//            it.x += dx
//            it.y += dy
//        }
        val u1 = me.velo
        val u2 = other.velo
        val x1 = Vector2(this.x + me.circle.radius, this.y + me.circle.radius)
        val x2 = Vector2(it.x + other.circle.radius, it.y + other.circle.radius)

        val mu1 = 2 * other.mass / (me.mass + other.mass)
        val mu2 = 2 * me.mass / (me.mass + other.mass)
        val f1 = (u1 - u2).dot(x1 - x2) / (x1 - x2).length2
        val f2 = (u2 - u1).dot(x2 - x1) / (x2 - x1).length2

        me.velo = u1 - mu1 * f1 * (x1 - x2)
        other.velo = u2 - mu2 * f2 * (x2 - x1)
    }

    fun registerUpdaters(input: Input, maxHeight: Double, maxWidth: Double, addMouse: Boolean = false, static: Boolean = false) {
        inputHandler(input)
        if (!static) positionUpdater(maxHeight, maxWidth)
        if (addMouse) mouseXUpdater(input)
        wallDestructingUpdater()
    }

    private fun wallDestructingUpdater() {
        circle.addUpdater {
        }
    }

    private lateinit var positionUpdater: Cancellable

    private fun positionUpdater(maxHeight: Double, maxWidth: Double) {
        positionUpdater = circle.addFixedUpdater(60.timesPerSecond, updatable = tick(maxWidth, maxHeight))
    }

    private fun tick(maxWidth: Double, maxHeight: Double): Circle.() -> Unit = {
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
        line.x2 = velo.x * 10
        line.y2 = velo.y * 10
    }

    private fun mouseXUpdater(input: Input) {
        circle.addUpdater {
            val xy = input.mouse
            x = xy.x
        }
    }

    private fun inputHandler(input: Input) {
        circle.onClick {
            if (input.keys.ctrl) {
                selected = true
                circle.fill = Colors.TRANSPARENT_BLACK
            }
        }
        // Maybe do something like newSpeed = sign(oldSpeed) * ( |oldSpeed| + delta)
        circle.addUpdater {
            when {
                input.keys.justPressed(Key.UP) -> {
                    velo.x += SPEED_DELTA
                    velo.y += SPEED_DELTA
                }

                input.keys.justPressed(Key.DOWN) -> {
                    velo.x -= SPEED_DELTA
                    velo.y -= SPEED_DELTA
                }

                input.keys.justPressed(Key.P) -> positionUpdater.cancel()
                input.keys.justPressed(Key.R) -> positionUpdater(sc.height, sc.width)
            }
        }
    }
}
