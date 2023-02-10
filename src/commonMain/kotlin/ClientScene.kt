import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.*
import kotlin.random.*

class ClientScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        val state = GameState(
            120.2, 150.0, Point(30.0, 50.0), Vector2D(5.0, 5.0)
        )
        val client = createTcpClient()
        client.connect("127.0.0.1", 5050)
        client.write(state.toMessage())
    }

}
