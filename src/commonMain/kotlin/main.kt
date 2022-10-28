import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korim.paint.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*
import kotlin.random.*

suspend fun main() = Korge(title = "Hello there", width = 512, height = 512, bgcolor = Colors["#003049"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {
    override suspend fun SContainer.sceneInit(): Unit {
        println("Hello there, humans!")
        println(listOf(Colors.PINK, Colors.AQUA, Colors.DARKRED, Colors.DARKKHAKI)[0])
    }

    // TODO: make something nice
    override suspend fun SContainer.sceneMain() {
        val c = circle(20.0, stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = Colors.PINK) {
            x = 50.0
            y = 100.0

        }

        var velomap = hashMapOf(c to 10.0)
        var velocityY = 10.0
        val myColors = listOf(
            Colors.HOTPINK,
            Colors.HOTPINK,
            Colors.HOTPINK,
            Colors["#F77F00"],
            Colors.AQUA,
            Colors.DARKRED,
            Colors.DARKKHAKI,
        )
        val dontGoSlowerThan = 0.5
        c.addUpdater {
            val xy = input.mouse
            x = xy.x
            var dvy = 5.0
            val currentVelocity = velomap[this]
            if (currentVelocity != null) {
                if (input.keys.justPressed(Key.UP)) {
                    if (currentVelocity < 0) {
                        dvy = -dvy
                    }
                    if (kotlin.math.abs(currentVelocity + dvy) > dontGoSlowerThan) velomap[this] = currentVelocity + dvy
                }
                if (input.keys.justPressed(Key.DOWN)) {
                    if (currentVelocity > 0) {
                        dvy = -dvy
                    }
                    if (kotlin.math.abs(currentVelocity + dvy) > dontGoSlowerThan) velomap[this] = currentVelocity + dvy
                }
            }
            if (input.keys.justReleased(Key.ENTER)) Console.info("circle coords x=$x y=$y v=$velomap")
        }

        c.addFixedUpdater(60.timesPerSecond) {
            val currentVelocity = velomap[this]
            y += currentVelocity ?: 0.0
            if (currentVelocity != null) {
                if (
                    ((y + 2 * radius) > this@MyScene.sceneHeight) || y < 0
                ) {
                    velomap[this] = -currentVelocity
                    var newColor = myColors.random()
                    while (newColor == this.color) {
                        newColor = myColors.random()
                    }
                    this.color = newColor
                }
            }
        }
    }
}
