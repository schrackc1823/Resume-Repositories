Sub BuildAssemblyList()
' BuildAssemblyList Macro
Application.ScreenUpdating = False
Application.Calculation = False
Dim wb As Workbook
Set wb = ThisWorkbook
Dim numAss As Integer
Dim i As Integer
Dim j As Integer
Dim topAss() As String
Dim line As Integer
wb.Sheets(/*Pricing Breakdown Tab*/).Activate

'Number of Assemblies in Quote
Do While Not IsEmpty(wb.Sheets(/*Assembly List Tab*/).Cells(numAss + 12, 1).Value) Or Not StrComp(wb.Sheets(/*Assembly List Tab*/).Cells(numAss + 12, 1).Value, /*End Condition*/)
    numAss = numAss + 1
Loop
If numAss = 0 Then
    MsgBox "No assemblies in this quote (Check /*Assembly List Tab*/)"
    Exit Sub
End If

'Create number of Assembly Lists
Application.CutCopyMode = False
wb.Sheets(/*Pricing Breakdown Tab*/).Range("A1:BY5").Copy

For i = 2 To numAss
    wb.Worksheets(/*Pricing Breakdown Tab*/).Range("A" & ((i - 1) * 8 + 2 - i)).PasteSpecial Paste:=xlPasteFormulas
    wb.Worksheets(/*Pricing Breakdown Tab*/).Range("A" & ((i - 1) * 8 + 2 - i)).PasteSpecial Paste:=xlPasteAllUsingSourceTheme
Next i

'Add top Assemblies to /*Pricing Breakdown Tab*/
ReDim topAss(numAss - 1)
For i = 0 To numAss - 1
    topAss(i) = wb.Worksheets(/*Assembly List Tab*/).Cells(12 + i, 1).Value
Next i
For i = 0 To numAss - 1
    wb.Worksheets(/*Pricing Breakdown Tab*/).Cells(i * 8 + 3 - i, 1).Value = topAss(i)
Next i

'Add List Value
line = -2
For i = 0 To numAss - 1
    Dim checker As Integer
    line = line + 6
    checker = line
    line = subsAdded(line, topAss(i), 1)
    If Not line + 6 = checker Then
        wb.Sheets(/*Pricing Breakdown Tab*/).Rows(line & ":" & line).Select
        Selection.Delete
    End If
Next i

MsgBox "Assembly List Builder is Complete"
End Sub


' Adds subassemblies and quantities of subassemblies. Then check for subassemblies for lower level assemblies. At end returns row location for next assembly will be placed in /*Pricing Breakdown Tab Name*/ sheet
Function subsAdded(rowLoc As Integer, assNum As String, qty As Integer) As Integer
    Dim loc As Range, i As Integer, subAssNum As String, subAssQty As Integer, partType As String
    Set wb = ThisWorkbook
    'Find /*First Assembly with Parts Tab*/ Assembly Number first location
    Set loc = wb.Sheets(/*First Assembly with Parts Tab*/).Range("B:B").Find(assNum)
    'If assembly number was not found
    If loc Is Nothing Then
        MsgBox "Assembly " & assNum & " not found in Assembly Qty 1 sheet. Removing from /*Pricing Breakdown Tab Name*/"
        Application.CutCopyMode = False
        wb.Sheets(/*Pricing Breakdown Tab*/).Rows(rowLoc - 5 & ":" & rowLoc + 1).Select
        Selection.Delete
        subsAdded = rowLoc - 6
        Exit Function
    End If
    
    'Search for subs in this assNumber
    i = 0
    partType = wb.Sheets(/*First Assembly with Parts Tab*/).Cells(loc.Row + i, 4).Value
    Do While Not partType = "" Or partType = "PC: "
        'If sub is found add assembly number and to assembly list,
        If partType = /*Subassembly Value*/ Then
            'Add row for subassembly
            Application.CutCopyMode = False
            wb.Sheets(/*Pricing Breakdown Tab*/).Rows(rowLoc & ":" & rowLoc).Select
            Selection.Copy
            wb.Sheets(/*Pricing Breakdown Tab*/).Rows(rowLoc & ":" & rowLoc).Select
            Selection.Insert Shift:=xlDown
            'Insert values in subasseblky area
            subAssNum = wb.Sheets(/*First Assembly with Parts Tab*/).Cells(loc.Row + i, 6).Value
            subAssQty = wb.Sheets(/*First Assembly with Parts Tab*/).Cells(loc.Row + i, 12).Value * qty
            wb.Sheets(/*Pricing Breakdown Tab*/).Cells(rowLoc, 1).Value = subAssNum
            wb.Sheets(/*Pricing Breakdown Tab*/).Cells(rowLoc, 4).Value = subAssQty
            rowLoc = rowLoc + 1
            'Check for sub of sub assembly
            rowLoc = subsAdded(rowLoc, subAssNum, subAssQty)
        End If
        i = i + 1
        partType = wb.Sheets(/*First Assembly with Parts Tab*/).Cells(loc.Row + i, 4).Value
    Loop
    subsAdded = rowLoc
End Function

