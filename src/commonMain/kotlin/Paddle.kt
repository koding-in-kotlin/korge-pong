import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.solidRect

class Paddle(val sceneContainer: Container, val x: Double) {
    var rect: SolidRect
    private val height = 100

    init {
        rect = sceneContainer.solidRect(10, height) {
            x = this@Paddle.x
            y = (sceneContainer.height - height) / 2
        }
    }

    operator fun minus(dy: Double): Paddle {
        rect.y -= dy
        if (rect.y < 0) {
            rect.y = 0.0
        }
        return this
    }

    operator fun plus(dy: Double): Paddle {
        rect.y += dy
        if (rect.y > (sceneContainer.height - height)) {
            rect.y = sceneContainer.height - height
        }
        return this
    }
}
