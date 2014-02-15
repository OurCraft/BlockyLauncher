package org.jglrxavpok.blocky.launch;

public interface ScriptListener
{

    public boolean onInstruction(ScriptInstruction in);
    
    public void onDownloaded(byte[] buffer, int bytesRead, int index, int max);

    public String formatArg(String string);
}
