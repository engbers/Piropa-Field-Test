package com.piropa_field_test;

import android.os.Environment;
import android.content.Context;
import android.util.Log;

import com.piropa_field_test.DataSave;
import com.piropa_field_test.data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Export {
	FileWriter writer;
	FileReader reader;
	BufferedReader br;
	File file;
	
	@SuppressWarnings("deprecation")
	public void exp (DatabaseHandler db, List<DataSave> PiropaList, int mode){		//mode 0 = GPS, mode 1 = DHDN3, mode 2 = Piropa
		
		final File dir;
		
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //handle case of no SDCARD present
        } else {
            dir = new File(Environment.getExternalStorageDirectory()
                 +File.separator
                 +"PiropaTest"); //file name
            
            dir.mkdirs(); //create folders where write files

    		
    		try{
    			int i=0;
    			while(i<PiropaList.size())
    			{
    				DataSave zwischen = PiropaList.get(i);
    				Date datum = new Date();
    				if (mode==2){
    					file = new File(dir,"Piropa: " + String.valueOf(zwischen.getLac())+"; "+String.valueOf(zwischen.getCid())+ ";" +datum.getHours() + ":"+datum.getMinutes()+".txt");
    				}else if (mode==1){
    					file = new File(dir,"DHDN3: " + String.valueOf(zwischen.getLac())+"; "+String.valueOf(zwischen.getCid())+ ";" +datum.getHours() + ":"+datum.getMinutes()+".txt");
    				}else{
    					file = new File(dir,"GPS: " + String.valueOf(zwischen.getLac())+"; "+String.valueOf(zwischen.getCid())+ ";" +datum.getHours() + ":"+datum.getMinutes()+".txt");
    				}
    				writer = new FileWriter(file,true);
    				reader = new FileReader(file);
    				double[] koordinaten ={Double.parseDouble(zwischen.getLatitude()),Double.parseDouble(zwischen.getLongitude())};
    				if (mode!=0) koordinaten=umrechnung(koordinaten[0],koordinaten[1]);
    				if (mode==2){
    					
    					
    					br = new BufferedReader(reader);
    					String zeile = br.readLine();
    				      int j=1;
    				      
    				      while(zeile!= null){
    				    	  j++; 
    				    	  zeile= br.readLine();
    				      }
    				br.close();   
    				writer.write("" + String.valueOf(j)+ " ");
    				writer.write("" + String.valueOf(koordinaten[0])+ " ");
    				writer.write("" + String.valueOf(koordinaten[1]));
    				} else {
    				writer.write("" + String.valueOf(koordinaten[0]));
    				writer.write(";	");
    				writer.write("" + String.valueOf(koordinaten[1]));
    				writer.write(";	");
    				writer.write("" + zwischen.getStrength());
    				}
    				writer.write("\r\n");
    		//		db.deleteTestPunkt(PiropaList.get(i));
    				writer.flush();
    				i++;
    				
    			}
    			
    			
    			writer.flush();
    			writer.close();
    			
    		}
    		catch(IOException ex){
    		
    			ex.printStackTrace();
    		}
        
        
        }
		
		

		
	}
	
	public double[] umrechnung(double lat, double lon){
		double PI=Math.PI;
		double[][] Tabelle1 = new double [3][22];
        double[][] Tabelle2 = new double [7][13];
        double[][] Tabelle3 = new double [10][27];
        double[][] Tabelle4 = new double [10][10];
        double[][] Tabelle5 = new double [6][17];
        double[][] Tabelle6 = new double [6][28];
        
        Tabelle1[2][17]=lat;
        Tabelle1[2][18]=lon;
        //Tabelle 2:
        Tabelle2[2][3]=Tabelle1[2][17];
        Tabelle2[2][4]=Tabelle1[2][18];
        Tabelle2[2][5]=Tabelle1[2][11];
        Tabelle2[6][3]= 6378137;
        Tabelle2[6][4]= 6356752.314;
        Tabelle2[6][5]= (Math.pow(Tabelle2[6][3],2)-Math.pow(Tabelle2[6][4],2))/Math.pow(Tabelle2[6][3],2);
        Tabelle2[2][7]= Tabelle2[6][3]/Math.sqrt(1-Tabelle2[6][5]*Math.pow(Math.sin(Tabelle2[2][3]/180*PI),2));
        Tabelle2[2][10]= (Tabelle2[2][7]+Tabelle2[2][5])*Math.cos(Tabelle2[2][3]/180*PI)*Math.cos(Tabelle2[2][4]/180*PI);
        Tabelle2[2][11]= (Tabelle2[2][7]+Tabelle2[2][5])*Math.cos(Tabelle2[2][3]/180*PI)*Math.sin(Tabelle2[2][4]/180*PI);
        Tabelle2[2][12]= ((Tabelle2[2][7]*Math.pow(Tabelle2[6][4],2))/Math.pow(Tabelle2[6][3],2) + Tabelle2[2][5])*Math.sin(Tabelle2[2][3]/180*PI);
        
        
        //Tabelle 4:
        Tabelle4[3][4] = 582;
        Tabelle4[3][5] = 598.1;
        Tabelle4[3][6] = 584.8;
        Tabelle4[3][7] = 590.5;
        Tabelle4[3][8] = 597.1;
        
        Tabelle4[4][4] = 105;
        Tabelle4[4][5] = 73.7;
        Tabelle4[4][6] = 67;
        Tabelle4[4][7] = 69.5;
        Tabelle4[4][8] = 71.4;
        
        Tabelle4[5][4] = 414;
        Tabelle4[5][5] = 418.2;
        Tabelle4[5][6] = 400.3;
        Tabelle4[5][7] = 411.6;
        Tabelle4[5][8] = 412.1;
        
        Tabelle4[6][4] = -1.04;
        Tabelle4[6][5] = -0.202;
        Tabelle4[6][6] = -0.105;
        Tabelle4[6][7] = 0.796;
        Tabelle4[6][8] = -0.894;
        
        Tabelle4[7][4] = -0.35;
        Tabelle4[7][5] = -0.045;
        Tabelle4[7][6] = -0.013;
        Tabelle4[7][7] = 0.052;
        Tabelle4[7][8] = -0.068;
        
        Tabelle4[8][4] = 3.080;
        Tabelle4[8][5] = 2.455;
        Tabelle4[8][6] = 2.378;
        Tabelle4[8][7] = 3.601;
        Tabelle4[8][8] = 1.563;
        
        Tabelle4[9][4] = 8.3;
        Tabelle4[9][5] = 6.7;
        Tabelle4[9][6] = 10.29;
        Tabelle4[9][7] = 8.3;
        Tabelle4[9][8] = 7.58;
        Tabelle4[9][9] = 0.000001;
        
        
        //Tabelle 3:
        Tabelle3[1][3]=Tabelle2[2][10];
        Tabelle3[1][4]=Tabelle2[2][11];
        Tabelle3[1][5]=Tabelle2[2][12];   
        
        //Drehmatrix
        Tabelle3[1][8] = 1;
        Tabelle3[1][9] = 0.00001153;
        Tabelle3[1][10]= 0.00000006;
        Tabelle3[2][8] = -0.00001153;
        Tabelle3[2][9] = 1;
        Tabelle3[2][10]= -0.00000051;
        Tabelle3[3][8] = -0.00000006;
        Tabelle3[3][9] = 0.00000051;
        Tabelle3[3][10]= 1;
        
        //Für Parametersatz 3
        Tabelle3[5][2] = -584.8;
        Tabelle3[5][3] = -67;
        Tabelle3[5][4] = -400.3;
        Tabelle3[5][5] = 0.105;
        Tabelle3[5][6] = 0.013;
        Tabelle3[5][7] = -2.378;
        Tabelle3[5][8] = -10.3;
        
        double[][] Mat4 = new double[3][3];
        double[] Mat5 = new double[3];
        double[] Mat6 = new double [3];
        
        for(int counter1 = 0; counter1 < 3; counter1++)
        {
          for(int counter2 = 0; counter2 < 3; counter2++)
          {
            Mat4[counter1][counter2] = Tabelle3[1+counter1][8+counter2];
          }
        }
        for(int counter3 = 0; counter3 < 3; counter3++)
        {
          
          Mat5[counter3] = Tabelle3[5][2+counter3];
          
        }
        
        for(int counter4 = 0; counter4 < 3; counter4++)
        {
          Mat6[counter4]=0;
          for(int j = 0; j < 3; j++)
          {
            
              Mat6[counter4] = Mat6[counter4] + Mat4[j][counter4] * Mat5[j];
            
          }
        }
        
        Tabelle3[6][8] = 0.99998971;
        Tabelle3[6][2] = Mat6[0]*Tabelle3[6][8];
        Tabelle3[6][3] = Mat6[1]*Tabelle3[6][8];
        Tabelle3[6][4] = Mat6[2]*Tabelle3[6][8];
        Tabelle3[6][5] = 0.00000051;
        Tabelle3[6][6] = 0.00000006;
        Tabelle3[6][7] = -0.00001153;
        
        
        Tabelle3[8][2] = Tabelle3[6][2];
        Tabelle3[8][3] = Tabelle3[6][3];
        Tabelle3[8][4] = Tabelle3[6][4];
        Tabelle3[8][5] = Tabelle3[5][5];
        Tabelle3[8][6] = Tabelle3[5][6];
        Tabelle3[8][7] = Tabelle3[5][7];
        Tabelle3[8][8] = Tabelle3[5][8];
        
        //Rotierte Vektoren
        double[][] Mat1 = new double[3][3];
        double[] Mat2 = new double[3];
        double[] Mat3 = new double [3];
        
        for(int counter1 = 0; counter1 < 3; counter1++)
        {
          for(int counter2 = 0; counter2 < 3; counter2++)
          {
            Mat1[counter1][counter2] = Tabelle3[1+counter1][8+counter2];
          }
        }
        for(int counter3 = 0; counter3 < 3; counter3++)
        {
          
          Mat2[counter3] = Tabelle3[1][3+counter3];
          
        }
        
        for(int counter4 = 0; counter4 < 3; counter4++)
        {
          Mat3[counter4]=0;
          for(int j = 0; j < 3; j++)
          {
            
              Mat3[counter4] = Mat3[counter4] + Mat1[j][counter4] * Mat2[j];
            
          }
        }
        
        Tabelle3[1][13]=Mat3[0];
        Tabelle3[1][14]=Mat3[1];
        Tabelle3[1][15]=Mat3[2];
        
        //Maßstab berücksichtigen
        Tabelle3[1][18]=Tabelle3[1][13]*Tabelle3[6][8];
        Tabelle3[1][19]=Tabelle3[1][14]*Tabelle3[6][8];
        Tabelle3[1][20]=Tabelle3[1][15]*Tabelle3[6][8];
        
        //Translation anbringen
        Tabelle3[1][23]=Tabelle3[1][18] + Tabelle3[8][2];
        Tabelle3[1][24]=Tabelle3[1][19] + Tabelle3[8][3];
        Tabelle3[1][25]=Tabelle3[1][20] + Tabelle3[8][4];
        
        //Tabelle 5
        Tabelle5[2][3] = Tabelle3[1][23];
        Tabelle5[2][4] = Tabelle3[1][24];
        Tabelle5[2][5] = Tabelle3[1][25];
        
        //Bessel Ellipsoid
        Tabelle5[5][3] = 6377397.155;
        Tabelle5[5][4] = 6356078.962;
        Tabelle5[5][5] = (Math.pow(Tabelle5[5][3], 2)-Math.pow(Tabelle5[5][4], 2))/Math.pow(Tabelle5[5][3], 2);
        
        Tabelle5[2][7] = Math.sqrt(Math.pow(Tabelle5[2][3],2)+Math.pow(Tabelle5[2][4],2));
        Tabelle5[2][8] = Math.atan(Tabelle5[2][5]*Tabelle5[5][3]/(Tabelle5[2][7]*Tabelle5[5][4]));
        Tabelle5[2][9] = Math.atan((Tabelle5[2][5]+Tabelle5[5][5]*Math.pow(Tabelle5[5][3],2)/Tabelle5[5][4]*Math.pow(Math.sin(Tabelle5[2][8]),3))/(Tabelle5[2][7]-Tabelle5[5][5]*Tabelle5[5][3]*Math.pow(Math.cos(Tabelle5[2][8]),3)));
        Tabelle5[2][10] = Math.atan(Tabelle5[2][4]/Tabelle5[2][3]);
        Tabelle5[2][11] = Tabelle5[5][3]/Math.sqrt(1-Tabelle5[5][5]*Math.pow(Math.sin(Tabelle5[2][9]),2));
        Tabelle5[2][12] = Tabelle5[2][7]/Math.cos(Tabelle5[2][9])-Tabelle5[2][11];
        
        Tabelle5[2][14] = Tabelle5[2][9]*180/PI;
        Tabelle5[2][15] = Tabelle5[2][10]*180/PI;
        Tabelle5[2][16] = Tabelle5[2][12];
        
        //Tabelle 6
        Tabelle6[2][3]=Tabelle5[2][14];
        Tabelle6[2][4]=Tabelle5[2][15]; 
        
        Tabelle6[2][7] = 9;
        Tabelle6[2][8] = (Tabelle6[2][4]-Tabelle6[2][7])*PI/180;
        Tabelle6[2][9] = Tabelle6[2][3]/180 * PI;
        
        //Bessel Elipsoid
        Tabelle6[5][3] = 6377397.155;
        Tabelle6[5][4] = 6356078.962;
        Tabelle6[5][5] = (Math.pow(Tabelle6[5][3],2)-Math.pow(Tabelle6[5][4],2))/Math.pow(Tabelle6[5][3],2);    
        
        Tabelle6[5][7] = (Tabelle6[5][3]-Tabelle6[5][4])/(Tabelle6[5][3]+Tabelle6[5][4]);
        Tabelle6[5][8] = (Tabelle6[5][3]+Tabelle6[5][4])/2*(1+0.25*Math.pow(Tabelle6[5][7],2)+1/64*Math.pow(Tabelle6[5][7],4));
        Tabelle6[5][9] = (0-1.5)*Tabelle6[5][7]+(0.5625)*Math.pow(Tabelle6[5][7],3)-((0.09375)*Math.pow(Tabelle6[5][7],5));
        Tabelle6[5][10]=0.9375*Math.pow(Tabelle6[5][7],2)-(0.46875*Math.pow(Tabelle6[5][7],4));
        Tabelle6[5][11]=(-0.72916666666667*Math.pow(Tabelle6[5][7],3)+0.41015625*Math.pow(Tabelle6[5][7],5));
        Tabelle6[5][12]=0.615234375*Math.pow(Tabelle6[5][7],4);
        
        Tabelle6[2][11]=Tabelle6[5][3]/Math.sqrt(1-Tabelle6[5][5]*Math.pow(Math.sin(Tabelle6[2][9]),2));
        Tabelle6[2][12]=Math.sqrt(Math.pow(Tabelle6[5][3],2)/Math.pow(Tabelle6[5][4],2)*Tabelle6[5][5]*Math.pow(Math.cos(Tabelle6[2][9]),2));
        Tabelle6[2][13]=Math.tan(Tabelle6[2][9]);
        //Bogenlänge:
        Tabelle6[2][15]=Tabelle6[5][8]*(Tabelle6[2][9]+Tabelle6[5][9]*Math.sin(2*Tabelle6[2][9])+Tabelle6[5][10]*Math.sin(4*Tabelle6[2][9])+Tabelle6[5][11]*Math.sin(6*Tabelle6[2][9])+Tabelle6[5][12]*Math.sin(8*Tabelle6[2][9]));
        
        Tabelle6[2][17]=Tabelle6[2][13]/2*Tabelle6[2][11]*Math.pow(Math.cos(Tabelle6[2][9]),2)*Math.pow(Tabelle6[2][8],2);
        Tabelle6[2][18]=Tabelle6[2][13]/24*Tabelle6[2][11]*Math.pow(Math.cos(Tabelle6[2][9]),4)*(5-Math.pow(Tabelle6[2][13],2)+9*Math.pow(Tabelle6[2][12],2)+4*Math.pow(Tabelle6[2][12],4))*Math.pow(Tabelle6[2][8],4);
        Tabelle6[2][19]=Tabelle6[2][13]/720*Tabelle6[2][11]*Math.pow(Math.cos(Tabelle6[2][9]),6)*(61-58*Math.pow(Tabelle6[2][13],2)+270*Math.pow(Tabelle6[2][12],2)-330*Math.pow(Tabelle6[2][13],2)*Math.pow(Tabelle6[2][12], 2))*Math.pow(Tabelle6[2][8],6);
        Tabelle6[2][20]=Tabelle6[2][13]/40320*Tabelle6[2][11]*Math.pow(Math.cos(Tabelle6[2][9]),8)*(1385-3111*Math.pow(Tabelle6[2][13],2)+543*Math.pow(Tabelle6[2][13],4)-Math.pow(Tabelle6[2][13],6))*Math.pow(Tabelle6[2][8],8);
        //Hochwert:
        Tabelle6[2][21]= Tabelle6[2][15]+Tabelle6[2][17]+Tabelle6[2][18];
        
        Tabelle6[2][23]=Tabelle6[2][11]*Math.cos(Tabelle6[2][9])*Tabelle6[2][8];
        Tabelle6[2][24]=Tabelle6[2][11]/6*Math.pow(Math.cos(Tabelle6[2][9]),3)*(1-Math.pow(Tabelle6[2][13],2)+Math.pow(Tabelle6[2][12],2))*Math.pow(Tabelle6[2][8],3);
        //Rechtswert:
        Tabelle6[2][27]=Tabelle6[2][23]+Tabelle6[2][24]+500000+Tabelle6[2][7]/3*1000000;
        

        double[] koordinaten ={Tabelle6[2][27],Tabelle6[2][21]};
		
		return koordinaten;
	}
	
}
	
	
