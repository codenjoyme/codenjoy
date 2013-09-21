using System;
using System.Threading;

namespace Demo
{
    class Program
    {
        static void Main(string[] args)
        {
            // creating custom bomberman's Ai client
            var bomber = new MyCustomBombermanAI("ApiDotNet");
            
            // starting thread with playing bomberman
            (new Thread(bomber.Play)).Start();
            
            // waiting for "anykey"
            Console.ReadKey();

            // on "anykey" - asking bomberman's Ai client to stop. 
            bomber.InitiateExit();
        }
    }
}
