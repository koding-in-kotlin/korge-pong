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

class GasParticle(
    scene: Scene,
    var mySpeed: Double,
) {
    val sc = scene.sceneContainer
    var direction = 1

    val circle = sc.circle(Random.nextDouble(5.0, 25.0), stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = MY_COLORS.random()) {}

    fun die() {
        sc.removeChild(circle)
    }
    init {
        circle.x = Random.nextDouble(circle.radius, sc.width - circle.radius)
        circle.y = Random.nextDouble(circle.radius, sc.height - circle.radius)
        registerUpdaters(scene.input, sc.height)
    }

    fun registerUpdaters(input: Input, maxHeight: Double, addMouse: Boolean = false) {
        InputHandler(input)
        boundaryUpdater(maxHeight)
        if (addMouse) mouseXUpdater(input)
    }

    private fun boundaryUpdater(maxHeight: Double) {
        circle.addFixedUpdater(60.timesPerSecond) {
            y += mySpeed * direction
            if ((y + 2 * radius) > maxHeight) {
                y = maxHeight - 2*radius
                direction = -direction
                var newColor = MY_COLORS.random()
                while (newColor == this.color)
                    newColor = MY_COLORS.random()
                this.color = newColor
            } else if (y < 0) {
                y = 2*radius
                direction = -direction
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

    private fun InputHandler(input: Input) {
        circle.addUpdater {
            if (input.keys.justPressed(Key.UP))
                mySpeed += SPEED_DELTA
            if (input.keys.justPressed(Key.DOWN))
                mySpeed -= SPEED_DELTA

            if (mySpeed < DONT_GO_SLOWER_THAN) {
                println("YOU'RE GOING TOO SLOW ${mySpeed}!!! STAPH")
                mySpeed = DONT_GO_SLOWER_THAN
            }
            if (input.keys.justReleased(Key.ENTER))
                Console.info("circle coords x=$x y=$y speed=${mySpeed} c=${circle.color}")
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
        (DONT_GO_SLOWER_THAN.toInt()..3).forEach {
            println("YO $it, ${it.toDouble()}")
            particles.add(GasParticle(this@GasBox, it.toDouble()))
        }
        // maybe extract it
        this.addUpdater {
            if (input.keys.justPressed(Key.SPACE))
                particles.add(
                    GasParticle(
                        this@GasBox,
                        Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                    )
                )
            if (input.keys.justPressed(Key.BACKSPACE)) {
                particles.removeLastOrNull()?.die()
            }

        }
    }
}
