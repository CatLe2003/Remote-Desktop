package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

public class TaskManager {

    public static boolean shutdown() {
        try {
            new ProcessBuilder("shutdown", "/s", "/t", "60").start();
        } catch (UnsupportedOperationException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean killProcess(long id) {
        Optional<ProcessHandle> process = ProcessHandle.of(id);
        
        if (process.isPresent())
            return process.get().destroyForcibly();
        return false;
    }

    public static Object[][] getTask() {
         try {
            Process temp = new ProcessBuilder("tasklist.exe", "/FO", "CSV", "/NH").start();
            BufferedReader datain = new BufferedReader(new InputStreamReader(temp.getInputStream()));
            ArrayList<Object[]> data = new ArrayList<Object[]>();
            String line = "";
            while (true) {
                line = datain.readLine();
                if (line == null || line.equals(""))
                    break;
                String[] lineComponents = line.split("\",\"");
                if (lineComponents[0].equals("\"tasklist.exe"))
                    continue;
                lineComponents[0] = lineComponents[0].substring(1);
                lineComponents[4] = lineComponents[4].substring(0, lineComponents[4].length() - 1);
                String[] lineData = { lineComponents[0], lineComponents[1], lineComponents[4] };
                data.add(lineData);
            }
            datain.close();
            // for (int i = 0; i < data.size(); i++) {
            // for (int j = 0; j < data.get(i).length; j++)
            // System.out.print(data.get(i)[j] + " ");
            // System.out.println("");
            // }
            Object[][] newData = new Object[data.size()][3];
            for (int i = 0; i < data.size(); i++)
                newData[i] = data.get(i);
            return newData;
        } catch (IOException e) {

        }
        return null;
    }

    public static boolean start(String name) {
        try {
            String fullpath = findFullPath(name);
            if (fullpath != null) {
                // System.out.println("\'" + fullpath + "\'");
                new ProcessBuilder("powershell", "Start-Process", "\'" + fullpath + "\'").start();
                return true;
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return false;
    }

    // get apps running
    public static Object[][] getAppRunning() {
        Process temp;
        try {
            temp = new ProcessBuilder("powershell",
                    "Get-Process |",
                    "where {$_.MainWindowHandle -ne 0} |",
                    "Group-Object -Property Id |",
                    "Format-Table @{n='Name'; e={$_.Group.ProcessName}}, @{n='ID'; e={$_.Name}}, @{n='Mem (KB)';e={'{0:N0}' -f (($_.Group|Measure-Object WorkingSet64 -Sum).Sum / 1KB)}}")
                    .start();
            temp.waitFor();
            DataInputStream datain = new DataInputStream(temp.getInputStream());
            String all = new String(datain.readAllBytes());
            datain.close();
            String[] lines = all.split("\n");
            Object[][] data = new String[lines.length - 5][3];
            for (int i = 3; i < lines.length - 2; i++)
                data[i - 3] = lines[i].split("[ ]+");
            return data;
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        return null;
    }

    // return full path if success else return the name of app
    public static String findFullPath(String name) {

        String position1 = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\";
        String position2 = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\";
        String position3 = "HKEY_CLASSES_ROOT\\Applications\\";
        try {
            Process temp = new ProcessBuilder("where", name).start();
            temp.waitFor();
            BufferedReader bf = new BufferedReader(new InputStreamReader(temp.getInputStream()));
            String fullpath = bf.readLine();
            if (fullpath != null)
                return fullpath;

            Process process = new ProcessBuilder("reg", "query", position1 + name, "/ve").start();
            process.waitFor();
            DataInputStream stream = new DataInputStream(process.getInputStream());
            String data = new String(stream.readAllBytes());
            stream.close();
            process.destroy();
            if (!data.isEmpty()) {
                String path = data.split("    ")[3];
                path = path.substring(0, path.lastIndexOf(".exe") + 4);
                return path;
            }

            process = new ProcessBuilder("reg", "query", position2 + name, "/ve").start();
            process.waitFor();
            stream = new DataInputStream(process.getInputStream());
            data = new String(stream.readAllBytes());
            stream.close();
            process.destroy();
            if (!data.isEmpty()) {
                String path = data.split("    ")[3];
                path = path.substring(1, path.lastIndexOf(".exe") + 4);
                return path;
            }

            process = new ProcessBuilder("reg", "query", position3 + name + "\\shell\\open\\command", "/ve").start();
            process.waitFor();
            stream = new DataInputStream(process.getInputStream());
            data = new String(stream.readAllBytes());
            stream.close();
            process.destroy();
            if (!data.isEmpty()) {
                String path = data.split("    ")[3];
                path = path.substring(0, path.lastIndexOf(".exe") + 5);
                return path;
            }

        } catch (IOException e) {
            // System.out.println("Can't invoke reg.exe command");
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }

        return null;
    }
}
