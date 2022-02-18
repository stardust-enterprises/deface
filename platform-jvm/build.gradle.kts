plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    this.command.set("cross")

    listOf(
        "x86_64-pc-windows-gnu" to "engine64.dll",
        "x86_64-unknown-linux-gnu" to "libengine64.so",
//        "x86_64-apple-darwin" to "libengine64.dylib",
        "i686-pc-windows-gnu" to "engine.dll",
        "i686-unknown-linux-gnu" to "libengine.so",
    ).forEach(this.targets::plusAssign)
}
