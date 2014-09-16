package com.google.gwt.query.client.functions;

import com.google.gwt.query.client.Function;

public class Functions {

  /**
   * Converts a {@link Func0} to a {@link FuncN} to allow heterogeneous handling of functions with different
   * arities.
   *
   * @param f
   *          the {@code Func0} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <R> FuncN<R> fromFunc(final Func0<? extends R> f) {
    return new FuncN<R>() {

      @Override
      public R call(Object... args) {
        if (args.length != 0) {
          throw new RuntimeException("Func0 expecting 0 arguments.");
        }
        return f.call();
      }

    };
  }

  /**
   * Converts a {@link Func1} to a {@link FuncN} to allow heterogeneous handling of functions with different
   * arities.
   *
   * @param f
   *          the {@code Func1} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0, R> FuncN<R> fromFunc(final Func1<? super T0, ? extends R> f) {
    return new FuncN<R>() {

      @SuppressWarnings("unchecked")
      @Override
      public R call(Object... args) {
        if (args.length != 1) {
          throw new RuntimeException("Func1 expecting 1 argument.");
        }
        return f.call((T0) args[0]);
      }

    };
  }

  /**
   * Converts a {@link Func2} to a {@link FuncN} to allow heterogeneous handling of functions with different
   * arities.
   *
   * @param f
   *          the {@code Func2} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0, T1, R> FuncN<R> fromFunc(final Func2<? super T0, ? super T1, ? extends R> f) {
    return new FuncN<R>() {

      @SuppressWarnings("unchecked")
      @Override
      public R call(Object... args) {
        if (args.length != 2) {
          throw new RuntimeException("Func2 expecting 2 arguments.");
        }
        return f.call((T0) args[0], (T1) args[1]);
      }

    };
  }

  /**
   * Converts a {@link Func3} to a {@link FuncN} to allow heterogeneous handling of functions with different
   * arities.
   *
   * @param f
   *          the {@code Func3} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0, T1, T2, R> FuncN<R> fromFunc(final Func3<? super T0, ? super T1, ? super T2, ? extends R> f) {
    return new FuncN<R>() {

      @SuppressWarnings("unchecked")
      @Override
      public R call(Object... args) {
        if (args.length != 3) {
          throw new RuntimeException("Func3 expecting 3 arguments.");
        }
        return f.call((T0) args[0], (T1) args[1], (T2) args[2]);
      }

    };
  }

  /**
   * Converts an {@link Action0} to a {@link FuncN} to allow heterogeneous handling of functions with
   * different arities.
   *
   * @param f
   *          the {@code Action0} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static FuncN<Void> fromAction(final Action0 f) {
    return new FuncN<Void>() {

      @Override
      public Void call(Object... args) {
        if (args.length != 0) {
          throw new RuntimeException("Action0 expecting 0 arguments.");
        }
        f.call();
        return null;
      }

    };
  }

  /**
   * Converts an {@link Action1} to a {@link FuncN} to allow heterogeneous handling of functions with
   * different arities.
   *
   * @param f
   *          the {@code Action1} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0> FuncN<Void> fromAction(final Action1<? super T0> f) {
    return new FuncN<Void>() {

      @SuppressWarnings("unchecked")
      @Override
      public Void call(Object... args) {
        if (args.length != 1) {
          throw new RuntimeException("Action1 expecting 1 argument.");
        }
        f.call((T0) args[0]);
        return null;
      }

    };
  }

  /**
   * Converts an {@link Action2} to a {@link FuncN} to allow heterogeneous handling of functions with
   * different arities.
   *
   * @param f
   *          the {@code Action2} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0, T1> FuncN<Void> fromAction(final Action2<? super T0, ? super T1> f) {
    return new FuncN<Void>() {

      @SuppressWarnings("unchecked")
      @Override
      public Void call(Object... args) {
        if (args.length != 2) {
          throw new RuntimeException("Action3 expecting 2 arguments.");
        }
        f.call((T0) args[0], (T1) args[1]);
        return null;
      }

    };
  }

  /**
   * Converts an {@link Action3} to a {@link FuncN} to allow heterogeneous handling of functions with
   * different arities.
   *
   * @param f
   *          the {@code Action3} to convert
   * @return a {@link FuncN} representation of {@code f}
   */
  public static <T0, T1, T2> FuncN<Void> fromAction(final Action3<? super T0, ? super T1, ? super T2> f) {
    return new FuncN<Void>() {

      @SuppressWarnings("unchecked")
      @Override
      public Void call(Object... args) {
        if (args.length != 3) {
          throw new RuntimeException("Action3 expecting 3 arguments.");
        }
        f.call((T0) args[0], (T1) args[1], (T2) args[2]);
        return null;
      }

    };
  }

  public static <R> Function fromFuncN(final FuncN<R> f) {
    return new Function() {
      @Override
      public Object f(Object... args) {
        return f.call(args);
      }
    };
  }

  /**
   * Constructs a predicate that returns true for each input for which the source predicate returns false, and
   * vice versa.
   *
   * @param predicate
   *            the source predicate to negate
   * @return a function that returns a Boolean that represents an inversion of the logical effect of
   *         {@code predicate}
   */
  public static <T> Func1<T, Boolean> not(Func1<? super T, Boolean> predicate) {
    return new Not<T>(predicate);
  }

  /**
   * Returns a function that always returns {@code true}.
   *
   * @return a {@link Func1} that accepts an Object and returns the Boolean {@code true}
   */
  public static <T> Func1<? super T, Boolean> alwaysTrue() {
    return AlwaysTrue.INSTANCE;
  }

  /**
   * Returns a function that always returns {@code false}.
   *
   * @return a {@link Func1} that accepts an Object and returns the Boolean {@code false}
   */
  public static <T> Func1<? super T, Boolean> alwaysFalse() {
    return AlwaysFalse.INSTANCE;
  }

  /**
   * Returns a function that always returns the Object it is passed.
   *
   * @return a {@link Func1} that accepts an Object and returns the same Object
   */
  public static <T> Func1<T, T> identity() {
    return new Func1<T, T>() {
      @Override
      public T call(T o) {
        return o;
      }
    };
  }

  private enum AlwaysTrue implements Func1<Object, Boolean> {
    INSTANCE;

    @Override
    public Boolean call(Object o) {
      return true;
    }
  }

  private enum AlwaysFalse implements Func1<Object, Boolean> {
    INSTANCE;

    @Override
    public Boolean call(Object o) {
      return false;
    }
  }

  /**
   * Returns a function that merely returns {@code null}, without side effects.
   *
   * @return a function that returns {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> NullFunction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> returnNull() {
    return (NullFunction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R>) NULL_FUNCTION;
  }

  @SuppressWarnings("rawtypes")
  private static final NullFunction NULL_FUNCTION = new NullFunction();

  private static final class NullFunction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements
      Func0<R>,
      Func1<T0, R>,
      Func2<T0, T1, R>,
      Func3<T0, T1, T2, R>,
      FuncN<R> {
    @Override
    public R call() {
      return null;
    }

    @Override
    public R call(T0 t1) {
      return null;
    }

    @Override
    public R call(T0 t1, T1 t2) {
      return null;
    }

    @Override
    public R call(T0 t1, T1 t2, T2 t3) {
      return null;
    }

    @Override
    public R call(Object... args) {
      return null;
    }
  }
}
