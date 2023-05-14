#Creator: Curtis Schrack
#Date Created: 2//15/2023
#Date Last Modified: 4/5/2023
#Description: Fills in RFQ previous quote information
#Reqirements: Line Item, Part Number and PO Released Qty fields filled in on RFQ File

#Imports:
import datetime
import sys
import numpy as np
import pandas as pd
import xlwings as xw
#May need to run 'pip install easygui' in anaconda
import tkinter as tk
from tkinter import ttk
import tkinter.messagebox
import tkinter.filedialog
import time


#Classes:
#Pull Previous Quote Logs and combines them into a collection of Dataframes
class QuoteLog:
     def __init__(self):
         #Data Information for Updating Logs
         date = ""
         #Previous Master Log
         try:
             date = open(/*Text File Save Location That Holds Date of Next Log Update*/, 'r')
             self.linesArchieve = pd.DataFrame()
             #Update Archieve on the first of each month
             updateDay = datetime.datetime.strptime(date.read(), '%y-%m-%d %H:%M:%S').strftime('%d')
             updateDay = int(updateDay)
             #Update Archive Pickle on the first of every Month incase archive has values added
             if (updateDay == 1 and int(datetime.date.today().strftime('%d')) < 2):
                 progress(progBar, 5)
                 self.linesArchive = updateArchive()
             else:
                 self.linesArchive = pd.read_pickle(/*File Save Location for Archive Pickle File*/)
                 progress(progBar, 25)
         except:
             progress(progBar, 5)
             self.linesArchive = updateArchive()
             
         self.linesArchiveQ = self.linesArchive[self.linesArchive['STATUS'] == 'Q'] 
         
         #Current Master Log
         try:
             self.lines = pd.DataFrame()
             updateDate = datetime.datetime.strptime(date.read(), '%y-%m-%d %H:%M:%S')
             #Update Master Log if current date is later than saved update date
             if (updateDate < datetime.datetime.now()):
                 updateMaster()
             else:
                 self.lines = pd.read_pickle(/*File Save Location for Current Log Pickle File*/)
                 progress(progBar, 30)
             date.close()
         except:
            self.lines  = updateMaster()
             
         self.linesQ = self.lines[self.lines['STATUS'] == 'Q']
         progress(progBar, 5)
         self.linesW = self.lines[self.lines['STATUS'] == 'W']
         progress(progBar, 5)
         
         #Append two excel files
         self.linesWArchiveQ = pd.concat([self.linesArchiveQ, self.linesQ])
         
         progress(progBar, 5)
 

#Functions:    
#Checks if there is a on going quote using the part number provided
def activelyQuotingQuote(partNum):
    #Find index of part number in the active quotes
    quoteNumIndex = qL.activeQuotes[qL.activeQuotes['ITEM'] == partNum].index

    #If a index is found matching the part number return it otherwise return None
    if len(quoteNumIndex) > 0:
        return qL.activeQuotes.iloc[quoteNumIndex[0],0]
    return "None"

#centering tkinter window
def centerTkinkerWindow(width, height, window):
    screen_width = window.winfo_screenwidth()  # Width of the screen
    screen_height = window.winfo_screenheight() # Height of the screen
     
    # Calculate Starting X and Y coordinates for Window
    x = (screen_width/2) - (width/2)
    y = (screen_height/2) - (height/2)
     
    window.geometry('%dx%d+%d+%d' % (width, height, x, y))
    return window
    

#Get a path to a specific file
def getWorkbookPath():
    root = tk.Tk()
    root.wm_attributes('-topmost', 1)
    root = centerTkinkerWindow(850, 500, root)
    root.withdraw()
    #Get File Name (change initaldir if you want to open in different location)
    path = tk.filedialog.askopenfilename(parent = root, initialdir=/*File Selected Starting Location on Local Device*/, title='Select A RFQ File', filetypes=(('Excel Macros-Enabled Workbook', '*.xlsm'), ('Excel Workbook', '*.xlsx')))
    #If No File Found
    if path == "":
        popupError("No Path Given", 'The path selected or not selected does not exist. Program will quit')
        sys.exit()
        
    root.destroy()
    return path

#Remove Non Necessary Lines
def removeUnneededRows(df):
     #Remove NRE Charges
     indexNRE = df.index[df['ITEM'].str.contains('NRE')]
     df.drop(indexNRE, inplace=True)
     
     #Remove Excess Material
     indexExcessMaterial = df.index[df['ITEM'].str.contains('EXCESS MATERIAL')]
     df.drop(indexExcessMaterial, inplace=True)
     
     #Remove Labor Charge
     indexLaborCharge = df.index[df['ITEM'].str.contains('LABOR')]
     df.drop(indexLaborCharge, inplace=True)
     
     #Remove Test
     indexTest = df.index[df['ITEM'].str.contains('TEST')]
     df.drop(indexTest, inplace=True)
     
     #Remove Cables
     indexCables = df.index[df['ITEM'] == 'CABLES']
     df.drop(indexCables, inplace=True)
     
     #Remove Carriers
     indexCarriers = df.index[df['ITEM'].str.contains('CARRIERS')]
     df.drop(indexCarriers, inplace=True)
     
#Creates a popup message explaining error
def popupError(title, body):
    root = tk.Tk()
    root.wm_attributes('-topmost', 1)
    root.eval('tk::PlaceWindow . center')
    root.withdraw()
    tk.messagebox.showerror(title, body)
    root.destroy()
    
#Creates a popup message explaining error
def popupInfo(title, body):
    root = tk.Tk()
    root.wm_attributes('-topmost', 1)
    root.eval('tk::PlaceWindow . center')
    root.withdraw()
    tk.messagebox.showinfo(title, body)
    root.destroy()

#Increase the Progress Bar
def progress(bar, amountIncrease):
    bar['value'] += amountIncrease
    # label
    value_label['text'] = update_progress_label(bar)
    root.update()
    
def progressBarCreation(winTitle):
    global root
    root = tk.Tk()
    root.wm_attributes('-topmost', 1)
    root.title(winTitle)
    root.geometry('320x120')
    #center window
    root = centerTkinkerWindow(320, 120, root)
    
    bar = ttk.Progressbar(root, orient='horizontal', length=300, mode='determinate', maximum=100)
    global value_label
    value_label = ttk.Label(root, text=update_progress_label(bar))
    
    # place the progressbars and progress percent label
    bar.grid(column=0, row=0, columnspan=2, padx=10, pady=10)
    value_label.grid(column=0, row=2, columnspan=3)

    root.update_idletasks()
    return bar

#Gives Progress Value in a Percentage
def update_progress_label(bar):
    if (bar['value'] == 5):
        return f"Current Progress: {bar['value']}%\nCurrently Getting Archive Log Quotes Might Take A While"
    return f"Current Progress: {bar['value']}%"

#Returns log
def updateLog():    
    #Get Combined log 
    df = QuoteLog()
    #DataFrame Cleanup
    df.linesWArchiveQ.reset_index(drop = True, inplace=True)
            
    ##Dataset Filtering and Sorting
    try:
        df.linesWArchiveQ["DATE QUOTED"] = df.linesWArchiveQ["DATE QUOTED"].astype('datetime64[ns]')
        df.linesWArchiveQ["BID QTY / EAU"] = df.linesWArchiveQ["BID QTY / EAU"].astype(str)
        df.linesWArchiveQ.drop(df.linesWArchiveQ.index[df.linesWArchiveQ['BID QTY / EAU'].str.contains('200+')], inplace=True)
        df.linesWArchiveQ.drop(df.linesWArchiveQ.index[df.linesWArchiveQ['BID QTY / EAU'].str.contains('-')], inplace=True)
        df.linesWArchiveQ['BID QTY / EAU']=df.linesWArchiveQ['BID QTY / EAU'].replace('nan',np.nan)
        df.linesWArchiveQ['BID QTY / EAU'] = pd.to_numeric(df.linesWArchiveQ['BID QTY / EAU'])
        progress(progBar, 10)
        df.linesWArchiveQ = df.linesWArchiveQ.sort_values(by=['DATE QUOTED', 'ESTIMATE #', 'ITEM', 'BID QTY / EAU'], ascending = [True, True, True, True])
        progress(progBar, 5)
        df.linesWArchiveQ.reset_index(drop = True, inplace=True)
        df.linesW.reset_index(drop = True, inplace=True)
        progress(progBar, 5)
    except:
        popupError("Log Error", "Master Log has a value in Tab Estimate Lines Column BID QTY / EAU. A completed quote value was found to not be a number. Check recent quoted quotes to confirm all values are numbers only. No commas or letter values are in this column.")
        sys.exit()
        
    #Remove last quote in linesW as they are probably in reference to the current RFQ
    removeValue = df.linesW.iloc[len(df.linesW)-1,0]
    df.linesW.drop(df.linesW.index[df.linesW['ESTIMATE #'] == removeValue], inplace=True)
    progress(progBar, 5)
    
    #Create Class to return quoted files and not quoted files
    class returnDF:
        quotedQuotes = df.linesWArchiveQ
        activeQuotes = df.linesW
    progress(progBar, 5)
    return returnDF

#Update Archive Log
def updateArchive():
    path = /*Archive File Location*/
    df = pd.read_excel(path, sheet_name='ESTIMATE LINES', usecols='A:V', header=2, engine='openpyxl')
    progress(progBar, 10)
    df = df[df['ESTIMATE #'].notna()]
    df = df.sort_values(by='DATE QUOTED')
    df['DATE QUOTED'] = df['DATE QUOTED'].dt.strftime('%m/%d/%Y')
    df['ITEM'] = df['ITEM'].apply(lambda x: str(x))
    removeUnneededRows(df)
    progress(progBar, 5)          
    # Create Dataset File for quick access
    df.to_pickle(/*File Save Location for Archive Pickle File*/)
    progress(progBar, 5)
    return df

#Update Master log file
def updateMaster():
     path = /*Current Log File Location*/
     df = pd.read_excel(path, sheet_name='ESTIMATE LINES', usecols='A:V', header=2, engine='openpyxl')
     progress(progBar, 10)
     df = df[df['ESTIMATE #'].notna()]
     df = df.drop(df.index[-1])
     for i in range(0,len(df)):
             if pd.notnull(df['DATE QUOTED'][i]):
                 df['DATE QUOTED'][i] = df['DATE QUOTED'][i].strftime('%m/%d/%Y')
     df['ITEM'] = df['ITEM'].apply(lambda x: str(x))
     progress(progBar, 5)
     removeUnneededRows(df)
     progress(progBar, 5)
     # Create Dataset File for quick access
     df.to_pickle(/*File Save Location for Current Log Pickle File*/)
     progress(progBar, 5)
     file = open(/*Log Update Dates Text File Location*/, 'w')
     updateDate = (datetime.datetime.now() + datetime.timedelta(days = 1)).strftime('%y-%m-%d %H:%M:%S')
     file.write(updateDate)
     file.close()
     progress(progBar, 5)
     return df


#MAIN
#Check if Log has been created and is recent. If not update log
try:
    date = open(/*Text File Save Location That Holds Date of Next Log Update*/, 'r')
    updateDate = datetime.datetime.strptime(date.read(), '%y-%m-%d %H:%M:%S')
    date.close()
    if (updateDate < datetime.datetime.now()):
        print("Updating Log")
        #Progress Bar Creation
        progBar = progressBarCreation("Getting Log Information")
        qL = updateLog()
        time.sleep(0.5)
        root.destroy()
    qL
 
except:
    print("Get Log")
    #Progress Bar Creation
    progBar = progressBarCreation("Getting Log Information")
    qL = updateLog()
    time.sleep(0.5)
    root.destroy()

#Variable Declarations
#Number of Lines and All Part List
lineCount = int(0)
quoteNum = []
quoteDate = []
quoteLastRev = []

#RFQ Check if Part Numbers are found
rfqHasPartNum = False

#rfqi iterator and log bottom
rfqi = int(18)
logi = int(len(qL.quotedQuotes)) - 1

#Color Variables
yellow = (255, 255, 0)

#Get Path of RFQ
path = getWorkbookPath()

#If No File Found
if path == "":
    popupError("No Path Given", 'The path selected or not selected does not exist. Program will quit')
    sys.exit()

#Get RFQ Worksheet
ws = xw.Book(path, update_links=False).sheets["Pricing Summary_1"]

#Add Line Item Info and proper spacing
while (not ('and' in str(ws.range("A" + str(rfqi)).value))):
    
    #Find Line Item Number in Excel File
     if pd.isna(ws.range(/*Line Number Column Letter*/ + str(rfqi)).value) == 0:
       #A Part Number was Found
       rfqHasPartNum = True
       
       #Check if part number is currently being quoted returns quote number in not return value None
       quoteNumCurrent = activelyQuotingQuote(ws.range(/*Part Number Column Letter*/ + str(rfqi)).value)
       
       #Let User know part number is being quoted and on what quote
       if(quoteNumCurrent != "None"):
           popupInfo('Parts Numbers Already Being Quoted',  ws.range("B" + str(rfqi)).value + " is already being quoted on quote " + quoteNumCurrent)
       
       #Check if REV is in part Number
       if ('-REV' in ws.range("B" + str(rfqi)).value):
           needCompanyTag = True  
           i = ws.range("B" + str(rfqi)).value.rfind('-REV')
           partNum = ws.range("B" + str(rfqi)).value[:i]
       else:
           partNum = ws.range("B" + str(rfqi)).value
           needCompanyTag = False
       #Get Company Tag
       if needCompanyTag:
           companyTagStart = ws.range("B" + str(rfqi)).value.rfind('-')
           companyTag = ws.range("B" + str(rfqi)).value[companyTagStart:]
           #Check that last value is alpha numeric
           while not companyTag[-1].isalnum():
               companyTag = companyTag[:-1]
       else:
           companyTag = ''
       numQty = 1
       
       #Look for part number in archive
       indexPartNum = qL.quotedQuotes.index[qL.quotedQuotes['ITEM'].str.contains(partNum)]
       if len(indexPartNum) > 0:
           iFindCompanyPart = 1
           while iFindCompanyPart <= len(indexPartNum):
               if companyTag in qL.quotedQuotes.iloc[indexPartNum[-iFindCompanyPart],6]:
                   logi = indexPartNum[-iFindCompanyPart]
                   iFindCompanyPart = len(indexPartNum)
               iFindCompanyPart = iFindCompanyPart + 1
           
           #Get Number of Quantities
           while partNum in qL.quotedQuotes.iloc[logi-numQty, 6]:
               numQty = numQty + 1
       
           #Add proper spacing to excel
           addRows = 0
           rowsNextPart = 1
            
            #Check how many rows need added
           while pd.isna(ws.range("A" + str(rfqi + rowsNextPart)).value) == True:
                rowsNextPart = rowsNextPart + 1
           addRows = numQty - rowsNextPart + 1
           if numQty < 2:
               addRows = 3 - rowsNextPart
            
            # Last Line Item needs extra rows
           if ('and' in str(ws.range("A" + str(rfqi + rowsNextPart)).value)):
                addRows = addRows + 3
                rowsNextPart = rowsNextPart - 3
            
            #Add Lines if Necessary
           if (addRows > 0):
                while addRows != 0:
                    ws.range(str(rfqi + rowsNextPart) + ":" + str(rfqi + rowsNextPart)).insert()
                    ws.range("M"+ str(rfqi + rowsNextPart) + ":P" + str(rfqi + rowsNextPart)).color = None
                    addRows = addRows - 1
            
            #Add Previous Quote Info
           i = 0
           ws.range("P" + str(rfqi)).value = qL.quotedQuotes.iloc[logi, 2]
           ws.range("P" + str(rfqi + 1)).value = qL.quotedQuotes.iloc[logi, 0]
           while i < numQty:
                ws.range("M" + str(rfqi + numQty - i - 1)).value = qL.quotedQuotes.iloc[logi - i, 19]
                if  pd.isna(qL.quotedQuotes.iloc[logi - i ,10]):
                    ws.range("N" + str(rfqi + numQty - i - 1)).value = qL.quotedQuotes.iloc[logi - i, 5]
                else:
                    ws.range("N" + str(rfqi + numQty - i - 1)).value = qL.quotedQuotes.iloc[logi - i, 10]
                if not pd.isna(qL.quotedQuotes.iloc[logi - i ,21]):
                    ws.range("O" + str(rfqi + numQty - i - 1)).value = qL.quotedQuotes.iloc[logi - i, 21]
                i = i + 1
           ws.range("Q" + str(rfqi)).formula = '=IF(OR($O$14="", $O$14="MULTIPLE"),(ROUNDUP((($I$15-P' + str(rfqi) + ')/365),1)),(ROUNDUP((($I$15-$O$14)/365),1)))'
           ws.range("Q" + str(rfqi)).number_format = '0.00'
           ws.range("R" + str(rfqi)).formula = '=1-(M' + str(rfqi) + '/H' + str(rfqi) + ')'
           ws.range("R" + str(rfqi)).number_format = "0.00%"
           quoteNum.append(qL.quotedQuotes.iloc[logi, 0])
           quoteDate.append(qL.quotedQuotes.iloc[logi, 2])
           quoteLastRev.append(qL.quotedQuotes.iloc[logi, 7])
           if pd.isna(quoteLastRev[lineCount]):
                quoteLastRev[lineCount] = "NO REV"
           lineCount = lineCount + 1
        
       else:
            popupInfo("Part Number Warning", "No previous quote information found on part number {}!\nNote: If this is a REVA previous quote info might be without REVA." .format(partNum))
            #Add NA value
            ws.range("M" + str(rfqi) + ":P" + str(rfqi)).value = "N/A"
            
       #Highlight top quantity line
       ws.range("M" + str(rfqi) + ":P" + str(rfqi)).color = yellow
           
       #Rows till next part number
       nextPart = 1
       while pd.isna(ws.range("A" + str(rfqi + nextPart)).value) == True:
           nextPart = nextPart + 1
       
       #Number Part Quantities
       #Check if parts are in PO Quantities or BIN
       colQuantities = "G"
       if pd.isna(ws.range(colQuantities + str(rfqi)).value) == True:
           colQuantities = 'F'
       
       quantities = 1
       while pd.isna(ws.range(colQuantities + str(rfqi + quantities)).value) == False:
           quantities = quantities + 1
       
       #Check if row needs added
       if nextPart < quantities:
           ws.range(str(rfqi + nextPart) + ":" + str(rfqi + nextPart)).insert()
           ws.range(str(rfqi + nextPart) + ":" + str(rfqi + nextPart)).color = None
             
       rfqi = rfqi + nextPart - 1
        
       logi = int(len(qL.quotedQuotes)) - 1
       
       
     #End code if it can't find end of part number list
     if rfqi == 100:
         print("Error: Could not find end of lines. Stopped after line 100.")
         sys.exit()
     rfqi = rfqi + 1
     
#No Part Numbers found
if not rfqHasPartNum:
    popupError("Part Number Warning", "No Part Numbers were found. Program Canceled!\nHint: Make sure there are line numbers in front of part number on RFQ")
    sys.exit()

#If new parts
if quoteNum == []:
    popupInfo('New Parts Numbers', 'No previous pricing found for RFQ parts')
    xw.Book(path).save(path)
    sys.exit()

#Prior Quote Headers
i=1
pQuoteNum = quoteNum[0]
pDate = quoteDate[0]
pRev = quoteLastRev[0]

#Check if all part numbers are from same quote, same rev, and same date
for i in range(lineCount):
    if (pQuoteNum != quoteNum[i]):
        pQuoteNum = "MULTIPLE"
    if (pDate != quoteDate[i]):
        pDate = "MULTIPLE"
    if (pRev != quoteLastRev[i]):
        pRev = "MULTIPLE"
        
#Set all part numbers are from same quote, same rev, and same date
ws.range("M15").value = pQuoteNum
ws.range("O14").value = pDate
ws.range("O15").value = pRev

#Save results back to file
xw.Book(path).save(path)