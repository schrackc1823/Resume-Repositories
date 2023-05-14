Option Explicit

Public assSize As Integer
Public wb As Workbook

Private Sub UserForm_Initialize()
    UserForm3.Height = 425
    UserForm3.Width = 400
    
    'Variable Delaration
   Dim assemblies() As String, i As Integer, tempStr As String
   Set wb = ThisWorkbook
   
   'Determine Number of Assemblies
   If (IsEmpty(wb.Sheets(/*Sheet with Assemblies List*/).Cells(12, 1).Value)) Then
       GoTo ErrHnd
   Else
       assSize = 0
       Do While Not IsEmpty(wb.Sheets(/*Sheet with Assemblies List*/).Cells(12 + assSize, 1).Value) And wb.Sheets(/*Sheet with Assemblies List*/).Cells(12 + assSize, 1).Value <> /*Ending Condition*/
           assSize = assSize + 1
        Loop
   End If
   
   'Make List of Assemblies
   ReDim assemblies(assSize - 1)
   For i = 0 To assSize - 1
       assemblies(i) = wb.Sheets(/*Sheet with Assemblies List*/).Cells(12 + i, 1).Value
   Next i
   
    AssemblyOptions.List() = assemblies
    NumAssembly.List() = Array(1, 2, 3, 4, 5)
    Exit Sub
        
ErrHnd:
    MsgBox ("No Assemblies found")
    Unload UserForm3
    Exit Sub
    
End Sub


Private Sub CalcButton_Click()
    ' Store the row numbers of selected items to a collection
    Dim selectedAss() As String, tempIntAss() As Integer, numQuantity As Integer, i As Integer, j As Integer, k As Integer, partSize As Integer, selectedSize As Byte
    selectedSize = 0
    ReDim tempIntAss(assSize - 1)
    
    'Get selected part numbers
    For i = 0 To AssemblyOptions.ListCount - 1
    
        ' Check if item at position i is selected
        If AssemblyOptions.Selected(i) Then
            tempIntAss(selectedSize) = i
            selectedSize = selectedSize + 1
        End If
    Next i
    
    'No assemblies selected error
    If selectedSize = 0 Then
        GoTo ErrNoSelect
    End If
    
    'Make array of selectedRows
    ReDim selectedAss(selectedSize - 1)
    For i = 0 To selectedSize - 1
        selectedAss(i) = AssemblyOptions.List(tempIntAss(i))
    Next i
    
    'Get size of part list
    Dim MaterialNum() As String
    partSize = 0
    Do While Not IsEmpty(wb.Sheets(/*Part Sheet Name*/).Cells(partSize + 8, 1).Value))
        partSize = partSize + 1
    Loop
    
    'Sort Material numbers before storing
    wb.Sheets(/*Part Sheet Name*/).Range("A8:FK" & partSize + 7).Sort Key1:=wb.Sheets(/*Part Sheet Name*/).Range("A8:A" & partSize + 7), Order1:=xlAscending, Header:=xlNo
    
    'Make Alphabetical part list in array
    ReDim MaterialNum(partSize - 1)
    For i = 0 To partSize - 1
        MaterialNum(i) = wb.Sheets(/*Part Sheet Name*/).Cells(i + 8, 1).Value
    Next i
        
    'Loop for each assembly
    For numQuantity = 1 To NumAssembly.Value
        'Find Material Quantity of assemblies & material removable values
        ReDim AssPart2DArray(selectedSize - 1, partSize - 1) As Single
        ReDim removeVal(partSize) As Single
        ReDim minVal(partSize) As Single
        Dim tempNum As Single
        For i = 0 To partSize - 1
            For j = 0 To selectedSize - 1
                AssPart2DArray(j, i) = getQty(selectedAss(j), MaterialNum(i), numQuantity)
            Next j
            'Create array of removable values
            Dim bigNum As Single
            bigNum = AssPart2DArray(0, i)
            removeVal(i) = 0
            For k = 1 To selectedSize - 1
                tempNum = AssPart2DArray(k, i)
                If bigNum < tempNum Then
                    removeVal(i) = removeVal(i) + bigNum
                    bigNum = tempNum
                Else
                    removeVal(i) = removeVal(i) + tempNum
                End If
            Next k
            minVal(i) = bigNum
        Next i
        
        'Calculate Material tab quantities
        Application.ScreenUpdating = False
        Application.Calculation = False
        ReDim changeVal(partSize) As Single
        For i = 0 To partSize - 1
            tempNum = wb.Sheets(/*Part Sheet Name*/).Cells(i + 8, 8 + (19 * (numQuantity - 1))).Value - removeVal(i)
            If tempNum < minVal(i) Then
                changeVal(i) = minVal(i)
            Else
                changeVal(i) = tempNum
            End If
        Next i
        'Change Material Values
        Select Case numQuantity
        Case 1
            wb.Sheets(/*Part Sheet Name*/).Range("H8:H" & partSize + 7) = WorksheetFunction.Transpose(changeVal)
        Case 2
            wb.Sheets(/*Part Sheet Name*/).Range("AA8:AA" & partSize + 7) = WorksheetFunction.Transpose(changeVal)
        Case 3
            wb.Sheets(/*Part Sheet Name*/).Range("AT8:AT" & partSize + 7) = WorksheetFunction.Transpose(changeVal)
        Case 4
            wb.Sheets(/*Part Sheet Name*/).Range("BM8:BM" & partSize + 7) = WorksheetFunction.Transpose(changeVal)
        Case 5
            wb.Sheets(/*Part Sheet Name*/).Range("CF8:CF" & partSize + 7) = WorksheetFunction.Transpose(changeVal)
        Case Else
            MsgBox Error("Quantity Value not 1 through 5")
        End Select
    Next numQuantity
    'End program
    Application.Calculation = True
    Application.ScreenUpdating = True
    Unload UserForm3
    MsgBox ("Quantity Calculator Complete")
Exit Sub

ErrNoSelect:
    MsgBox ("No Assemblies were selected")
    Unload UserForm3
    Load UserForm3
    End Sub


'Give Bin Quantity for part in that parent part
Function getQty(parent As String, part As String, numQuantity As Integer) As Single
    Dim loc As Range, last As Integer, qty As Single, i As Integer
    qty = 0
    'Find first and last occurance of parent
    Set loc = wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Range("B:B").Find(parent)
    last = 1
    Do While Not wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(last + loc.Row, 6).Value = ""
        last = last + 1
    Loop
    last = last + loc.Row - 1
    
    'Check Subs for part number
    For i = loc.Row To last
        If (wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 4).Value = "S") Then
            qty = qty + getQtySub(wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 6).Value, part, wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 14).Value, numQuantity)
        End If
    Next i
    
    'Find quantity location
    Set loc = wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Range("F" & loc.Row & ":B" & last).Find(part, LookAt:=1, MatchCase:=True)
    If loc Is Nothing Then
    Else
        qty = qty + wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(loc.Row, 14).Value
    End If
    
    getQty = qty
End Function


'Give Bin Quantity for part in that parent part for subassemblies
Function getQtySub(parent As String, part As String, need As Single, numQuantity As Integer) As Single
    Dim loc As Range, last As Integer, qty As Single, i As Integer, min As Single
    qty = 0
    'Find first and last occurance of parent
    Set loc = wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Range("B:B").Find(parent)
    last = 1
    Do While Not wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(last + loc.Row, 6).Value = ""
        last = last + 1
    Loop
    last = last + loc.Row - 1
    
    'Find Assembly min order
    min = wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(loc.Row + 27, 25).Value
    
    'Check Subs for part number
    If min > need Then
        For i = loc.Row To last
            If (wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 4).Value = "S") Then
                qty = qty + getQtySub(wb.Sheets("Assemblies Qty 1").Cells(i, 6).Value, part, wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 12).Value * min, numQuantity)
            End If
        Next i
    Else
        For i = loc.Row To last
            If (wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(i, 4).Value = "S") Then
                qty = qty + getQtySub(wb.Sheets(/*First Tab Name with Assembly Parts*/).Cells(i, 6).Value, part, wb.Sheets(/*First Tab Name with Assembly Parts*/).Cells(i, 12).Value * need, numQuantity)
            End If
        Next i
    End If
    'Find quantity location
    Set loc = wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Range("F" & loc.Row & ":B" & last).Find(part, LookAt:=1, MatchCase:=True)
    If loc Is Nothing Then
        getQtySub = qty
    Else
        If min > need Then
            getQtySub = qty + wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(loc.Row, 12).Value * min
        Else
            getQtySub = qty + wb.Sheets(/*Tab Name with Assembly Parts*/ & numQuantity).Cells(loc.Row, 12).Value * need
        End If
    End If
End Function


Private Sub CloseButton_Click()
    Unload UserForm3
End Sub




