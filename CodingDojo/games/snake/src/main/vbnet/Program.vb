Imports System
Imports System.Collections.Generic
Imports System.Linq
Imports System.Net.WebSockets
Imports System.Text
Imports System.Threading
Imports System.Threading.Tasks

Module Program

    'Server name and port number -- ask orgs
    Private Property ServerNameAndPort As String = "epruryaw0576:8080"

    'Register on the server, write down your player id (you can find it on board page url after registration)
    Private Property UserId As String = "3edq63tw0bq4w4iem7nb"

    'Look up for the code in the browser url after the registration
    Private Property UserCode As String = "12345678901234567890"

    Private ReadOnly Property consoleLock As New Object()
    Private Const receiveChunkSize As Integer = 1024 * 10
    Private Const verbose As Boolean = True
    Private ReadOnly Property encoder As Encoding = New UTF8Encoding(False)
    Private ReadOnly Property myBot As New MySnakeBot()


    Sub Main(args As String())
        Thread.Sleep(1000)
        Dim url as String = String.Format("ws://{0}/codenjoy-contest/ws?user={1}&code={2}", _
            ServerNameAndPort, UserId, UserCode)
        Connect(url).Wait()
    End Sub

    Public Async Function Connect(ByVal uri As String) As Task

        Dim webSocket As ClientWebSocket = Nothing

        Try
            webSocket = New ClientWebSocket()
            Await webSocket.ConnectAsync(New Uri(uri), CancellationToken.None)
            Await Receive(webSocket)
        Catch ex As Exception
            Console.WriteLine("Exception: {0}", ex)
        Finally
            If webSocket IsNot Nothing Then
                webSocket.Dispose()
            End If
            Console.WriteLine()

            SyncLock consoleLock
                Console.ForegroundColor = ConsoleColor.Red
                Console.WriteLine("WebSocket closed.")
                Console.ResetColor()

            End SyncLock

        End Try

    End Function

    Private Async Function Send(ByVal websocket As ClientWebSocket, command As String) As Task
        Dim buffer As Byte() = encoder.GetBytes(command)
        Await websocket.SendAsync(New ArraySegment(Of Byte)(buffer), WebSocketMessageType.Text, True, CancellationToken.None)
        LogStatus(False, buffer, buffer.Length)
    End Function

    Private Async Function Receive(ByVal webSocket As ClientWebSocket) As Task

        Dim buffer(receiveChunkSize) As Byte

        While webSocket.State = WebSocketState.Open
            Dim result = Await webSocket.ReceiveAsync(New ArraySegment(Of Byte)(buffer), CancellationToken.None)

            If result.MessageType = WebSocketMessageType.Close Then
                Await webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, String.Empty, CancellationToken.None)
            Else
                For i As Integer = result.Count To buffer.Length - 1
                    buffer(i) = 0
                Next
                LogStatus(True, buffer, result.Count)
                Dim command As String = myBot.Process(encoder.GetString(buffer, 0, result.Count))
                Await Send(webSocket, command)
            End If

        End While

    End Function

    Private Sub LogStatus(ByVal receiving As Boolean, buffer As Byte(), length As Integer)

        SyncLock consoleLock
            If verbose AndAlso Not receiving Then
                Console.Clear()
                Console.Write(DateTime.Now.ToString())
                Console.Write("  ")

                Console.WriteLine(myBot.HeadlineText)
                Console.WriteLine(myBot.DisplayText)
                Console.WriteLine(myBot.CommandText)
            End If

            Console.ResetColor()

        End SyncLock

    End Sub

End Module
