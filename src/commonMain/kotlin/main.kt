import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*

suspend fun main() = Korge(title = "Ideal gas here we go", width = 768, height = 768, bgcolor = Colors["#000052"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ GasBox() })
}

const val DONT_GO_SLOWER_THAN = 1.0
const val SPEED_DELTA = 2
val MY_COLORS = listOf(
    Colors["#D5DFE5"],
    Colors["#C9B1BD"],
    Colors["#7F9172"],
    Colors["#8B9B7F"],
    Colors["#96A48B"],
)

internal val CIRCLE_TO_PARTICLE = hashMapOf<Circle, GasParticle>()

operator fun Double.times(other: Vector2): Vector2 {
    return other * this
}

