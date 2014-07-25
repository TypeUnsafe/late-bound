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

public class LateBound<T> {

  public static interface Evaluator {
    String apply();
  }

  private final Evaluator evaluator;
  private final ClassLoader classLoader;

  private Class<T> targetClass;

  public LateBound(Evaluator evaluator, ClassLoader classLoader) {
    this.evaluator = evaluator;
    this.classLoader = classLoader;
  }

  public LateBound(Evaluator evaluator) {
    this(evaluator, LateBound.class.getClassLoader());
  }

  @SuppressWarnings("unchecked")
  public Class<T> targetClass() throws ClassNotFoundException {
    if (targetClass == null) {
      targetClass = (Class<T>) Class.forName(evaluator.apply(), true, classLoader);
    }
    return targetClass;
  }

  public T newInstance() throws ReflectiveOperationException {
    if (targetClass == null) {
      targetClass();
    }
    return targetClass.newInstance();
  }
}
