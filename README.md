# deface

[![Build][badge-github-ci]][project-gradle-ci]
[![Maven Central][badge-mvnc]][project-mvnc]

**deface** is an open-source runtime transformation library for the
[JVM][jvm], written in [Kotlin][kotlin] and [Rust][rust].

# importing

you can import **[deface][project-url]** from [maven central][mvnc] just by adding it to your dependencies:

## gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("fr.stardustenterprises:deface:0.4.0") // or latest version
}
```

## maven

```xml
<dependency>
    <groupId>fr.stardustenterprises</groupId>
    <artifactId>deface</artifactId>
    <!-- or latest version -->
    <version>0.4.0</version> 
</dependency>
```

# usage
`TODO`

# building
**important**: this part assumes you're using a Linux host as `osxcross` isn't supported yet on Windows hosts. 

this project depends on the [Cargo][rust] and [osxcross][osxcross] projects.

to install cargo, install [Rust][rust] on your machine via the [rustup][rustup] tool.

## toolchains

### windows & linux arm
this project requires multiple cross-compilation toolchains, which might differ depending on your distribution.

here are the required packages for ArchLinux:
```shell
sudo pacman -Sy --noconfirm \
  gcc \
  mingw-w64-gcc mingw-w64-headers \
  aarch64-linux-gnu-gcc
```

### macOS
to create a cross-compilation toolchain for macOS, you'd need to setup `osxcross` 
by following the instructions on their [github repo][osxcross].

## actually building
you can then build the project via [Gradle][gradle] by running the following command:
```bash
./gradlew build
```
this should download the project dependencies and build the project automatically.

# troubleshooting

if you ever encounter any problem **related to this project**, you can [open an issue][new-issue] describing what the
problem is. please, be as precise as you can, so that we can help you asap. we are most likely to close the issue if it
is not related to our work.

# contributing

you can contribute by [forking the repository][fork], making your changes and [creating a new pull request][new-pr]
describing what you changed, why and how.

# licensing

this project is under the [ISC license][project-license].


<!-- Links -->

[jvm]: https://adoptium.net "adoptium website"

[kotlin]: https://kotlinlang.org "kotlin website"

[rust]: https://rust-lang.org "rust website"

[mvnc]: https://repo1.maven.org/maven2/ "maven central website"

[jvmti]: https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html "jvmti documentation"

[mixin]: https://github.com/SpongePowered/Mixin "sponge's mixin"

[gradle]: https://gradle.org "gradle website"

[rustup]: https://www.rust-lang.org/learn/get-started "rustup website"

[cross]: https://github.com/cross-rs/cross "cross github page"

[osxcross]: https://github.com/tpoechtrager/osxcross "osxcross github page"

<!-- Project Links -->

[project-url]: https://github.com/stardust-enterprises/deface "project github repository"

[fork]: https://github.com/stardust-enterprises/deface/fork "fork this repository"

[new-pr]: https://github.com/stardust-enterprises/deface/pulls/new "create a new pull request"

[new-issue]: https://github.com/stardust-enterprises/deface/issues/new "create a new issue"

[project-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/deface "maven central repository"

[project-gradle-ci]: https://github.com/stardust-enterprises/deface/actions/workflows/build.yml "gradle ci workflow"

[project-license]: https://github.com/stardust-enterprises/deface/blob/trunk/LICENSE "LICENSE source file"

[tree-engine]: https://github.com/stardust-enterprises/deface/tree/trunk/src "engine source code"

[tree-hook]: https://github.com/stardust-enterprises/deface/tree/trunk/hook "hook source code"

[tree-mix]: https://github.com/stardust-enterprises/deface/tree/trunk/mix "mix source code"

<!-- Badges -->

[badge-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/deface/badge.svg "maven central badge"

[badge-github-ci]: https://github.com/stardust-enterprises/deface/actions/workflows/build.yml/badge.svg?branch=trunk "github actions badge"
