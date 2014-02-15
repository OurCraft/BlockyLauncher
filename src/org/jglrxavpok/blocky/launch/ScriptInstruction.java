package org.jglrxavpok.blocky.launch;

public class ScriptInstruction
{

    private String instruction;
    private String[] args;

    public ScriptInstruction(String instruction, String[] args)
    {
        this.instruction = instruction;
        this.args = args;
    }
    
    public String getInstruction()
    {
        return instruction;
    }
    
    public String[] getArgs()
    {
        return args;
    }
}
