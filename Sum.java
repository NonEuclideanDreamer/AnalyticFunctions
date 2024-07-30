public class Sum extends Function{
    Function[]summand;

    public Sum(Function[] s)
    {
        summand=s;
    }

    @Override
    public ComplexNumber evaluate(ComplexNumber[] value)
    {
        ComplexNumber out=ComplexNumber.zero;
        for(int i=0;i<summand.length;i++)
        {
            if(summand[i]!=null)
                out=out.add(summand[i].evaluate(value));
        }
        return out;
    }

    public Function simplify()
    {
        int counter=0;
        for (Function function : summand) {
            if (function != null)
            function=function.simplify();
            if (function != null) {
                counter++;
            }
        }


        if(counter<summand.length)
        {
            if(counter==0)return null;
            if(counter==1)
            {
                for (Function function : summand) {
                    if (function != null) {
                        return function;
                    }
                }
            }
            Function[]s=new Function[counter];
            counter=0;
            for (Function function : summand) {
                if (function != null) {
                    s[counter] = function;
                    counter++;
                }
            }
            return new Sum(s);
        }
        return this;
    }

    @Override
    public Function copy()
    {
        Function[]s=new Function[summand.length];
        for(int i=0;i<s.length;i++)
        {
            s[i]=summand[i].copy();
        }
        return new Sum(s);
    }

    @Override
    public Function diff(int i)
    {
        Function[]out=new Function[summand.length];
        for(int j=0;j< summand.length;j++)
            out[j]=summand[j].diff(i);
        Sum d= new Sum(out);
        d.simplify();
        return d;
    }

    @Override
    public String print(boolean complex)
    {
        String out="",temp;
        boolean plus=false;
        for(int i=0;i< summand.length;i++)
        {
            if(plus)
                out=out.concat("+");
            temp=summand[i].print(complex);
            if(temp.isEmpty()) plus=false;
            else {
                plus=true;
                out=out.concat(temp);
            }
        }
        return out;
    }

    @Override
    public Sum times(double f) {
        Function[]out=new Function[summand.length];
        for(int i=0;i<summand.length;i++)
            out[i]=summand[i].times(f);
        return new Sum(out);
    }
   public Sum concat(Sum s)
    {
        Function[]out=new Function[summand.length+s.summand.length];
        for(int i=0;i<summand.length;i++)
            out[i]=summand[i];
        for(int i=0;i<s.summand.length;i++)
            out[i+summand.length]=s.summand[i];
        return new Sum(out);
    }
}
