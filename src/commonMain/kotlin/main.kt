import com.soywiz.klogger.*
import com.soywiz.korge.Korge
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korio.lang.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Vector2D

suspend fun main(vararg args: String) = if (Environment.get("MODE") == "server") {
    Korge(title = "MultiPLONG SERVER", width = 1024, height = 768) {
        val sceneContainer = sceneContainer()
        sceneContainer.changeTo({ PongGame })
    }

} else if (Environment.get("MODE") == "client") {
    Korge(title = "MultiPLONG CLIENT", width = 512, height = 384) {
        val sceneContainer = sceneContainer()
        sceneContainer.changeTo({ ClientScene() })
    }
} else {
    val state = GameState(
        120.2, 150.0, Point(30.0, 50.0), Vector2D(5.0, 5.0), 0.0,0.0
    )
    val client = createTcpClient("127.0.0.3", 5252)
    client.connect("127.0.0.1", 5050)
    client.write(state.toMessage())
}
