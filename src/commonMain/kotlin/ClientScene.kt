import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.onClick
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import com.soywiz.korio.async.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.*
import kotlin.random.*

val SEND_UP = -1
val SEND_DOWN = 1

class ClientScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val client = createTcpClient()
        client.connect("127.0.0.1", 5050)
        uiVerticalStack(150.0) {
            uiButton("Send the State") {
                onClick {
                    val state = GameState(
                        Random.nextDouble(0.0, 200.0),
                        Random.nextDouble(300.0, 400.0),
                        Point(121.0, 100.0),
                        Vector2D(0.0, 0.0),
                    )
                    client.write(state.toMessage())
                }
            }
            uiButton("UP") {
                onClick {
                    client.write(SEND_UP)
                }
            }
            uiButton("DOWN") {
                onClick {
                    client.write(SEND_DOWN)
                }
            }
        }

        this.addUpdater {
            when {
                input.keys.pressing(Key.UP) -> {
                    launchImmediately { client.write(SEND_UP) }

                }

                input.keys.pressing(Key.DOWN) -> {
                    launchImmediately { client.write(SEND_DOWN) }
                }
            }
        }
    }
}
