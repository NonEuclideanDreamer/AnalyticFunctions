public class Quotient extends Function {
    Function num,denom;

    public Quotient(Function n, Function d) {
        num=n;
        denom=d;
    }

    @Override
    public ComplexNumber evaluate(ComplexNumber[] value) {
        if(num==null)return ComplexNumber.zero;
        return num.evaluate(value).divided(denom.evaluate(value));
    }



    @Override
    public Function copy() 
    {
        return new Quotient(num.copy(),denom.copy());
    }

    @Override
    public Function diff(int i)
    {
        Function nprime=num.diff(i),
            dprime=denom.diff(i);
        if(dprime==null) return new Quotient(nprime,denom);
        if(nprime==null) return new Quotient(new Product(num,dprime).times(-1),new Product(denom,denom));
        return new Quotient(new Sum(new Function[]{new Product( denom,nprime),new Product(num,dprime).times(-1)}),new Product(denom,denom));
    }

    @Override
    public String print(boolean complex)
    {
        String out="";
        if (num instanceof Sum) out="("+num.print(complex)+")";
        else out=num.print(complex);
        out=out.concat("div");
        if(denom instanceof Sum || denom instanceof Product || denom instanceof Quotient)out=out.concat("("+denom.print(complex)+")");
       else out=out.concat(denom.print(complex));
        return out;
    }

    @Override
    public Function times(double f) {
        return new Quotient(num.times(f),denom);
    }

    @Override
    public Function simplify() {
        if(num==null)return null;
        num=num.simplify();
        denom=denom.simplify();
        if(num==null)return null;
        if(num.equals(denom))return Monom.one;
        return this;
    }
}
