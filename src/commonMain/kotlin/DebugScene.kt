import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import kotlin.random.*

class DebugScene : Scene() {
    lateinit var textView: Text
    override suspend fun SContainer.sceneInit() {
        textView = text("Debug Scene!", 32.0) {
            x = 300.0
            y = 100.0
            alignment = TextAlignment.RIGHT
        }
    }

    override suspend fun SContainer.sceneMain() {
        val bigTL = circle(
            150.0,
            Colors.TRANSPARENT_BLACK ,
            Colors.WHITE,
            5.0
        ) {
            x = 100.0
            y = 100.0
        }
        val bigTR = circle(
            150.0,
            Colors.TRANSPARENT_BLACK ,
            Colors.WHITE,
            5.0
        ) {
            x = 300.0
            y = 100.0
        }
        val bigBL = circle(
            150.0,
            Colors.TRANSPARENT_BLACK ,
            Colors.WHITE,
            5.0
        ) {
            x = 100.0
            y = 300.0
        }
        val bigBR = circle(
            150.0,
            Colors.TRANSPARENT_BLACK ,
            Colors.WHITE,
            5.0
        ) {
            x = 300.0
            y = 300.0
        }

        (50..700 step 50).forEach { xx ->
            (50..700 step 50).forEach { yy ->
                circle(
                    5.0,
                    Colors.GREEN
                ){
                    x = xx.toDouble()
                    y = yy.toDouble()
                }.onCollision (filter = { it is Circle && it.radius > 15.0 }, kind = CollisionKind.SHAPE){
                    (this as Circle).fill = Colors.RED
                }
            }
        }

    }
}
