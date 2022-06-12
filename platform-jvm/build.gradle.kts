plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    this.command.set("cargo")

    cargoInstallTargets.set(true)

    targets {
        // x86_64
        this += target("x86_64-pc-windows-gnu", "deface64.dll")
        this += target("x86_64-unknown-linux-gnu", "libdeface64.so")

        // x86
        this += target("i686-pc-windows-gnu", "deface.dll")
        this += target("i686-unknown-linux-gnu", "libdeface.so")

        // aarch64
        this += target("aarch64-unknown-linux-gnu", "libdeface64.so")

        create("osx-x86") {
            target = "x86_64-apple-darwin"
            outputName = "libdeface64.dylib"

            env += "CC" to "oa64-clang"
            env += "CXX" to "oa64-clang++"

            command = "cargo"
        }
        create("osx-aarch64") {
            target = "aarch64-apple-darwin"
            outputName = "libdeface64.dylib"

            env += "CC" to "oa64-clang"
            env += "CXX" to "oa64-clang++"

            command = "cargo"
        }
    }

    this.release.set(true)
}
