# deface

[![Build][badge-github-ci]][project-gradle-ci]
[![Maven Central][badge-mvnc]][project-mvnc]

**deface** is an open-source runtime transformation framework for the
[JVM][jvm], written in [Kotlin][kotlin] and [Rust][rust].

**deface** comes in three different flavors:
- [engine][tree-engine]
    - the engine is what makes the transformations happen, implementing the API
      via Native code using the [JVMTI][jvmti] API.
- [hook][tree-hook]
    - the hooking framework, which provides a nicer API, and allows 
      for common "hooking" transformations to be applied easily.
- [mix][tree-mix]
    - a transformation framework heavily inspired by [SpongePowered/Mixin][mixin], 
      which allows for a more declarative and opinionated style of transformation.

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

# using
`TODO`

# building
this project depends on the [Cargo][rust], [Cross][cross], and [osxcross][osxcross] projects.

you'll first need to install [Rust][rust] and [Cargo][rust] on your machine via the [rustup][rustup] tool, then install
`cross` via this command:
```bash
cargo install cross
```

after that, you'd need to setup `osxcross` by following the instructions on their [github repo][osxcross].

then you can build the project via [Gradle][gradle] by running the following command:
```bash
./gradlew build
```
this should download the dependencies and build the project automatically.


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

[tree-engine]: https://github.com/stardust-enterprises/deface/tree/trunk/engine "engine source code"

[tree-hook]: https://github.com/stardust-enterprises/deface/tree/trunk/hook "hook source code"

[tree-mix]: https://github.com/stardust-enterprises/deface/tree/trunk/mix "mix source code"

<!-- Badges -->

[badge-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/deface/badge.svg "maven central badge"

[badge-github-ci]: https://github.com/stardust-enterprises/deface/actions/workflows/build.yml/badge.svg?branch=trunk "github actions badge"
