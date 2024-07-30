import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.io.File;
import java.io.IOException;
public class NewtonFractal {
    static String colorSymmetry="v",
            type="png";
    static boolean lambdaSpace=false;
    static int fineness=9,
    steps=100;
    static double[] loc= {0,0};
    static int start=1,width=1080, height=1080,maxit=10, black=Color.black.getRGB() ,a0,b;//120
    static BufferedImage canvas=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
    static double zoom=0.5, accuracy=1*Math.pow(10, -3), factor=4.0/zoom/height,colorwidth=2, t=0;
    static String name0=""/*+zoom/*+" ,loc("+loc[0]+","+loc[1]+")"*/;
    static ComplexNumber c=ComplexNumber.zero,a=ComplexNumber.one,one=ComplexNumber.one,d,e,f,g;
    static DecimalFormat df=new DecimalFormat("0000");
    static Function function,derivative;
    static Monom z3=new Monom(one,new int[]{3,0,0}),
                 z3t=new Monom(one,new int[]{3,0,1}),
                z2k=new Monom(one, new int[]{2,1,0}),
                 zt=new Monom(one,new int[]{1,0,1}),
                z2=new Monom(one, new int[]{2,0,0}),
                zt2=new Monom(one,new int[]{1,0,2}),
                z2t=new Monom(one, new int[]{2,0,1}),
                zk2=new Monom(one, new int[]{1,2,0}),
                zkt=new Monom(one, new int[]{1,1,1}),
                zk=new Monom(one, new int[]{1,1,0}),
                zet=new Monom(one,new int[]{1,0,0}),
                cons=new Monom(one,new int[]{0,0,0}),
                kay=new Monom(one,new int[]{0,1,0}),
            k2t=new Monom(one,new int[]{0,2,1}),
            kt=new Monom(one,new int[]{0,1,1}),
            t2=new Monom(one,new int[]{0,0,2}),
            z2kt=new Monom(one,new int[]{2,1,1}),
                tee=new Monom(one, new int[]{0,0,1});
    public static void main(String[]args)
    {
       function =new Quotient(new Sum(new Function[]{new Func("sin",z2t),z3,kay,cons}),new Sum(new Function[]{kay,cons,new Func("sin",kay)}));
       derivative=function.diff(0);
       derivative=derivative.simplify();
       System.out.println(function.print(false));
       System.out.println(derivative.print(false));
       for(int i=0;i<steps;i++)
       {
           ComplexNumber t=new ComplexNumber(1.0*i/steps,0);
           ComplexNumber  tcirc=ComplexNumber.polar(1,2*Math.PI*0/steps);

           drawImage(name0+df.format(start+i),tcirc);

       }

    }



    public static int colormap(ComplexNumber z)
    {
        double factor=1;
        if(z.r<colorwidth&& (colorSymmetry.length()==1)) {factor=z.r*factor/colorwidth;}

        if(colorSymmetry.equals("v"))//sw:+++ rc:-++ gm:+-+ by: ++-
        {
            double a=Math.cos(z.phi)*Math.sqrt(3)-Math.sin(z.phi),b=2*Math.sin(z.phi),c=-Math.cos(z.phi)*Math.sqrt(3)-Math.sin(z.phi),norm=Math.max(Math.abs(c),Math.max(Math.abs(a),Math.abs(b)))/factor;
            int x=(int)Math.max(0, Math.min((128+128*a/norm), 255)),y=(int)Math.max(0, Math.min(128-128*b/norm,255)),w=(int)Math.max(0, Math.min(128-128*c/norm,255));
            return new Color(w,y,x).getRGB();
        }
        else if(colorSymmetry.equals("f"))//w where constant
        {
            double a=Math.cos(z.phi),b=Math.sin(z.phi),norm=Math.max(Math.abs(a),Math.abs(b))/factor;
            int x=(int)Math.max(0, Math.min((128+128*a/norm), 255)),y=(int)Math.max(0, Math.min(128+128*b/norm,255)),w=128;
            return new Color(w,y,x).getRGB();
        }
        else if(colorSymmetry.equals("e"))//x different from y,w
        {
            double a=Math.cos(z.phi), b=Math.sin(z.phi),norm=Math.max(Math.abs(a), Math.abs(b))/factor;
            int x=(int)Math.max(0, Math.min((128-128*a/norm), 255)),y=(int)Math.max(0, Math.min(128-128*b/norm,255)),w=(int)Math.max(0, Math.min(128+128*b/norm,255));
            return new Color(w,y,x).getRGB();
        }
        else if(colorSymmetry.equals("horizontal f"))
        {	//System.out.print(factor);
            //System.out.print("blip");
            int w=(int) Math.max(0, Math.min(255,128+z.y/colorwidth*factor)),x,y,edge=(int)(((z.x+1000*colorwidth)%colorwidth)/(colorwidth/4.0)),pos=(int)Math.max(0,Math.min(255,(((z.x+1000*colorwidth)%(colorwidth/4.0))/(colorwidth/4.0)-0.5)*factor*256+128));
            if(edge==0||edge==3)x=(int)(128-factor*127.9); else x=(int) (128*(1+factor*0.999999));
            if(edge<2)y=pos; else y=255-pos;
            //	System.out.println(edge+","+pos+","+x+", "+y);
            if (edge%2==0)return new Color(x,w,y).getRGB(); else return new Color(y,w,x).getRGB();
        }
        else if(colorSymmetry.equals("horizontal e"))
        {
            double a=z.x, b=z.y%colorwidth, norm=Math.max(Math.abs(a), Math.abs(b))/factor;
            int x=(int)Math.max(0, Math.min((128-128*a/norm), 255)),y=(int)Math.max(0, Math.min(128-128*b/norm,255)),w=(int)Math.max(0, Math.min(128+128*b/norm,255));
            return new Color(w,y,x).getRGB();
        }

        else return 0;
    }
    public static void drawImage(String name,ComplexNumber t)
    {
        ComplexNumber z0=new ComplexNumber(1000,0);
        for (int i=0;i<width;i++)
        {
            //System.out.print("line"+i);
            for (int j=0;j<height;j++)
            {
                z0=new ComplexNumber(1000,0);
                ComplexNumber kay=new ComplexNumber(loc[0]+factor*(i-width/2.0),loc[1]+factor*(j-height/2.0));
                ComplexNumber z=	ComplexNumber.zero;

                if(!lambdaSpace) z=kay;
                int l=0;
                ComplexNumber[]par= {z,kay,t};
                while(z.subtract(z0).norm()>accuracy&&l<maxit)
                {
                    z0=z.copy();
                    par[0]=z;
                    z=z.subtract(a.times(function.evaluate(par).divided(derivative.evaluate(par)))).add(c);
                    l++;
                }
                int col=colormap(z);
                canvas.setRGB(i,j,col);
            }
        }

        File outputfile = new File(name+"."+type);
        try {
            ImageIO.write(canvas, type, outputfile);
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        System.out.println();System.out.println(name+" finished.");
    }
}
