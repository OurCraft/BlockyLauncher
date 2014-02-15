package net.blocky.launch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;

public class Script
{

    private String script;
    private ArrayList<ScriptInstruction> instructions;
    private int index;
    private ScriptListener listener;

    public Script(String script)
    {
        this.script = script;
        parseScript(script);
    }
    
    public String getEntireScript()
    {
        return script;
    }
    
    public void setListener(ScriptListener l)
    {
        listener = l;
    }
    
    private void parseScript(String s)
    {
        String[] lines = s.split("\r|\n");
        ArrayList<String> args = new ArrayList<String>();
        instructions = new ArrayList<ScriptInstruction>(); 
        for(String line : lines)
        {
            String parts[] = line.split(" ");
            String instruction = parts[0];
            args.clear();
            if(doesInstructionHasArgs(instruction))
            {
                for(int index = 1;index<parts.length;index++)
                {
                    args.add(parts[index]);
                }
            }
            instructions.add(new ScriptInstruction(instruction, args.toArray(new String[0])));
        }
    }
    
    public void injectIntoClasspath(URL path)
    {
        try
        {
            URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[]{URL.class}); 
            addURL.setAccessible(true);
            addURL.invoke(classLoader, new Object[]{path});
            System.out.println("Injected "+path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private int executeInstruction(ScriptInstruction in) throws ScriptParseException
    {
        if(listener == null || listener.onInstruction(in))
        try
        {
            String instruction = in.getInstruction();
            String[] args = in.getArgs();
            if(instruction == null || instruction.equals(""))
            {
                return 0;
            }
            if(instruction.equals("exit"))
            {
                return -1; // Stops the script
            }
            for(int i = 0;i<args.length;i++)
            {
                args[i] = format(args[i]);
            }
            if(instruction.equals("download"))
            {
                download(args[0], args[1]);
            }
            else if(instruction.equals("updateclasspath"))
            {
                if(args[0].equals("add"))
                {
                    injectIntoClasspath(new File(args[1]).toURI().toURL());
                }
            }
            else
            {
                throw new ScriptParseException("Unknown instruction: "+instruction);
            }
            return 0;
        }
        catch(Exception e)
        {
            throw new ScriptParseException(e);
        }
        
        return 0;
    }
    
    private void download(String url, String dstPath) throws Exception
    {
        URL src = new URL(url);
        File dst = new File(dstPath);
        if(!dst.getParentFile().exists())
            dst.getParentFile().mkdirs();
        dst.createNewFile();
        URLConnection connection = src.openConnection();
        BufferedInputStream input = new BufferedInputStream(src.openStream());
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(dst));
        int i = 0;
        byte[] buffer = new byte[65565];
        int index = 0;
        int max = connection.getContentLength();
        while((i = input.read(buffer, 0, buffer.length)) != -1)
        {
            output.write(buffer, 0, i);
            index+=i;
            if(listener != null)
            listener.onDownloaded(buffer, i, index, max);
        }
        output.flush();
        output.close();
        input.close();
    }

    private String format(String string)
    {
        if(listener != null)
            string = listener.formatArg(string);
        return string;
    }

    private boolean doesInstructionHasArgs(String instruction)
    {
        if(instruction.equals("exit"))
        {
            return false;
        }
        return true;
    }
    
    public boolean hasNext()
    {
        if(index == -1)
            return false;
        return index < instructions.size();
    }

    public void executeNext()
    {
        if(hasNext())
        {
            if(this.executeInstruction(instructions.get(index)) != -1)
                index++;
            else
                index = -1;
        }
    }
}
