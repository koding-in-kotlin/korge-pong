import com.soywiz.klock.seconds
import com.soywiz.klogger.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.tests.ViewsForTesting
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import kotlin.test.*

class MyTest : ViewsForTesting() {
    @Test
    fun test() = viewsTest {
        val log = arrayListOf<String>()
        val rect = solidRect(100, 100, Colors.RED)
        rect.onClick {
            log += "clicked"
        }
        assertEquals(1, views.stage.numChildren)
        rect.simulateClick()
        assertEquals(true, rect.isVisibleToUser())
        tween(rect::x[-102], time = 10.seconds)
        assertEquals(Rectangle(x = -102, y = 0, width = 100, height = 100), rect.globalBounds)
        assertEquals(false, rect.isVisibleToUser())
        assertEquals(listOf("clicked"), log)
    }

    @Test
    fun testStateSerialization() {
        val gameState = GameState(
            .5,
            .1,
            Point(.2, .3),
            Point(.4, 0.5),
            2.0,
            1.0
        )
        assertEquals(GameState.thickness, gameState.toMessage().size)
        // SORRY THIS IS BROKEN I KNOW
        assertEquals(gameState, gameState.toMessage().toGameState())
    }
}
