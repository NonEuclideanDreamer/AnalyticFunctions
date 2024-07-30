//******************************************
//
//********************************************
public class Monom extends Function
{
    static String[] variables= {"z","c","t"};
    int[] vfactors;//cfactors,
    ComplexNumber constant;

    public static Monom one=new Monom(ComplexNumber.one,new int[variables.length]),
    zero=new Monom(ComplexNumber.zero,new int[variables.length]);
    //static ComplexNumber[] constants;//field may be change but I assume commutativity&associativity

    public Monom(ComplexNumber c,int[]v)
    {
        constant=c;
        vfactors=v;
    }
    public Monom(int a, int b, int c)
    {
        constant=ComplexNumber.one;
        vfactors= new int[]{a,b,c};
    }
    public Monom(double c,int[]v)
    {
        constant=new ComplexNumber(c,0);
        vfactors=v;
    }



    public ComplexNumber evaluate(ComplexNumber[]value)
    {	//		System.out.println(print()+" constant="+constant.toString(10)+", norm="+constant.norm());
        if( this==null)return ComplexNumber.zero;
        //if(equals(null))return ComplexNumber.zero; //This should never happen I think? we'll see
        //System.out.print("Evaluate "+print()+" at "+value[0].toString(10));
        if(constant.equals(ComplexNumber.zero)) {return ComplexNumber.zero;}

        ComplexNumber out=constant.copy();
		/*for(int i=1;i<cfactors.length;i++)
			if(cfactors[i]!=0)out=out.times(constants[i].power(cfactors[i]));*/
        for(int i=0;i<vfactors.length;i++)
            if(vfactors[i]!=0)out=out.times(value[i].power(vfactors[i]));

        //System.out.println(": "+out.toString(10));
        return out;
    }
    public String print(boolean complex)
    {
        String out="";
        if(constant.equals(ComplexNumber.zero))return "";
        if(!constant.equals(ComplexNumber.one)){

        if(complex)out="("+constant+")";
        else if(!constant.equals(ComplexNumber.one)) out=""+constant.x;}
        for(int i=0;i<vfactors.length;i++)
        {
            if(vfactors[i]>0)out=out.concat(variables[i]);
            if(vfactors[i]>1)out=out.concat("^"+vfactors[i]);
        }
        if(out.equals(""))out="1";
        return out;
    }

    @Override
    public Function times(double f) {
        Monom out=copy();
        out.constant=out.constant.times(f);
      //  System.out.print("function times "+f+" = ");//System.out.println(out.print(false));
        return out;
    }

    public Monom diff(int k)//Differentiate by kth variable
    {

        int[]vf=vfactors.clone();if (vf[k]==0)return null;
        ComplexNumber c=constant.times(vf[k]);
        vf[k]--;
        return new Monom(c,vf);
    }
    public Monom times(ComplexNumber z)
    {
        return new Monom(constant.times(z),vfactors);
    }

    public static Sum degN(int n,int m)//n=degree, m=variable count
    {
        if(n==0)return new Sum(new Monom[] {new Monom(1, new int[] {0,0,0,0,0,0,})});
        int c=choose(n+m-1,m-1);

        int[] factors=new int[m];factors[0]=n;
        Monom[] out=new Monom[c];
        for(int i=0;i<c;i++)
        {
            out[i]=new Monom(1,factors.clone());
            int k=m-1;
            while(factors[k]==0)k--;
            if(k<m-1)
            {
                factors[k]--;factors[k+1]++;
            }
            else
            {
                int count=factors[k];
                factors[k]=0;
                while(factors[k]==0&&k>0)k--;
                factors[k]--;factors[k+1]+=count+1;
            }
        }
        return new Sum( out);
    }
    /*	public static Monom parse(String input)
        {
            String rest=input;
            while(!rest.isEmpty())
            {
                int star=rest.indexOf("*");
                //if(star==-1)
                String f=rest.substring(0,star);
            }

        }*/
    private static int choose(int n, int m)
    {
        if(n<m)return 0;
        int c=1;
        for(int i=0;i<m;i++)
        {
            c*=(n-i);c/=(i+1);
        }
        return c;
    }
    public Monom copy()
    {
        Monom out=new Monom(constant,vfactors.clone());
        return out;
    }

    public static Function parse(String f)
    {
        int[]occ=new int[variables.length];
        int min=-1;
        for(int i=0;i<occ.length;i++)
        {
            occ[i]=f.indexOf(variables[i]);
            if(min==-1)min=occ[i];
        }
        if(min==-1)return Monom.one.times(Integer.parseInt(f));

        return new Monom(Integer.parseInt(f.substring(0,min)),new int[]{});
    }

    @Override
    public Function simplify() {
        if(equals(zero))
        return null;

        return this;
    }

}