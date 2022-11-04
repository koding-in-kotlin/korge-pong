import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import kotlin.random.*

suspend fun main() = Korge(title = "Ideal gas here we go", width = 768, height = 768, bgcolor = Colors["#003049"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

const val DONT_GO_SLOWER_THAN = 0.5
val MY_COLORS = listOf(
    Colors.HOTPINK,
    Colors.HOTPINK,
    Colors.HOTPINK,
    Colors["#F77F00"],
    Colors.AQUA,
    Colors.DARKRED,
    Colors.DARKKHAKI,
)

class GasParticle(
    val scene: SContainer,
    var velocity: Double,
    input: Input,
) {

    val circle = this.scene.circle(Random.nextDouble(5.0, 25.0), stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = Colors.PINK) {}

    init {
        circle.x = Random.nextDouble(circle.radius, this.scene.width - circle.radius)
        circle.y = Random.nextDouble(circle.radius, this.scene.height - circle.radius)
        registerUpdaters(input, this.scene.height)
    }

    fun registerUpdaters(input: Input, maxHeight: Double, addMouse: Boolean = false) {
        speedUpdater(input)
        boundaryUpdater(maxHeight)
        if (addMouse) mouseXUpdater(input)
    }

    private fun boundaryUpdater(maxHeight: Double) {
        circle.addFixedUpdater(60.timesPerSecond) {
            y += velocity
            if (
                ((y + 2 * radius) > maxHeight) || y < 0
            ) {
                velocity = -velocity
                var newColor = MY_COLORS.random()
                while (newColor == this.color)
                    newColor = MY_COLORS.random()
                this.color = newColor
            }
        }
    }

    private fun mouseXUpdater(input: Input) {
        circle.addUpdater {
            val xy = input.mouse
            x = xy.x
        }
    }

    private fun speedUpdater(input: Input) {
        circle.addUpdater {
            var dvy = 5.0
            if (input.keys.justPressed(Key.UP)) {
                if (velocity < 0)
                    dvy = -dvy
                if (kotlin.math.abs(velocity + dvy) > DONT_GO_SLOWER_THAN)
                    velocity += dvy
            }
            if (input.keys.justPressed(Key.DOWN)) {
                if (velocity > 0)
                    dvy = -dvy
                if (kotlin.math.abs(velocity + dvy) > DONT_GO_SLOWER_THAN)
                    velocity += dvy
            }
            if (input.keys.justReleased(Key.ENTER))
                Console.info("circle coords x=$x y=$y v=${velocity} c=${circle.color}")
        }
    }
}

class MyScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        println("Hello there, humans!")
        println(listOf(Colors.PINK, Colors.AQUA, Colors.DARKRED, Colors.DARKKHAKI)[0])
    }

    // TODO: make something nice
    override suspend fun SContainer.sceneMain() {
        (DONT_GO_SLOWER_THAN.toInt()..100).forEach{
            GasParticle(this, it.toDouble(), input)
        }
    }
}
