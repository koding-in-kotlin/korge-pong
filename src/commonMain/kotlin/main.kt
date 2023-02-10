import com.soywiz.korge.Korge
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korio.net.createTcpClient
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Vector2D

suspend fun main(vararg args: String) = if (args.isEmpty()) {
    Korge(title = "MultiPLONG", width = 1024, height = 768) {
        val sceneContainer = sceneContainer()

        // sceneContainer.changeTo({ Start() })
        sceneContainer.changeTo({ PongGame })

    }

} else {
    val state = GameState(
        120.2, 150.0, Point(30.0, 50.0), Vector2D(5.0, 5.0)
    )
    val client = createTcpClient("127.0.0.3", 5252)
    client.connect("127.0.0.1", 5050)
    client.write(state.toMessage())

}
