import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*

suspend fun main() = Korge(title = "Hello there", width = 256, height = 256, bgcolor = Colors["#003049"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {
    override suspend fun SContainer.sceneInit(): Unit {
        println("Hello there, humans!")
    }

    override suspend fun SContainer.sceneMain() {
        val c = circle(20.0, stroke = Colors["#FCBF49"], strokeThickness = 3.0, fill = Colors["#F77F00"]) {
            x = 50.0
            y = 100.0
        }
        var velocityY = 1.0

        c.addFixedUpdater(60.timesPerSecond) {
            y += velocityY
            if (
                ((y + 2 * radius) > this@MyScene.sceneHeight) || y < 0
            ) {
                velocityY = -velocityY
            }
        }
    }
}
