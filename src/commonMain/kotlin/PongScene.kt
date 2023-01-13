import com.soywiz.klock.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*

class PongScene : Scene() {
    private var scoreLeft: Int = 0
    private var scoreRight: Int = 0
    lateinit var sliderRight: SolidRect
    lateinit var sliderLeft: SolidRect
    lateinit var scoreLeftText: Text
    lateinit var scoreRightText: Text

    lateinit var ball: Circle

    override suspend fun SContainer.sceneInit() {
        val x0 = sceneContainer.width / 2
        val y0 = sceneContainer.height / 2
        scoreLeftText = text("LEFT", 32.0) {
            x = 100.0
            y = 50.0
            alignment = TextAlignment.LEFT
        }
        scoreRightText = text("RIGHT", 32.0) {
            x = sceneContainer.width - 100
            y = 50.0
            alignment = TextAlignment.RIGHT
        }

        ball = circle(10.0, Colors.WHITE) {
            x = x0
            y = y0
        }

        sliderLeft = solidRect(10, 100) {
            x = sceneContainer.width / 10
            y = sceneContainer.height / 2 - height / 2
        }

        sliderRight = solidRect(10, 100) {
            x = sceneContainer.width * 9 / 10
            y = sceneContainer.height / 2 - height / 2
        }

        ball.addFixedUpdater(60.timesPerSecond) {
            x += 5.0
            y += 5.0

            if (x > sceneContainer.width) {
                scoreLeft += 1
                x = x0
                y = y0
                scoreLeftText.text = "$scoreLeft"
            }

            if (x < 0) {
                scoreRight += 1
                x = x0
                y = y0
                scoreRightText.text = "$scoreRight"
            }
        }

        ball.onCollision {

        }
    }

    override suspend fun SContainer.sceneMain() {
        println("Not yet implemented")
    }
}
