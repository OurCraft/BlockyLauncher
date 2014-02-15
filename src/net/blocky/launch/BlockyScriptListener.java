package net.blocky.launch;

import java.io.File;

public class BlockyScriptListener implements ScriptListener
{

    @Override
    public boolean onInstruction(ScriptInstruction in)
    {
        if(in.getInstruction().equals("download"))
        {
            BlockyLauncher.barState.setValue(0);
            BlockyLauncher.barState.repaint();
            BlockyLauncher.statePanel.setVisible(true);
            File dst = new File(in.getArgs()[1]);
            BlockyLauncher.labelState.setText("Downloading "+dst.getName());
        }
        else
            BlockyLauncher.statePanel.setVisible(false);
        return true;
    }

    @Override
    public String formatArg(String string)
    {
        return string.replace("${folder}", BlockyLauncher.getFolder().getPath());
    }

    @Override
    public void onDownloaded(byte[] buffer, int bytesRead, int index, int max)
    {
        BlockyLauncher.barState.setValue((int)((float)index/(float)max*100f));
        BlockyLauncher.barState.repaint();
    }

}
