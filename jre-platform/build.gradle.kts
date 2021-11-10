plugins {
    id("io.github.arc-blroth.cargo-wrapper") version "1.0.0"
}

configure<> {
    cargoCommand = "cross"

    outputs = mapOf(
        'i686-pc-windows-gnu' to 'patcher32.dll',
        'x86_64-pc-windows-gnu' to 'patcher.dll',
        'x86_64-apple-darwin' to 'libpatcher.dylib',
        'i686-unknown-linux-gnu' to 'libpatcher32.so',
        'x86_64-unknown-linux-gnu' to 'libpatcher.so'
    )

    profile = "release"
}