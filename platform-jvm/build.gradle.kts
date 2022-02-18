plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    this.command.set("cross")

    listOf(
        "x86_64-pc-windows-gnu" to "deface64.dll",
        "x86_64-unknown-linux-gnu" to "libdeface64.so",
//        "x86_64-apple-darwin" to "libdeface64.dylib",
        "i686-pc-windows-gnu" to "deface.dll",
        "i686-unknown-linux-gnu" to "libdeface.so",
    ).forEach(this.targets::plusAssign)

    this.release.set(true)
}
