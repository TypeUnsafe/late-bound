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

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LateBoundTest {

  @Test
  @SuppressWarnings("unchecked")
  public void smoke_test() throws ReflectiveOperationException {
    LateBound<List> lateBound = new LateBound<List>(new LateBound.Evaluator() {
      @Override
      public String apply() {
        return "java.util.LinkedList";
      }
    });

    assertThat(lateBound.targetClass()).isEqualTo(LinkedList.class);
    assertThat(lateBound.newInstance()).isInstanceOf(LinkedList.class);
  }

  @Test(expected = ClassCastException.class)
  public void not_a_supertype() throws ReflectiveOperationException {
    LateBound<List> lateBound = new LateBound<List>(new LateBound.Evaluator() {
      @Override
      public String apply() {
        return "java.lang.Object";
      }
    });
    List list = lateBound.newInstance();
  }
}