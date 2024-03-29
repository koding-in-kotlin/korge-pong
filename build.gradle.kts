import com.soywiz.korge.gradle.*

plugins {
    alias(libs.plugins.korge)
}

korge {
    id = "com.github.cra"

// To enable all targets at once

    targetAll()

// To enable targets based on properties/environment variables
    //targetDefault()

// To selectively enable targets

    targetJvm()
    targetJs()
    targetDesktop()
    targetIos()
    targetAndroidIndirect() // targetAndroidDirect()

    serializationJson()
    //targetAndroidDirect()
}

