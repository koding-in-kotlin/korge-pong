import com.soywiz.korge.*
import com.soywiz.korge.scene.*

suspend fun main() = Korge(title = "MultiPLONG", width = 1024, height = 768) {
    val sceneContainer = sceneContainer()

    // sceneContainer.changeTo({ Start() })
    sceneContainer.changeTo({ PongGame })

}

