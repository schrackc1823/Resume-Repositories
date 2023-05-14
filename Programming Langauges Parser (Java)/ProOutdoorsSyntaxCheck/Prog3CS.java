
/**
 * Collect the filename and parses and returns if the file is syntacticaly correct
 *
 * @author (Curtis Schrack)
 * @version (4/7/2022)
 */
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Prog3CS
{
    static String token;
    static Scanner file;
    /*
    ;***************************************************************************
    ;Start
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for start
    ;Example Usage: Start()
    ;Description: Checks to see if the file meets the ProOut syntax for START
    ;***************************************************************************
    */
    public static boolean Start(){
        //Check if passes PERSON
        System.out.print("Entering: Person\n");
        boolean match = Person();
        
        //Check if passes ADVENTURE
        if(match == true){
            System.out.print("Leaving: Person\n");
            System.out.print("Entering: Adventure\n");
            match = Adventure();
            return match;
        }else{
            return false;
        }
    }
    
    /*
    ;***************************************************************************
    ;Person
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for start
    ;Example Usage: Person()
    ;Description: Checks to see if the file meets the ProOut syntax for PERSON
    ;EBNF: PERSON -> CREATE [ADD_ATTRUBUTE] '.'
    ;BNF:PERSON -> CREATE PERSON1 '.'
    ;    PERSON1-> ADD_ATTRIBUTE | *
    :EBNF was easier because I didn't have to make a second function for Pearson1
    ;***************************************************************************
    */
    public static boolean Person(){
        //check if passes CREATE
        System.out.print("Entering Create\n");
        boolean check = Create();
        
        //check if passes ADD_ATTRUBUTE
        if(check){
            System.out.print("Leaving Create\n");
            System.out.print("Entering Person1\n");
            Person1();
        }else{
            return false;
        }
        if(!check)
            return false;
        System.out.print("Leaving Pearson1\n");
        
        //check if passes '.'
        System.out.print("Check token '.'\n");
        if(token.equals(".")){
            token = file.nextLine();
            return true;
        }else{
            return false;
        }
    }
    /*
    ;***************************************************************************
    ;Person1
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments:
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for start
    ;Example Usage: Person1()
    ;Description: Checks to see if the file meets the ProOut syntax for PERSON1
    ;EBNF: PERSON -> CREATE [ADD_ATTRUBUTE] '.'
    ;BNF:PERSON -> CREATE PERSON1 '.'
    ;    PERSON1-> ADD_ATTRIBUTE | *
    :EBNF was easier because I didn't have to make a second function for PERSON1
    ;***************************************************************************
    */
    public static void Person1(){
        boolean check = true;
        //Checks if it passes Add_Attribute
        while(check == true){
            System.out.print("Entering Add_Attrubute\n");
            check = Add_Attribute();
        }
        if(check)
            System.out.print("Leaving Add_Attrubute\n");
        return;
    }
    
    /*
    ;***************************************************************************
    ;Add_Attrubute
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments:
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for start
    ;Example Usage: Add_Attribute()
    ;Description: Checks to see if the file meets the ProOut syntax for ADD_ATTRIBUTE
    ;***************************************************************************
    */
    public  static boolean Add_Attribute(){
        //check if age or hair token is next
        System.out.print("Check token 'age' or 'hair'\n");
        if(token.equals("age")){
            token = file.nextLine();
            //check if parse Digit
            System.out.print("Entering Digits\n");
            if(Digits()){
                System.out.print("Leaving Digits\n");
                return true;
            }else{
                return false;
            }
        }else if(token.equals("hair")){
            token = file.nextLine();
            //check if parse Name
            System.out.print("Entering Name\n");
            if(Name()){
                System.out.print("Leaving Name\n");
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /*
    ;***************************************************************************
    ;Create
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for CREATE
    ;Example Usage: Create()
    ;Description: Checks to see if the file meets the ProOut syntax for CREATE
    ;***************************************************************************
    */
    public  static boolean Create(){
        //check if 'create' is the next token
        System.out.print("Check token 'create'\n");
        if(!token.equals("create"))
            return false;
        token = file.nextLine();
        
        //check if passes Project_Object
        System.out.print("Entering Person_Object\n");
        if(Person_Object()){
            System.out.print("Entering Person_Object\n");
            return true;
        }else
            return false;
    }
    
    /*
    ;***************************************************************************
    ;Person_Object
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for PERSON_OBJECT
    ;Example Usage: Personal_Object()
    ;Description: Checks to see if the file meets the ProOut syntax for PERSON_OBJECT
    ;***************************************************************************
    */
    public  static boolean Person_Object(){
        //Check if next token is 'person'
        System.out.print("Check token 'person'\n");
        if(!token.equals("person"))
            return false;
        token = file.nextLine();
        
        //check if passes Name
        System.out.print("Entering Name\n");
        if(Name()){
            System.out.print("Leaving Name\n");
            return true;
        }else
            return false;
    }
    
    /*
    ;***************************************************************************
    ;Name
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for NAME
    ;Example Usage: Name()
    ;Description: Checks to see if the file meets the ProOut syntax for NAME
    ;***************************************************************************
    */
    public  static boolean Name(){
        boolean check = true;
        //Check if passes Letter
        System.out.print("Entering Letter\n");
        if(!Letter(token, 0))
            return false;
        System.out.print("Leaving Letter\n");
        
        //parse through rest of Letter or Digit
        for(int i = 1; i < token.length() && check; i++){
            System.out.print("Entering Letter and Digit\n");
            if(Letter(token, i))
                System.out.print("Leaving Letter\n");
            else if(Digit(token, i))
                System.out.print("Leaving Digit\n");
            else
                check = false;
        }
        token = file.nextLine();
        return true;
    }
   
    /*
    ;***************************************************************************
    ;Adventure
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for ADVENTURE
    ;Example Usage: Adventure()
    ;Description: Checks to see if the file meets the ProOut syntax for ADVENTURE
    ;***************************************************************************
    */
    public  static boolean Adventure(){
        //check if adventure is the next token
        System.out.print("Check token 'adventure'\n");
        if(!token.equals("adventure"))
            return false;
        
        token = file.nextLine();
        
        //check if passes Hiking or Camping
        System.out.print("Entering Hiking or Camping\n");
        if(Hiking()){
            System.out.print("Leaving Hiking\n");
        }else if(Camping()){
            System.out.print("Leaving Camping\n");
        }else{
            return false;
        }
        
        System.out.print("Check token '.'\n");
        if(!token.equals("."))
            return false;
        
        return true;
    }
   
    /*
    ;***************************************************************************
    ;Hiking
    ;Programmer:Curtis Schrack
    ;Date Created:4/3/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for HIKING
    ;Example Usage: Hiking()
    ;Description: Checks to see if the file meets the ProOut syntax for HIKING
    ;***************************************************************************
    */
    public  static boolean Hiking(){
        //check if next token is 'hiking'
        System.out.print("Check token 'hiking'\n");
        if(!token.equals("hiking"))
            return false;
            
        token = file.nextLine();
        
        //check if passes Gear
        System.out.print("Entering Gear\n");
        if(!Gear())
            return false;
        System.out.print("Leaving Gear\n");
        
        //check if passes Hile_Trail
        System.out.print("Entering Hike_Trail\n");
        if(!Hike_Trail())
            return false;
        System.out.print("Leaving Hike_Trail\n");
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Gear
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line       
    ;            
    ;Returns: the value true if the file is syntaticly correct for GEAR
    ;Example Usage: Gear()
    ;Description: Checks to see if the file meets the ProOut syntax for GEAR
    ;***************************************************************************
    */
    public  static boolean Gear(){
        //check if token is 'gear'
        System.out.print("Check token 'gear'\n");
        if(!token.equals("gear"))
            return false;
        token = file.nextLine();
            
        //Check if passes Trail_Objects
        System.out.print("Entering Trail_Objects\n");
        if(Trail_Objects()){
            System.out.print("Leaving Trail_Objects\n");
            return true;
        }else{
            return false;
        }
    }
     
    /*
    ;***************************************************************************
    ;Trail_Objects
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for TRAIL_OBJECTS
    ;Example Usage: Trail_Objects()
    ;Description: Checks to see if the file meets the ProOut syntax for TRAIL_OBJECTS
    ;***************************************************************************
    */
    public  static boolean Trail_Objects(){
        //Check if passes first Trail_Object
        System.out.print("Entering Trail_Object\n");
        if(!Trail_Object())
            return false;
        System.out.print("Leaving Trail_Object\n");
        
        //Check for extra Trail_Objects
        System.out.print("Entering Trail_Object\n");
        while(Trail_Object()){
            System.out.print("Leaving Trail_Object\n");
            System.out.print("Entering Trail_Object\n");
        }
        return true;
    }
   
    /*
    ;***************************************************************************
    ;Trail_Object
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line  
    ;            
    ;Returns: the value true if the file is syntaticly correct for TRAIL_OBJECT
    ;Example Usage: Trail_Object()
    ;Description: Checks to see if the file meets the ProOut syntax for TRAIL_OBJECT
    ;***************************************************************************
    */
    public  static boolean Trail_Object(){
        //Check if passes Walking_Stick or Water_Bottle
        System.out.print("Entering Walking_Stick and Water_Bottle\n");
        if(Walking_Stick()){
            System.out.print("Leaving Walking_Stick\n");
            return true;
        }else if(Water_Bottle()){
            System.out.print("Leaving Water_Bottle\n");
            return true;
        }else{
            return false;
        }
    }
   
    /*
    ;***************************************************************************
    ;Walking_Stick
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line         
    ;            
    ;Returns: the value true if the file is syntaticly correct for WALKING_STICK
    ;Example Usage: Walking_Stick()
    ;Description: Checks to see if the file meets the ProOut syntax for WALKING_STICK
    ;***************************************************************************
    */
    public  static boolean Walking_Stick(){
        //check if token is 'walkingStick'
        System.out.print("Check token 'walkingStick'\n");
        if(token.equals("walkingStick")){
            token = file.nextLine();
            return true;
        }else
            return false;
    }
   
    /*
    ;***************************************************************************
    ;Water_Bottle
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for WATER_BOTTLE
    ;Example Usage: Water_Bottle()
    ;Description: Checks to see if the file meets the ProOut syntax for WATER_BOTTLE
    ;***************************************************************************
    */
    public  static boolean Water_Bottle(){
        //check if token is 'waterBottle'
        System.out.print("Check if token 'waterBottle'\n");
        if(token.equals("waterBottle")){
            token = file.nextLine();
            return true;
        }else{
            return false;
        }
    }
    
    /*
    ;***************************************************************************
    ;Hike_Trail
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for HIKE_TRAIL
    ;Example Usage: Hike_Trail()
    ;Description: Checks to see if the file meets the ProOut syntax for HIKE_TRAIL
    ;***************************************************************************
    */
    public  static boolean Hike_Trail(){
        //check if hike is next token
        System.out.print("Check if token 'hike'\n");
        if(!token.equals("hike"))
            return false;
        token = file.nextLine();
        
        //check if keystone linnRun WinniePalmerNatureReserve are next token
        System.out.print("Check token 'keystone' 'linnRun' or 'WinniePalmerNatureReserve'\n");
        if(token.equals("keystone")){   
        }else if (token.equals("linnRun")){
        }else if (token.equals("WinniePalmerNatureReserve")){
        }else{
            return false;
        }
        
        token = file.nextLine();
            return true;
    }
   
    /*
    ;***************************************************************************
    ;Camping
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for CAMPING
    ;Example Usage: Camping()
    ;Description: Checks to see if the file meets the ProOut syntax for CAMPING
    ;***************************************************************************
    */
    public  static boolean Camping(){
        //check if setup is next token
        System.out.print("Check token 'setup'\n");
        if(!token.equals("setup"))
            return false;
        token = file.nextLine();
        
        //check if passes Caming_Objects
        System.out.print("Entering Camping_Objects\n");
        if(!Camping_Objects())
            return false;
        System.out.print("Leaving Camping_Objects\n");
            
        //check if passes Caming_Objects
        System.out.print("Entering Days\n");
        if(!Days())
            return false;
        System.out.print("Leaving Days\n");
            
        //check if passes Activity
        System.out.print("Entering Activity\n");
        if(!Activity())
            return false;
        System.out.print("Leaving Activity\n");
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Camping_Objects
    ;Programmer:Curtis Schrack
    ;Date Created:4/4/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for CAMPING_OBJECTS
    ;Example Usage: Camping_Objects()
    ;Description: Checks to see if the file meets the ProOut syntax for CAMPING_OBJECTS
    ;***************************************************************************
    */
    public  static boolean Camping_Objects(){
        //check if passes first Camping_Object
        System.out.print("Entering Camping_Object\n");
        if(!Camping_Object())
            return false;
        System.out.print("Leaving Camping_Object\n");
            
        //check if more Camping_Objects
        System.out.print("Entering Camping_Object\n");
        while(Camping_Object()){
            System.out.print("Leaving Camping_Object\n");
            System.out.print("Entering Camping_Object\n");
        }
        
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Camping_Object
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for CAMPING_OBJECT
    ;Example Usage: Camping_Object()
    ;Description: Checks to see if the file meets the ProOut syntax for CAMPING_OBJECT
    ;***************************************************************************
    */
    public  static boolean Camping_Object(){
        //if it passes Tent
        System.out.print("Entering Tent\n");
        if(Tent()){
            System.out.print("Leaving Tent\n");
            return true;
        }
            
        //if it passes Fire
        System.out.print("Entering Fire\n");
        if(Fire()){
            System.out.print("Leaving Fire\n");
            return true;
        }
        
        //if it passes Insect_Spray
        System.out.print("Entering Insect_Spray\n");
        if(Insect_Spray()){
            System.out.print("Leaving Insect_Spray\n");
            return true;
        }else
            return false;
    }
    
    /*
    ;***************************************************************************
    ;Tent
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments:
    ;           
    ;            
    ;Returns: the value true if the file is syntaticly correct for TENT
    ;Example Usage: Tent()
    ;Description: Checks to see if the file meets the ProOut syntax for TENT
    ;***************************************************************************
    */
    public  static boolean Tent(){
        //check token is 'tent'
        System.out.print("Check token 'tent'\n");
        if(!token.equals("tent"))
            return false;
            
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Fire
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for FIRE
    ;Example Usage: Fire()
    ;Description: Checks to see if the file meets the ProOut syntax for FIRE
    ;***************************************************************************
    */
    public  static boolean Fire(){
        //check token is 'fire'
        System.out.print("Check token 'fire'\n");
        if(!token.equals("fire"))
            return false;
            
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Insect_Spray
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for INSECT_SPRAY
    ;Example Usage: Insect_Spray()
    ;Description: Checks to see if the file meets the ProOut syntax for INSECT_SPRAY
    ;***************************************************************************
    */
    public  static boolean Insect_Spray(){
        //check token is 'insectspray'
        System.out.print("Check token 'insectSpray'\n");
        if(!token.equals("insectSpray"))
            return false;
            
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Days
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for DAYS
    ;Example Usage: Days()
    ;Description: Checks to see if the file meets the ProOut syntax for DAYS
    ;***************************************************************************
    */
    public  static boolean Days(){
        //check if token is 'days'
        System.out.print("Check token 'days'\n");
        if(!token.equals("days")){
            return false;
        }
        token = file.nextLine();
        
        //check if passes Digit
        System.out.print("Entering Digit\n");
        if(!Digits())
            return false;
        
        System.out.print("Leaving Digit\n");
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Digits
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for DIGITS
    ;Example Usage: Digits()
    ;Description: Checks to see if the file meets the ProOut syntax for DIGITS
    ;***************************************************************************
    */
    public  static boolean Digits(){
        //check if has extra Digits
        System.out.print("Entering Digit\n");
        if(!Digit(token, 0)){
            return false;    
        }
        System.out.print("Leaving Digit\n");
        
        //check if has extra Digits
        for(int i = 0; i < token.length(); i++){
            System.out.print("Entering Digit\n");
            if(!Digit(token, i))
                return false;
            System.out.print("Leaving Digit\n");
        }
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Letter
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/5/2022
    ;
    ;Arguments:
    ;           ref - reference number to what char to view in token
    ;            
    ;Returns: the value true if it is a number
    ;Example Usage: Digit()
    ;Description: Checks to see if value is a number and return true if so otherwise false
    ;***************************************************************************
    */
    public  static boolean Letter(String token, int ref){
        //check if token is a letter
        System.out.print("Check token is letter\n");
        switch(token.charAt(ref)){
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z': return true;
            default: return false;
        }
    }
    
    /*
    ;***************************************************************************
    ;Digit
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/5/2022
    ;
    ;Arguments:
    ;           ref - reference number to what char to view in token
    ;            
    ;Returns: the value true if it is a number
    ;Example Usage: Digit()
    ;Description: Checks to see if value is a number and return true if so otherwise false
    ;***************************************************************************
    */
    public  static boolean Digit(String token, int ref){
        //check if token is numeric
        System.out.print("Check token is numeric\n");
        switch(token.charAt(ref)){
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': return true;
            default: return false;
        }
    }
     
    /*
    ;***************************************************************************
    ;Activity
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Old Rule:ACTIVITY -> ACTIVITY 'activity' (STAR_GAZING | FISHING)
    ;New Rule: ACTIVITY -> 'activity' (STAR_GAZING | FISHING) ACTIVITY`
    ;          ACTIVITY` -> 'activity' (STAR_GAZING | FISHING) ACTIVITY` | *
    ;Returns: the value true if the file is syntaticly correct for ACTIVITY
    ;Example Usage: Activity()
    ;Description: Checks to see if the file meets the ProOut syntax for ACTIVITY
    ;***************************************************************************
    */
    public  static boolean Activity(){
        //check if token is 'activity'
        System.out.print("Check token 'activity'\n");
        if(!token.equals("activity"))
            return false;
        
        token = file.nextLine();
        //check if passes Start_Gazing or Fishing
        System.out.print("Entering Star_Gazing or Fishing\n");
        if(Star_Gazing()){
            System.out.print("Leaving Star_Gazing\n");
        }else if(Fishing()){
            System.out.print("Leaving Fishing\n");
        }else{
            return false;
        }
        
        //check for extra activaties
        System.out.print("Entering Activity1\n");
        if(Activity1()){
            System.out.print("Leaving Activity1\n");
            return true;
        }
        return false;
    }
    
    /*
    ;***************************************************************************
    ;Activity1
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Old Rule:ACTIVITY -> ACTIVITY 'activity' (STAR_GAZING | FISHING)
    ;New Rule: ACTIVITY -> 'activity' (STAR_GAZING | FISHING) ACTIVITY1
    ;          ACTIVITY1 -> 'activity' (STAR_GAZING | FISHING) ACTIVITY1 | *
    ;Returns: the value true if the file is syntaticly correct for ACTIVITY1
    ;Example Usage: Activity1()
    ;Description: Checks to see if the file meets the ProOut syntax for ACTIVITY1
    ;***************************************************************************
    */
    public  static boolean Activity1(){
        //check if token is 'activity'
        System.out.print("Check token 'activity'\n");
        if(!token.equals("activity"))
            return true;
        
        token = file.nextLine();
        //check if passes Start_Gazing or Fishing
        System.out.print("Entering Star_Gazing or Fishing\n");
        if(Star_Gazing()){
            System.out.print("Leaving Star_Gazing\n");
        }else if(Fishing()){
            System.out.print("Leaving Fishing\n");
        }else{
            return false;
        }
        
        //check for extra activaties
        System.out.print("Entering Activity1\n");
        if(Activity1())
            System.out.print("Leaving Activity1\n");
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Star_Gazing
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for STAR_GAZING
    ;Example Usage: Star_Gazing()
    ;Description: Checks to see if the file meets the ProOut syntax for STAR_GAZING
    ;***************************************************************************
    */
    public  static boolean Star_Gazing(){
        //check if token is starGazing
        System.out.print("Check token 'starGazing'\n");
        if(!token.equals("starGazing"))
            return false;
        
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Fishing
    ;Programmer:Curtis Schrack
    ;Date Created:4/5/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: token - next value token value in the file
    ;           file - contains remainder of tokens on each line
    ;            
    ;Returns: the value true if the file is syntaticly correct for FISHING
    ;Example Usage: Fishing()
    ;Description: Checks to see if the file meets the ProOut syntax for FISHING
    ;***************************************************************************
    */
    public  static boolean Fishing(){
        //check if token is fishing
        System.out.print("Check token 'fishing'\n");
        if(!token.equals("fishing"))
            return false;
            
        token = file.nextLine();
        return true;
    }
    
    /*
    ;***************************************************************************
    ;Main
    ;Programmer:Curtis Schrack
    ;Date Created:4/2/2022
    ;Last Modified:4/6/2022
    ;
    ;Arguments: 
    ;            
    ;Returns:
    ;Example Usage: main()
    ;Description: Opens a file then checks the text in the file to see if it passes the ProOut syntax
    ;***************************************************************************
    */
    public static void main()
    {
        //Get input file name
        Scanner in = new Scanner(System.in);
        System.out.print("Enter name of input file name: ");
        String fileName = in.nextLine();
        
        //Create input file
        try{
            file = new Scanner(new File(fileName));
        }
        catch(FileNotFoundException e){
            System.out.println("Error opening the file " + fileName);
            System.exit(0);
        }
        
        //Check Syntax
        if(!file.hasNext()){
            System.out.print("Nothing in the file");
            return;
        }
        
        
        //Check file is syntacically correct
        boolean syntax = true;
        do{
        token = file.nextLine();
        System.out.print("Entering function: Start\n");
        syntax = Start();
        }while(file.hasNext() && syntax == true);
        
        //Output result
        if(syntax == true){
            System.out.println("The " + fileName + " has been parsed and is sytacticly correct\n");
        }else{
            System.out.print("The " + fileName + " has been parsed and is sytacticly incorrect\n");
        }
    }
}