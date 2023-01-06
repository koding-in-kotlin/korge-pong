import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import kotlin.random.*

class DebugScene : Scene() {
    lateinit var textView: Text
    override suspend fun SContainer.sceneInit() {
        textView = text("Debug Scene!", 32.0) {
            x = 515.0
            y = 768.0
            alignment = TextAlignment.CENTER
        }
    }

    override suspend fun SContainer.sceneMain() {
        val L = 75.0
        val R = 405.0
        val B = 395.0
        val T = 80.0
        val r = 100.0
        val bigTL = circle(r, Colors.TRANSPARENT_BLACK, Colors.WHITE, 5.0) {x = L; y = T}
        val bigTR = circle(r, Colors.TRANSPARENT_BLACK, Colors.WHITE, 5.0) {x = R; y = T}
        val bigBL = circle(r, Colors.TRANSPARENT_BLACK, Colors.WHITE, 5.0) {x = L; y = B}
        val bigBR = circle(r, Colors.TRANSPARENT_BLACK, Colors.WHITE, 5.0) {x = R+100; y = B+100}
        bigTR.anchor(0.5, 0.5)
        bigBL.anchor(0.3, 0.3)
        bigBR.anchor(1.0, 1.0)

        textView.text = """Anchors:
TL: ${bigTL.anchorX}, ${bigTL.anchorY}    TR: ${bigTR.anchorX}, ${bigTR.anchorY}
BL: ${bigBL.anchorX}, ${bigBL.anchorY}    BR: ${bigBR.anchorX}, ${bigBR.anchorY}
Small red colored circles are colliding with big ones
"""

        (10..700 step 12).forEach { xx ->
            (0..600 step 12).forEach { yy ->
                circle(
                    3.0,
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
