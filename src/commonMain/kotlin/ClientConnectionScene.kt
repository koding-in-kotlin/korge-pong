import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.onClick
import com.soywiz.korio.async.*
import com.soywiz.korio.net.*

object ClientConnectionScene: Scene() {
    lateinit var client: AsyncClient
    override suspend fun SContainer.sceneInit() {
        uiButton("Connect") {
            onClick {
                client = createTcpClient("127.0.0.1", 5050)
                sceneContainer.changeTo({ ClientScene() })
            }
        }
    }
}
