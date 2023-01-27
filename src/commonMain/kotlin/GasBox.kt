import com.soywiz.klock.*
import com.soywiz.klogger.Console
import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import kotlin.random.Random

class GasBox : Scene() {
    lateinit var textView: Text
    override suspend fun SContainer.sceneInit() {
        // println(listOf(Colors.PINK, Colors.AQUA, Colors.DARKRED, Colors.DARKKHAKI)[0])

        textView = text("It's hot!", 32.0) {
            x = 100.0
            y = 100.0
            alignment = TextAlignment.LEFT
        }
    }

    override suspend fun sceneDestroy() {
        super.sceneDestroy()
        // destroy all particles
        particles.forEach {
            it.die()
        }
        particles.clear()
    }

    val particles = arrayListOf<GasParticle>()

    // TODO: make something nice
    override suspend fun SContainer.sceneMain() {

        // maybe doublerange here?
        repeat(3) {
            val v = Vector2(
                x = Random.nextDouble(DONT_GO_SLOWER_THAN, 10.0),
                y = Random.nextDouble(DONT_GO_SLOWER_THAN, 10.0),
            )
            particles.add(
                GasParticle(this@GasBox, v, mass = 50.0)
            )
        }

        particles.windowed(2).forEach {
            val (lhs, rhs) = it
            lhs.circle.fill = Colors.TRANSPARENT_BLACK
            rhs.circle.fill = Colors.TRANSPARENT_BLACK
            line(lhs.x, lhs.y, rhs.x, rhs.y).addFixedUpdater(60.timesPerSecond){
                x1 = lhs.x
                y1 = lhs.y
                x2 = rhs.x
                y2 = rhs.y
            }
        }

/*
        particles.add(GasParticle(this@GasBox, Vector2(.0, .0), 300.0, 100.0, 100.0).apply {
            circle.fill = Colors.TRANSPARENT_BLACK
        })
*/
        (100..700 step 100).forEach { x ->
            (100..700 step 100).forEach { y ->
                particles.add(GasParticle(this@GasBox, Vector2(.0, .0), 10.0, x.toDouble(), y.toDouble()))
            }
        }

        // maybe extract it
        this.addUpdater {
            when {
                input.keys.justPressed(Key.SPACE) -> {
                    val v = Vector2(
                        x = Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                        y = Random.nextDouble(DONT_GO_SLOWER_THAN, 30.0),
                    )
                    particles.add(
                        GasParticle(this@GasBox, v, mass = 60.0)
                    )
                }

                input.keys.justPressed(Key.N) -> {
                    launchImmediately { sceneContainer.changeTo({ DebugScene() }) }
                }

                input.keys.justPressed(Key.BACKSPACE) -> particles.removeLastOrNull()?.die()
                input.keys.justPressed(Key.ENTER) ->
                    particles
                        .filter { it.selected }
                        .forEach {
                            Console.info("Velocity: ${it.velo}, position: ${it.circle.pos}")
                        }
            }

            val energy = particles.sumOf { it.energy }.toInt()

            textView.text = """N = ${particles.size}
Energy = $energy"""
        }
    }
}
