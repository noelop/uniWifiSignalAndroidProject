package com.noelop.p_0721;

import com.noelop.p_0721.Circles.*;

/**
 * Created by noelop135899 on 2016/10/8.
 */

public class TPL {
    private double dA,dB,dC;
    private double A[],B[],C[] = new double[2];
    private double process[][] = new double[3][4];
    private double result[][] = new double[6][2];
    private double sect1[],sect2[],sect3[] = new double[2];

    public TPL(double[] a,double[] b,double[] c,double da, double db,double dc){
        A=a;B=b;C=c;dA=da;dB=db;dC=dc;
    }
    public double[] showTPL(){
        Circle circle1 = new Circle(A[0],A[1],dA);
        Circle circle2 = new Circle(B[0],B[1],dB);
        Circle circle3 = new Circle(C[0],C[1],dC);


        int l = 0;
        int m = 0;

        CirIntersect cirIntersect1= new CirIntersect(circle1,circle2);
        CirIntersect cirIntersect2= new CirIntersect(circle1,circle3);
        CirIntersect cirIntersect3= new CirIntersect(circle2,circle3);

        process[0]=cirIntersect1.intersect();
        process[1]=cirIntersect2.intersect();
        process[2]=cirIntersect3.intersect();

        for (int j=0;j<=5;j++){
            for (int k=0;k<=1;k++){
                result[j][k] = process[l][m];
                m++;
                if (m==4){
                    m=0;
                    l++;
                }
            }
        }

        Intersectdistance intersect1 = new Intersectdistance(process[2],A);
        Intersectdistance intersect2 = new Intersectdistance(process[1],B);
        Intersectdistance intersect3 = new Intersectdistance(process[0],C);

        sect1=intersect1.shortIntersect();
        sect2=intersect2.shortIntersect();
        sect3=intersect3.shortIntersect();

        System.err.println(process[0][0]+" "+process[0][1]+" "+process[0][2]+" "+process[0][3]);
        System.err.println(process[1][0]+" "+process[1][1]+" "+process[1][2]+" "+process[1][3]);
        System.err.println(process[2][0]+" "+process[2][1]+" "+process[2][2]+" "+process[2][3]);

        double X = (sect1[0]+sect2[0]+sect3[0])/3;
        double Y = (sect1[1]+sect2[1]+sect3[1])/3;
        return new double[]{X,Y};
    }

}
