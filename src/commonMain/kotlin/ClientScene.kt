import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.*

class ClientScene : Scene() {
    override suspend fun SContainer.sceneInit() {

        uiVerticalStack(width = 150.0) {
            uiButton("Open Window List") {
                onClick {
                    val state = GameState(
                        120.2, 150.0, Point(30.0, 50.0), Vector2D(5.0, 5.0)
                    )
                    val client = createTcpClient("127.0.0.1", 5050)
//                    client.connect()
                    client.write(state.toMessage())

                }
            }
        }.position(32.0, 32.0)

    }

}
