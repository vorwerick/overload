package cz.dzubera.overloadtest2023

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

class MainApp : ApplicationAdapter() {

    companion object {
        const val MAX_THREAD_COUNT = 1_000
    }

    val executor = Executors.newSingleThreadExecutor()

    var batch: SpriteBatch? = null
    var img: Texture? = null
    var infoTextView: BitmapFont? = null
    var threadCountTextView: BitmapFont? = null
    var threadProcessTextView: BitmapFont? = null

    var rgb = mutableListOf<Float>(0f, 0f, 0f)

    var threads = 0
    var times = mutableListOf<Double>()

    var started = false

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        threadCountTextView = BitmapFont()
        infoTextView = BitmapFont()
        threadProcessTextView = BitmapFont()
    }

    override fun render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !started) {
            started = true
            var future = executor.submit {
                startTest()
            }


        }
        ScreenUtils.clear(rgb[0], rgb[1], rgb[2], 1f)
        batch!!.begin()

        if (!started) {
            infoTextView!!.draw(batch, "Press enter to start", 100f, (Gdx.graphics.height / 2f) + 100)
        }
        threadCountTextView!!.draw(
            batch,
            "Threads: " + threads + "/" + MAX_THREAD_COUNT,
            100f,
            Gdx.graphics.height / 2f
        )
        batch!!.end()
        if (started) {
            randomRgb()
        }
    }

    private fun startTest() {
        for (i in 0 until MAX_THREAD_COUNT) {
            var thread = Thread(ThreadGroup("overload_test"), taskComputing, "OT_THREAD_$i")
            thread.start()
            thread.join()
            threads++
        }
    }

    val taskComputing = Runnable {
        iteratingTest()
        calculatePi()
    }

    private fun iteratingTest() {
        var x: Double = 0.0
        for (i in 0..1000_000) {
            if (i % 2 == 1) {
                x += i
            } else {
                x -= i
            }
        }
    }

    private fun calculatePi(): Double {

        var index = 0
        var pi = 0.0
        var denominator = 1
        val iterations = 1_000_000

        while (index < iterations) {

            if (index % 2 == 0)
                pi += 4.0 / denominator
            else
                pi -= 4.0 / denominator

            index += 1
            denominator += 2
        }

        return pi;
    }


    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }

    private fun randomRgb(): Unit {
        for (i in 0 until rgb.size) {
            val x = Random(System.currentTimeMillis()).nextDouble(-0.1, 0.1)
            when {
                rgb[i] >= 0.1 -> {
                    rgb[i] -= abs(x.toFloat())
                }
                rgb[i] <= 0 -> {
                    rgb[i] += abs(x.toFloat())
                }
                else -> {
                    rgb[i] += x.toFloat()
                }
            }
        }
    }
}