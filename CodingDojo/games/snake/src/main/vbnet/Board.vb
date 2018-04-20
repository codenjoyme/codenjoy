Imports System
Imports System.Text

Friend Class Board

    Public Property RawBoard As String

    Public Property MapSize As Integer

    Public Sub Parse(ByVal input As String)

        If input.StartsWith("board=") Then
            input = input.Substring(6)
        End If

        RawBoard = input.Replace("☼", "#").  ' wall
        Replace("▲", "0").Replace("◄", "0").Replace("►", "0").Replace("▼", "0").  ' head
        Replace("║", "o").Replace("═", "o").Replace("╙", "o").Replace("╘", "o").  ' body
        Replace("╓", "o").Replace("╕", "o").
        Replace("╗", "o").Replace("╝", "o").Replace("╔", "o").Replace("╚", "o").  ' body
        Replace("☻", "X").  ' bad apple
        Replace("☺", "$") ' good apple

        With RawBoard

        End With

        Dim length As Integer = RawBoard.Length
        MapSize = CType(Math.Sqrt(length), Integer)

    End Sub

    Public Function GetAt(ByVal x As Integer, ByVal y As Integer)

        Return RawBoard(x + y * MapSize)

    End Function

    Public Function GetDisplay() As String

        Dim sb As New StringBuilder()

        For line As Integer = 0 To MapSize - 1
            If line > 0 Then
                sb.AppendLine()
            End If
            sb.Append("  ")
            sb.Append(RawBoard.Substring(MapSize * line, MapSize))
        Next

        Return sb.ToString()

    End Function

End Class
