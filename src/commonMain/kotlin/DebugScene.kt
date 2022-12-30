import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*

class DebugScene : Scene() {
    lateinit var textView: Text
    override suspend fun SContainer.sceneInit() {
        textView = text("Debug Scene!", 32.0) {
            x = 300.0
            y = 100.0
            alignment = TextAlignment.RIGHT
        }
    }
}
