public class Power extends Function{
   Function base,
            exponent;

    public Power(Function b, Function e) {
        base=b;
        exponent=e;
    }

    @Override
    public ComplexNumber evaluate(ComplexNumber[] value) {
        return base.evaluate(value).power(exponent.evaluate(value));
    }

    @Override
    public Function copy() {
        return new Power(base.copy(),exponent.copy());
    }

    @Override
    public Function diff(int i) {
        Function bprime=base.diff(i),
                eprime=exponent.diff(i);
       // System.out.println("base"+base.print(false));
      //  System.out.println("bprime="+bprime.print(false));
        if(eprime==null)return new Product(new Quotient(new Product(bprime,exponent),base),this) ;
        if(bprime==null)new Product(new Product(new Func("ln",base),eprime),this) ;
        return new Product(new Sum(new Function[]{new Product(new Func("ln",base),eprime),new Quotient(new Product(bprime,exponent),base)}),this) ;
    }

    @Override
    public String print(boolean complex) {
        return "("+base.print(complex)+")tothe("+exponent.print(complex)+")";
    }

    @Override
    public Function times(double f) {
        return new Product(new Monom(new ComplexNumber(f,0),new int[]{0,0,0}),this);
    }

    @Override
    public Function simplify()
    {
        base=base.simplify();
        exponent=exponent.simplify();
        if (base==null)return null;
        if(exponent==null)return Monom.one;
        return this;
    }
}
