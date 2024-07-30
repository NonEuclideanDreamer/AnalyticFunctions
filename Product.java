public class Product extends Function{
    Function f1,f2;

    public Product(Function fone,Function ftwo) {
       f1=fone;
       f2=ftwo;
    }

    @Override
    public ComplexNumber evaluate(ComplexNumber[] value) {

        if(f1==null||f2==null)return ComplexNumber.zero;
        return f1.evaluate(value).times(f2.evaluate(value));
    }

    @Override
    public Function copy() {
        return new Product(f1.copy(),f2.copy());
    }

    @Override
    public Function diff(int i)
    {
        Function prime1=f1.diff(i),
            prime2=f2.diff(i);
        return new Sum(new Function[]{new Product(f1,prime2),new Product(f2,prime1)});
    }

    @Override
    public String print(boolean complex)
    {
        if(f1==null)return"";
        String out="("+f1.print(complex)+")";
        out=out.concat("x("+f2.print(complex))+")";
        return out;
    }

    @Override
    public Function times(double f) {
        return new Product(f1.times(f),f2);
    }

    @Override
    public Function simplify() {
        f1=f1.simplify();
        f2=f2.simplify();
        if(f1==null||f2==null)return null;

        return this;
    }
}
