import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*

suspend fun main() = Korge(title = "MultiPLONG", width = 1024, height = 768) {
    val sceneContainer = sceneContainer()

    // sceneContainer.changeTo({ PongScene() })
    sceneContainer.changeTo({ PongScene() })

}

