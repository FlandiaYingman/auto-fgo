package top.anagke.auto_ark.pnc

import top.anagke.auto_ark.adb.BlueStacks
import top.anagke.auto_ark.adb.Device
import top.anagke.auto_ark.adb.assert
import top.anagke.auto_ark.adb.await
import top.anagke.auto_ark.img.Img
import top.anagke.auto_ark.img.Tmpl
import java.io.FileNotFoundException
import java.net.URL


object PncRes {
    operator fun invoke(name: String): URL? {
        return this.javaClass.getResource(name)
    }
}

fun template(name: String, diff: Double = 0.05): Tmpl {
    val url = PncRes(name)
    var count = 0

    if (url == null) {
        val urlList = mutableListOf<URL>()
        do {
            val varUrl = PncRes("${name.substringBeforeLast(".")}_${count++}.${name.substringAfterLast(".")}")
            if (varUrl != null) urlList.add(varUrl)
        } while (varUrl != null)
        if (urlList.isEmpty()) {
            throw FileNotFoundException("resource '$name' not found by '${PncRes.javaClass.packageName}'")
        } else {
            return Tmpl(name, diff, urlList.map { Img.decode(it.readBytes())!! })
        }
    } else {
        return Tmpl(name, diff, listOf(Img.decode(url.readBytes())!!))
    }
}

// 主界面
val atMainScreen = template("atMainScreen.png")

// 可跳回主界面
val canJumpOut = template("canJumpOut.png")

fun Device.jumpOut() {
    assert(canJumpOut)
    tap(250, 50)
    await(atMainScreen)
}

fun main() {
    val device = BlueStacks().connect()
    device.jumpOut()
}