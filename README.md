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

[tree-engine]: https://github.com/stardust-enterprises/deface/tree/trunk/engine "engine source code"

[tree-hook]: https://github.com/stardust-enterprises/deface/tree/trunk/hook "hook source code"

[tree-mix]: https://github.com/stardust-enterprises/deface/tree/trunk/mix "mix source code"

<!-- Project Links -->

[project-url]: https://github.com/stardust-enterprises/deface "project github repository"

[fork]: https://github.com/stardust-enterprises/deface/fork "fork this repository"

[new-pr]: https://github.com/stardust-enterprises/deface/pulls/new "create a new pull request"

[new-issue]: https://github.com/stardust-enterprises/deface/issues/new "create a new issue"

[project-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/deface "maven central repository"

[project-gradle-ci]: https://github.com/stardust-enterprises/deface/actions/workflows/build.yml "gradle ci workflow"

[project-license]: https://github.com/stardust-enterprises/deface/blob/trunk/LICENSE "LICENSE source file"

<!-- Badges -->

[badge-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/deface/badge.svg "maven central badge"

[badge-github-ci]: https://github.com/stardust-enterprises/deface/actions/workflows/build.yml/badge.svg?branch=trunk "github actions badge"
