public class Func  extends Function{

    Function argument;
    String type;

    public Func(String typ, Function arg) {
        type=typ;
        argument=arg;
    }

    @Override
    public ComplexNumber evaluate(ComplexNumber[] value){
       ComplexNumber out=argument.evaluate(value);
        return switch (type) {
            case "sin" -> out.sin();
            case "cos" -> out.cos();
            case "tan" -> out.tan();
            case "exp" -> out.exp();
            case "ln" -> out.log(0);
            case "acos" -> out.acos(0);
            case "re" -> out.Re();
            case "asin" ->out.asin();
            case "atan" ->out.atan(0);
            case "atanh" ->out.atanh(0);
            default -> out;
        };
    }

    @Override
    public Function copy()
    {
        Function out=argument.copy();
       return new Func(type,out);
    }

    @Override
    public Function diff(int i)
    {
        Function diff=argument.diff(i);
        if(diff==null) return null;
        switch (type) {
            case "sin" -> {
                return new Product(diff, new Func("cos", argument));
            }
            case "cos" -> {
                return new Product(diff.times(-1), new Func("sin", argument));
            }
            case "tan" -> {
                Function cos = new Func("cos", argument);
                return new Quotient(diff, new Product(cos, cos));
            }
            case "exp" -> {
                return new Product(diff, this);
            }
            case "ln" -> {
                return new Quotient(diff, argument);
            }
            case "acos" -> {

                return new Quotient(diff,new Power(new Sum(new Function[]{Monom.one,new Product(argument,argument).times(-1)}),Monom.one.times(0.5))).times(-1);
            }
            case "asin" -> {
                return new Quotient(diff,new Power(new Sum(new Function[]{Monom.one,new Product(argument,argument).times(-1)}),Monom.one.times(0.5)));
            }
            case "atan" -> {
                return new Quotient(diff,new Sum(new Function[]{new Product(argument,argument),Monom.one}));
            }

            case "atanh" -> {
                return new Quotient(diff,new Sum(new Function[]{new Product(argument,argument).times(-1),Monom.one}));
            }

            case "re" -> {
                return null;
            }
        }
        return diff;
    }

    @Override
    public String print(boolean complex)
    {
        return type+"("+argument.print(complex)+")";
    }

    @Override
    public Function times(double f) {
        return new Product(new Monom(new ComplexNumber(f,0),new int[]{0,0,0}),this);
    }

    @Override
    public Function simplify() {

        argument=argument.simplify();
        if(argument==null) {
            switch (type) {
                case "sin", "tan", "asin", "re", "atan", "atanh" -> {
                    return null;
                }
                case "cos", "exp" -> {
                    return Monom.one;
                }

                case "acos" -> {
                    return Monom.one.times(Math.PI / 2);
                }
            }
        }
        return this;
    }
}
