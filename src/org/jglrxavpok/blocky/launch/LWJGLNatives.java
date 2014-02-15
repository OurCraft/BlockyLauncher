package org.jglrxavpok.blocky.launch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class LWJGLNatives
{

    private static boolean loaded;

    public static void load(String nativesFolder) throws Exception
    {
        if(!loaded)
        {
            File folder = new File(nativesFolder);
            if(!folder.exists())folder.mkdirs();
            if(folder.isDirectory())
            {
                installNatives(folder);
                System.setProperty("org.lwjgl.librarypath", nativesFolder);
            }
            loaded = true;
        }
    }
    
    private static void installNatives(File folder) throws Exception 
    {
        if(getNameOfOS().equalsIgnoreCase("windows"))
        {
            if(!new File(folder.getPath()+"/OpenAL64.dll").exists())
            {
                IO.copy(getFFJar("/windows/jinput-dx8_64.dll", LWJGLNatives.class), folder.getPath()+"/jinput-dx8_64.dll").close();
                IO.copy(getFFJar("/windows/jinput-dx8.dll", LWJGLNatives.class), folder.getPath()+"/jinput-dx8.dll").close();
                IO.copy(getFFJar("/windows/jinput-raw_64.dll", LWJGLNatives.class), folder.getPath()+"/jinput-raw_64.dll").close();
                IO.copy(getFFJar("/windows/jinput-raw.dll", LWJGLNatives.class), folder.getPath()+"/jinput-raw.dll").close();
                IO.copy(getFFJar("/windows/lwjgl.dll", LWJGLNatives.class), folder.getPath()+"/lwjgl.dll").close();
                IO.copy(getFFJar("/windows/lwjgl64.dll", LWJGLNatives.class), folder.getPath()+"/lwjgl64.dll").close();
                IO.copy(getFFJar("/windows/OpenAL32.dll", LWJGLNatives.class), folder.getPath()+"/OpenAL32.dll").close();
                IO.copy(getFFJar("/windows/OpenAL64.dll", LWJGLNatives.class), folder.getPath()+"/OpenAL64.dll").close();
            }
            else
            {
                System.out.println("Natives already exist.");
            }
        }
        if(getNameOfOS().equalsIgnoreCase("solaris"))
        {
            if(!new File(folder.getPath()+"/liblwjgl.so").exists())
            {
                IO.copy(getFFJar("/solaris/liblwjgl.so", LWJGLNatives.class), folder.getPath()+"/liblwjgl.so").close();
                IO.copy(getFFJar("/solaris/liblwjgl64.so", LWJGLNatives.class), folder.getPath()+"/liblwjgl64.so").close();
                IO.copy(getFFJar("/solaris/libopenal.so", LWJGLNatives.class), folder.getPath()+"/libopenal.so").close();
                IO.copy(getFFJar("/solaris/libopenal64.so", LWJGLNatives.class), folder.getPath()+"/libopenal64.so").close();
            }
            else
            {
                System.out.println("Natives already exist.");
            }

        }
        if(getNameOfOS().equalsIgnoreCase("linux"))
        {
            if(!new File(folder.getPath()+"/liblwjgl.so").exists())
            {
                IO.copy(getFFJar("/linux/liblwjgl.so", LWJGLNatives.class), folder.getPath()+"/liblwjgl.so").close();
                IO.copy(getFFJar("/linux/liblwjgl64.so", LWJGLNatives.class), folder.getPath()+"/liblwjgl64.so").close();
                IO.copy(getFFJar("/linux/libopenal.so", LWJGLNatives.class), folder.getPath()+"/libopenal.so").close();
                IO.copy(getFFJar("/linux/libopenal64.so", LWJGLNatives.class), folder.getPath()+"/libopenal64.so").close();
            }
            else
            {
                System.out.println("Natives already exist.");
            }

        }
        if(getNameOfOS().equalsIgnoreCase("macosx"))
        {
            if(!new File(folder.getPath()+"/openal.dylib").exists())
            {
                IO.copy(getFFJar("/macosx/liblwjgl.jnilib", LWJGLNatives.class), folder.getPath()+"/liblwjgl.jnilib").close();
                IO.copy(getFFJar("/macosx/liblwjgl-osx.jnilib", LWJGLNatives.class), folder.getPath()+"/liblwjgl-osx.jnilib").close();
                IO.copy(getFFJar("/macosx/openal.dylib", LWJGLNatives.class), folder.getPath()+"/openal.dylib").close();
            }
            else
            {
                System.out.println("Natives already exist.");
            }

        }
        System.setProperty("net.java.games.input.librarypath", folder.getPath()+"/");
    }

    public static String getNameOfOS() 
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
        {
            return "Windows";
        }
        if(os.contains("sunos") || os.contains("solaris"))
        {
            return "Solaris";
        }
        if(os.contains("unix"))
        {
            return "Linux";
        }
        if(os.contains("mac"))
        {
            return "Macosx";
        }
        return "Unknown";
    }
    
    private static InputStream getFFJar(String string, Class<?> class1)
    {
        return new BufferedInputStream(class1.getResourceAsStream(string));
    }
}
