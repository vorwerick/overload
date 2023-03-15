package cz.dzubera.overloadtest2023

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

class DesktopLauncher {


    companion object {
        @JvmStatic fun main(args : Array<String>) {
            val config = Lwjgl3ApplicationConfiguration()
            config.setForegroundFPS(60)
            config.setTitle("overloadtest-2023")
            Lwjgl3Application(MainApp(), config)
        }
    }
}