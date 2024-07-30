import java.util.ArrayList;

public abstract class Function {

    static String[] variables;

   public abstract ComplexNumber evaluate(ComplexNumber[]value);

    public abstract Function copy();

    public abstract Function diff(int i);

    public abstract String print(boolean complex);


    public abstract Function times(double f);

   public static Function parse(String f)
    {
        ArrayList<Function> fu=new ArrayList<Function>();
        boolean notdone=true;
        while(notdone) {
            int bracket = fu.indexOf('('),
                    plus = fu.indexOf('+');
            if(plus<bracket) fu.add(Monom.parse(f.substring(0,plus)));
            else if(plus==bracket) {
                fu.add(Monom.parse(f));
                notdone=false;
            }
            else if(bracket>0)
            {
                String type=f.substring(0,bracket);
                int ket=closebracket(f,bracket);
                if(ket==f.length()-1){
                    fu.add(new Func(type, Function.parse(f.substring(bracket+1,ket))));
                    notdone=false;
                }
            }
            else {
                int ket=closebracket(f,bracket);
                if(f.substring(ket+1,ket+2).equals("x")){
                    fu.add(new Product(parse(f.substring(bracket+1,ket)),parse(f.substring(ket+3,closebracket(f,ket+2)))));
                }
            }
        }
        Function[]fun=new Function[fu.size()];
        for(int i=0;i<fu.size();i++)fun[i]=fu.get(i);
      return new Sum(fun);
    }

    public abstract Function simplify();

    private static int closebracket(String f, int bracket) {
       int counter=1;
       while(counter>0)
       {
           int bra=f.indexOf('(',bracket+1),
                   ket=f.indexOf('(',bracket+1);
           if(bra<ket){
               counter++;
               bracket=bra;
           }
           else{
               counter--;
               bracket=ket;
           }
       }
       return bracket;
    }
}
