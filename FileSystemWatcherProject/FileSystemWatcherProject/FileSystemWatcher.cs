using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
public class OZFileSystemWatcher
{
    private static int fileCounter = 0;

    public static void Main()
    {
        startWatching();
    }

    public static void startWatching()
    {
        string[] args = System.Environment.GetCommandLineArgs();

        // If a directory is not specified, exit program.
        if (args.Length != 4)
        {
            // Display the proper way to call the program.
            Console.WriteLine("Usage: FileSystemWatcher4NetworkViews.exe directory2Watch hh mm");
            Console.ReadLine();
            return;
        }
        String folder2Watch = args[1];
        int endHour = Convert.ToInt32(args[2]);
        int endMinute = Convert.ToInt32(args[3]);
        // Create a new FileSystemWatcher and set its properties.
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher();
        fileSystemWatcher.Path = folder2Watch;
        /* Watch for changes in LastAccess and LastWrite times, and the renaming of files or directories. */
        //  watcher.NotifyFilter = NotifyFilters.LastAccess | NotifyFilters.LastWrite
        //   | NotifyFilters.FileName | NotifyFilters.DirectoryName | NotifyFilters.Size;
        fileSystemWatcher.NotifyFilter = NotifyFilters.Size;
        // Only watch text files.
        fileSystemWatcher.Filter = "*.txt";

        // Add event handlers.
        fileSystemWatcher.Changed += new FileSystemEventHandler(OnChanged);
        fileSystemWatcher.Created += new FileSystemEventHandler(OnChanged);
        //  fileSystemWatcher.Deleted += new FileSystemEventHandler(OnChanged);
        //  fileSystemWatcher.Renamed += new RenamedEventHandler(OnRenamed);

        // Begin watching.
        fileSystemWatcher.EnableRaisingEvents = true;
        // fileSystemWatcher.IncludeSubdirectories = true;

        //  Console.WriteLine("Start watchinging at "+ folder2Watch);
        // Wait for the user to quit the program.

        string currentTimeInString = GetTime();
        DateTime currentDateTime = DateTime.Now;
        DateTime endDateTime = getEndDateTime(endHour, endMinute, 00);
        TimeSpan duration = endDateTime - currentDateTime;
        int millisecondsToRun = (duration.Hours * 3600 + duration.Minutes * 60 + duration.Seconds) * 1000;

        //   millisecondsToRun = 1000;
        //  Console.WriteLine(currentDateTime);
        //  Console.WriteLine(endDateTime);
        //  Console.WriteLine(duration);
        //  Console.WriteLine(millisecondsToRun);
        // Console.WriteLine("Duration is " + duration );

        Console.WriteLine("\nStart watchinging " + folder2Watch + " at " + currentTimeInString + " for " + duration + " thread: " + System.Threading.Thread.CurrentThread.ManagedThreadId);

        //   while (Console.Read() != 'q') ;
        // System.Threading.Thread.Sleep(2000);

        System.Threading.Thread.Sleep(millisecondsToRun);
        Console.WriteLine("Watcher exists on " + GetTime());
        System.Environment.Exit(0);
    }

    // Define the event handlers.
    private static void OnChanged(object source, FileSystemEventArgs e)
    {
        fileCounter++;
        // Specify what is done when a file is changed, created, or deleted.
        Console.WriteLine(GetTime() + "   Processing: " + e.FullPath + "  change type: " + e.ChangeType + " name: " + e.Name + " file number: " + fileCounter.ToString() + " thread: " + System.Threading.Thread.CurrentThread.ManagedThreadId);

        if (e.FullPath.ToLower().EndsWith("exitnow.txt"))
        {
            Console.WriteLine("Watcher exists on " + GetTime());
            System.Environment.Exit(0);
        }
        String batFilePath = e.FullPath + "." + DateTime.Now.Hour.ToString() + "." + DateTime.Now.Minute.ToString() + "." + DateTime.Now.Second.ToString() + "." + DateTime.Now.Millisecond.ToString() + ".bat";
        File.Move(e.FullPath, batFilePath);
        System.Diagnostics.Process proc = new System.Diagnostics.Process();
        proc.EnableRaisingEvents = false;
        proc.StartInfo.FileName = batFilePath;
        proc.StartInfo.CreateNoWindow = true;
        proc.Start();
        Console.WriteLine(GetTime() + "   " + batFilePath + " started");

    }

    public static string GetTime()
    {
        string TimeString = "";
        // Get current time
        int hour = DateTime.Now.Hour;
        int min = DateTime.Now.Minute;
        int sec = DateTime.Now.Second;

        // Format current time into string
        TimeString = (hour < 10) ? "0" + hour.ToString() : hour.ToString();
        TimeString += ":" + ((min < 10) ? "0" + min.ToString() : min.ToString());
        TimeString += ":" + ((sec < 10) ? "0" + sec.ToString() : sec.ToString());

        return TimeString;
    }
    //
    //
    public static DateTime getEndDateTime(int hour, int minute, int second)
    {
        int year = DateTime.Now.Year;
        int month = DateTime.Now.Month;
        int day = DateTime.Now.Day;
        DateTime endDateTime = new DateTime(year, month, day, hour, minute, second);
        return endDateTime;

    }
}


