# deface
deface is an open-source hooking engine and framework for the 
[JVM][jvm]/[ART][art], written in [Kotlin][kotlin] and [Rust][rust].

## structure
the project is divided into 2 types of submodules:
 - [engine][engine]
   - the transformation engine, everything is based around it
 - platforms 
   - platform implementations of the engine native, such as 
   [platform-jvm][platform-jvm] or [platform-art][platform-art]
 - frameworks 
   - tools designed around the [engine][engine] for ease of use, like 
   [framework-patcher][fwpatcher] and [framework-mix][fwmix]
    

# contributing

you can contribute by [forking the repository][fork], making your changes and [creating a new pull request][new-pr]
describing what you changed, why and how.

# licensing

this project is under the [ISC license][blob-license].
<!-- Links -->

[jvm]: https://adoptium.net "JVM"

[art]: https://source.android.com/devices/architecture/modular-system/art "Android Runtime"

[kotlin]: https://kotlinlang.org "kotlin website"

[rust]: https://rust-lang.org "rust website"

[engine]: https://github.com/stardust-enterprises/deface/tree/trunk/engine

[platform-jvm]: https://github.com/stardust-enterprises/deface/tree/trunk/platform-jvm

[platform-art]: https://github.com/stardust-enterprises/deface/tree/trunk/platform-art

[fwpatcher]: htps://github.com/stardust-enterprises/deface/tree/trunk/framework-patcher

[fwmix]: htps://github.com/stardust-enterprises/deface/tree/trunk/framework-mix

[fork]: https://github.com/stardust-enterprises/deface/fork "fork this repository"

[new-pr]: https://github.com/stardust-enterprises/deface/pulls/new "create a new pull request"

[blob-license]: https://github.com/stardust-enterprises/deface/blob/trunk/LICENSE "LICENSE source file"
