[![Build Status](https://travis-ci.com/naderghanbari/cl-scala.svg?branch=master)](https://travis-ci.com/naderghanbari/cl-scala)

## Simple Combinatory Logic in Scala

This is a humble attempt to learn Combinatory Logic by implementing a
very simple DSL for CL-terms and Term reduction in Scala.

The terminology, theorems and lemmas are taken from
`Lambda-Calculus and Combinators, an Introduction` by `J. Roger Hindley`
 and `Jonathan P. Seldin`, `Cambridge University Press 2008`,
 specifically from the second chapter.

### First Sample

```scala

import cl._

val (x, y, z) = (Var('x'), Var('y'), Var('z'))
val Kx        = K(x)
val Kxy       = Kx(y)
val K_xy      = K $ x(y)

val shouldBeTrue = x ⊆ Kxy
val l            = Kxy.length

val freeVars     = K_xy.FV
```

### Structure
  - `core`: The DSL is implemented in this sub-project.
  - `repl`: A mini language (mini-CL) + a REPL.

### ADT Basics
The `Term` ADT is implemented in the package object `cl`.
So you will need the following import everywhere:

```scala
import cl._
```

#### Variables
As the literature suggests, variables must have a lower case letter as
their name.

```scala
import cl._

val x = Var('x')
val y = Var('y')
```

#### Basic Combinators
The three musketeers, aka basic combinators `I`, `K`, and `S`, are
predefined.

```scala
import cl._
val x = Var('x')
val y = Var('y')

val N = I(x)           // Ix
val M = K(x)(y)        // Kxy
val U = S(K)(S)        // SKS
```

#### Application
We are in the Untyped Combinatory Logic land so any `Term` is
applicable to any `Term`.

Our DSL is like Haskell and most of the literature in that you can use
  - the `apply` function as in `K(S)`
  - or the `$` operator as in `K $ S`

```scala
import cl._
val x = Var('x')
val y = Var('y')

val M = S(K)(S)                // SKS
val N = S $ K(S)               // S(KS)
val U = K(I) $ S(S)            // KI(SS)
```

Scala's spec (similar to that of Haskell's in this case) gives higher
precedence to the `$` operator. This means the `apply` function will
be left associative just like the convention used in the CL literature.
`$` can be used to break the left associativity. In most cases this
will avoid the need to group things with lots of parenthesises.

```scala
val H = S(K)(I)(K(S(S)))
val E = S(K)(I) $ (K $ S(S))       // Same thing, more readable
```

### Pattern Matching on Term ADT
Pattern matching (aka induction on the structure of the CL Terms) is
the essence of most Combinatory Logic theorems and algorithms.

```scala
   M match {
     case Var(x) => ...                 // Variables
     case _U $ _V => ...                // Application of term _U to _V
     case BasicCombinator(name) =>  ... // one of I, K, S
   }
```

One can also match on `I`, `K`, and `S` directly:

```scala
  M match {
    case I => ...
    case K => ...
    case S => ...
    case _ =>
  }
```

Note: due to Scala's spec, the following pattern does not work:

```scala
  ...
  case M $ N => ... // Wrong, value patterns can't begin with uppercase
  ...
```

and it's not a good idea to use lower case names for CL Terms, hence the
underscore workaround:

```scala
  ...
  case _M $ _N => ... // Ok! Underscore is fine
  ...
```
