Imports System

Friend Class MySnakeBot

    Public Property HeadlineText As String

    Public Property DisplayText As String

    Public Property CommandText As String

    Public Function Process(ByVal input As String) As String

        HeadlineText = ""
        DisplayText = ""

        Dim board As New Board()
        board.Parse(input)
        DisplayText = board.GetDisplay()

        Dim random As New Random()

        Dim move As Integer = random.Next(0, 3)

        Select Case move
            Case 0
                CommandText = "UP"
            Case 1
                CommandText = "DOWN"
            Case 2
                CommandText = "LEFT"
            Case 3
                CommandText = "RIGHT"
        End Select

        Return CommandText

    End Function


End Class
