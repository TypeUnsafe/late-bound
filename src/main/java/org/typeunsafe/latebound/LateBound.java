/*
 * Copyright (c) 2014 Julien Ponge
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.typeunsafe.latebound;

/**
 * A factory to ease late-binding at runtime of environment-specific implementation classes.
 *
 * This class is not thread-safe. The target class selection is done just once over the lifetime of a {@code LateBound}
 * instance.
 *
 * The {@code T} parametric type is not enforced at runtime. That is the beauty of generic types not being available
 * for reflective inspection.
 *
 * @param <T> The type of an interface or base class for implementations.
 */
public class LateBound<T> {

  /**
   * A functional interface to select a target implementation class at runtime.
   */
  public static interface Evaluator {

    /**
     * Perform the target class selection.
     *
     * @return the selected qualified class name.
     */
    String apply();
  }

  private final Evaluator evaluator;
  private final ClassLoader classLoader;

  private Class<T> targetClass;

  /**
   * Creates a new {@code LateBound} instance.
   *
   * @param evaluator   an evaluator.
   * @param classLoader a classlaoder.
   */
  public LateBound(Evaluator evaluator, ClassLoader classLoader) {
    this.evaluator = evaluator;
    this.classLoader = classLoader;
  }

  /**
   * Creates a new {@code LateBound} instance with the classloader used to load the {@code LateBound} class itself.
   *
   * @param evaluator an evaluator.
   */
  public LateBound(Evaluator evaluator) {
    this(evaluator, LateBound.class.getClassLoader());
  }

  /**
   * Target class accessor, which may trigger the one-time target class selection.
   *
   * @return a new instance.
   * @throws ClassNotFoundException if the target class cannot be loaded.
   */
  @SuppressWarnings("unchecked")
  public Class<T> targetClass() throws ClassNotFoundException {
    if (targetClass == null) {
      targetClass = (Class<T>) Class.forName(evaluator.apply(), true, classLoader);
    }
    return targetClass;
  }

  /**
   * Creates a new instance, which may trigger the one-time target class selection.
   *
   * The target class must have a no-arguments public constructor.
   *
   * @return a new instance.
   * @throws ReflectiveOperationException in case the class cannot be loaded or instantiated.
   */
  public T newInstance() throws ReflectiveOperationException {
    if (targetClass == null) {
      targetClass();
    }
    return targetClass.newInstance();
  }
}
