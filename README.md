Java Classes to work with analytic Functions

I wrote an Abstract Class Function.java, extended by several Classes

Sum.java
Product.java
Quotient.java
Monom.java
Power.java
Func.java

Monom ist the seed to build all functions from, the are of the form Number*Variables.
all others have Functions as attributes, so that one can built functions from inside out


Sum, Product, Quotient and Power are what it says on the can.
Func is a class for all the classic functions, so far I implemented:
sin, cos, tan, ln, exp, acos, asin, atan, atanh

I have methods to differentiate and evaluate Functions.

The Class NewtonFractal.java contains a main method, were one can use this to create Newton Fractals.

