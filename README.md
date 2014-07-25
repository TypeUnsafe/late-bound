# Late-Bound, a no-fluff late-binding helper for Java

There are times when you need to support features based on the runtime context. A good example would be supporting Java 8 new features and APIs while preserving Java 7 (or even 6) compatibility.

The typical solution for that is to provide either an interface or an abstract class, and have concrete implementations depend on conditions at runtime.

There are 2 obvious ways to do that:

1. hack your own conditional class loading and reflection to call the target code, or
2. use a full-blown dependency injection container like Spring, Guice, Dagger or PicoContainer.

Late-Bound provides a no-fluff abstraction when all you want is a simpler solution.

## Sample usage

This library consists of a single factory class `LateBound`.

Here is a smoke test in Java 6 style that shows the usage to unconditionally provide `LinkedList` instances:

```java
import org.typeunsafe.latebound.LateBound;

// (...)

LateBound<List> lateBound = new LateBound<List>(new LateBound.Evaluator() {
  @Override
  public String apply() {
    return "java.util.LinkedList";
  }
});

assertThat(lateBound.targetClass()).isEqualTo(LinkedList.class);
assertThat(lateBound.newInstance()).isInstanceOf(LinkedList.class);
```

* Implementation classes shall have a no-arguments constructor.
* The implementation selection is done by an evaluator functional interface that returns a qualified class name.
* ...that's it!

## Maven, Gradle, etc

* `groupId`: `org.typeunsafe`
* `artifactId`: `late-bound`
* `version`: (to be released)

## License

    Copyright (c) 2014 Julien Ponge

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
