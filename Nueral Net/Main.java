import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A class used to anaylse skate trick data based on training data.
 * 
 * @author (Ryan Baker) 
 * @version (1.0)
 */
public class Main
{
    // Parameters for Different Matrcies
    
    private int cols = 3;
    private int rowCount = 0;
    private int tricksLearned;
    
    //Decleration of skate trick example files (1. Ollie, 2. Kickflip, 3. Heelflip, 4. Manual)
    File kickflipFile;
    File heelflipFile;
    File manualFile;
    File noseManualFile;
    File ollieFile;
    File dataFile;
    
    //File Readers
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    DataInputStream dis = null;
    
    //Extracted readings from file
    private String [] kickflipTrain;
    private String [] heelflipTrain;
    private String [] manualTrain;
    private String [] ollieTrain;
    
    private String [] dataRead;
    
    //Manipulated readings (A matrix saved as [roll] [pitch] [heading] x number of results in file).
    private double [][] kickFlip;
    private double [][] heelFlip;
    private double [][] manual;
    private double [][] ollie;
    
    private double [][] dataTrain;
    
    //private File [] files;
    
    //Used during splitting process.
    private double [] tempA;
    private double [] tempB;
    private double [] tempC;

    //Counts used for number of times a trick has been performed. 

    private int manualCount;
    private int noseManualCount;
    private int heelflipCount;
    private int kickflipCount;
    private int ollieCount;
    private int popShuvitCount;
    private int ollie180Count;
    
    private int performed180;

    

    /**
     * Constructor for objects of class Main
     */
    
    public Main()
    {   
        //Load Files
        
        manualCount = 0;
        heelflipCount = 0;
        kickflipCount = 0;
        noseManualCount = 0;
        popShuvitCount = 0;
        
        
        dataFile = new File("C:\\Skate Tricks\\file.txt");
        
    }
    
    // TRAINING FUNCTIONS
    
    public void getFileRowNumbers(File file){
        
        File tempFile = file;        

        try
        {
            fis = new FileInputStream(tempFile);
            
            //Speeds up reading file
            
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            
            while(dis.available() != 0)
            {
                dis.readLine();
                rowCount++;
            }
            
            System.out.println("Row Count is: " + rowCount);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void loadDataFile(File file){
        
        File tempFile = file;
        getFileRowNumbers(tempFile);
        dataRead = new String [rowCount];
        
        try
        {
            fis = new FileInputStream(tempFile);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            
            while(dis.available() != 0)
            {
                for (int i = 0; i < rowCount; i++)
                {
                    String line = dis.readLine();
                    dataRead[i] = line;
                    //System.out.println(line);
            
                }   
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        
        tempA = new double[rowCount];
        tempB = new double[rowCount];
        tempC = new double[rowCount];
        
        for(int i=0; i < rowCount; i ++)
        {
            String line = dataRead[i];
            String[] readings = line.split(Pattern.quote(","));
            String roll = readings[0];
            String pitch = readings[1];
            String heading = readings[2];
            
            
            //System.out.print(roll + ",");
            //System.out.print(pitch + ",");
            //System.out.println(heading);
            
            
            tempA[i] = Double.parseDouble(roll);
            tempB[i] = Double.parseDouble(pitch);
            tempC[i] = Double.parseDouble(heading);
            
        }
        
        dataTrain = new double[rowCount][cols];
        
        for(int i=0; i < rowCount; i++)
        {
            dataTrain[i][0] = tempA[i];
            dataTrain[i][1] = tempB[i];
            dataTrain[i][2] = tempC[i];
        }
        
        rowCount = 0;
        System.out.println(dataRead[0]);
        
    }
    
    public void trainSystem()
    {
        
        loadDataFile(dataFile);
    
    }
    
    //ANAYLSING FUNCTIONS
    
    /* 
     * Function - checkForManual() 
     * 
     * This function is used to find out whether a manual has been performed. 
     * 
     * 
     * 
     * 1. Check that roll value has gone over 10 and that pitch value has gone over 5 a manual has been performed. 
     * 
     * 2. Count how many times the readings have gone over this if that is above 20 then manual has been performed. 
     * 
     */
    
    public void checkForManual(){
        int count = 0;
        int highCount = 0;
        double rollBound = dataTrain[0][0] - 25.00;
        boolean manualState = false;
        
        for(int i = 0; i < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            double pitch = dataTrain[i][1];

            if(roll < rollBound)
            {
                highCount++;
                manualState = true;
            }
            
            if(manualState == true && highCount > 100 && roll > rollBound)
            {
                manualState = false;
                manualCount++;
                highCount = 0;
                count = 0;
            }
        }
        
        System.out.println("Manual Count is: " + manualCount);
        //System.out.println("Count is: " + count);
        //System.out.println("High Count is: " + highCount);
        
    }
    
    public void checkForNoseManual(){
        int count = 0;
        int highCount = 0;
        double rollBound = dataTrain[0][0] + 25.00;
        boolean noseManualState = false;
        
        for(int i = 0; i < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            double pitch = dataTrain[i][1];

            if(roll > rollBound)
            {
                highCount++;
                noseManualState = true;
            }
            
            if(noseManualState == true && highCount > 100 && roll < rollBound)
            {
                noseManualState = false;
                noseManualCount++;
                highCount = 0;
                count = 0;
            }
        }
        
        System.out.println("Nose Manual Count is: " + noseManualCount);
        
    }
    
    public void checkForOllie(){
        int count = 0;
        double rollBound = dataTrain[0][0] - 30.00;
        
        for(int i = 0; i  < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            
            if(roll < rollBound)
            {
                count++;
            }
            
            if(count > 5)
            {
                ollieCount++;
                count = 0;
            }
            
        }
        
        System.out.println("Ollie Count is :" + ollieCount);
    }
    
    public void checkforOllie180(){
        int count = 0;
        double rollBound = dataTrain[0][0] + 35.00;
        
        double headingBound = dataTrain[0][2] - 100.00;
        double headingBound180 = dataTrain[0][2] + 100.00;
        
        boolean phase1 = false;
        
        for(int i = 0; i < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            double heading = dataTrain[i][2];
            
            if(roll > rollBound){
                count++;
                if(count > 5){
                    phase1 = true;
                }
            }
            
            if(phase1 && heading < headingBound || heading > headingBound180){
                performed180++;
                phase1 = false;
                count = 0;
                ollie180Count++;
                
            }
        }
        
        
        System.out.println("Ollie 180 Count is: " + ollie180Count);
    }
    

    public void checkForHeelflip(){
        int count = 0;
        
        //Variables for Phase 1
        boolean phase1 = false;
        double phase1RollBound = dataTrain[0][0] + 60.00;
        double phase1PitchBound = dataTrain[0][1] - 50.00;
        int heelPhase1Count = 0;
        
        //Variables for Phase 2
        boolean phase2 = false;
        double phase2RollBound = dataTrain[0][0] - 60.00;
        double phase2PitchBound = dataTrain[0][1] + 50.00;
        int heelPhase2Count = 0;

        for(int i = 0; i < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            double pitch = dataTrain[i][1];
            
            if(pitch < phase1PitchBound){
                heelPhase1Count++;
                
                if(heelPhase1Count > 5){
                    phase1 = true;
                }
            }
            
            if(phase1){
                count++;
                if(count > 40){
                    count = 0;
                    heelPhase1Count = 0;
                    phase1 = false;
                }
            }
            
            if(phase1 == true && pitch > phase2PitchBound){
                heelPhase2Count++;
                
                if(heelPhase2Count > 5 && count < 40){
                    heelPhase1Count = 0;
                    phase1 = false;
                    phase2 = true;
                }
            }
            
            if(phase2){
                heelflipCount++;
                phase2 = false;
                heelPhase2Count = 0;
                count = 0;
            }
             
        }
        
        if(phase1){
            System.out.println("Phase 1 True");
        }
        
        //System.out.println("Phase 1 Count: " + heelPhase1Count);
        //System.out.println("Phase 2 Count: " + heelPhase2Count);
        //System.out.println("Count: " + count);
        System.out.println("Heelflip Count is: " + heelflipCount);
    }
    
    public void checkForKickflip(){
        int count = 0;
        
        //Variables for Phase 1
        boolean phase1 = false;
        double phase1RollBound = dataTrain[0][0] - 60.00;
        double phase1PitchBound = dataTrain[0][1] + 50.00;
        int kickPhase1Count = 0;
        
        //Variables for Phase 2
        boolean phase2 = false;
        double phase2RollBound = dataTrain[0][0] + 60.00;
        double phase2PitchBound = dataTrain[0][1] - 50.00;
        int kickPhase2Count = 0;

        for(int i = 0; i < dataTrain.length; i++)
        {
            double roll = dataTrain[i][0];
            double pitch = dataTrain[i][1];
            
            if(pitch > phase1PitchBound){
                kickPhase1Count++;
                
                if(kickPhase1Count > 5){
                   phase1 = true;
                    
                }
            }
            
            if(phase1){
                count++;
                if(count > 40){
                    count = 0;
                    kickPhase1Count = 0;
                    phase1 = false;
                }
            }
            
            if(phase1 == true && pitch < phase2PitchBound){
                kickPhase2Count++;
                
                
                if(kickPhase2Count > 5 && count < 40){
                    kickPhase1Count = 0;
                    phase1 = false;
                    phase2 = true;
                }
            }
            
            if(phase2){
                kickflipCount++;
                phase2 = false;
                kickPhase2Count = 0;
                count = 0;
            }
             
        }

        //System.out.println("Count: " + count);
        //System.out.println("Phase 1 Count: " + kickPhase1Count);
        //System.out.println("Phase 2 Count: " + kickPhase2Count);
        System.out.println("Kickflip Count is: " + kickflipCount);
    }
    
    public void checkForPopshuvit(){
        int count = 0;
        boolean phase1 = false;
        double headingBound = dataTrain[0][2] - 100.00;
        double headingBound180 = dataTrain[0][2] + 100.00;
        
        for(int i = 0; i < dataTrain.length; i++)
        {
            double heading = dataTrain[i][2];
            
            if(heading < headingBound || heading > headingBound180){
                count++;
            }
            
            if(count > 70){
                popShuvitCount++;
                count = 0;
            }
        }
        //System.out.println("Count is : " + count);
        System.out.println("Pop Shuv-It Count is: " + popShuvitCount); 
    }
    
    public void searchForTricks(){
        
        checkForNoseManual();
        checkForHeelflip();
        checkForKickflip();
        checkForPopshuvit();
        checkForManual();
        checkforOllie180();
        checkForOllie();
        
    }
    
    public void resetCounts(){
        
       manualCount = 0;
       heelflipCount = 0;
       kickflipCount = 0;
       popShuvitCount = 0;
       
    }
}
