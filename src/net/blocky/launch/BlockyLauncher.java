package net.blocky.launch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class BlockyLauncher
{

	public static final String RESOURCES_LINK_START = "http://jglrxavpok.minecraftforgefrance.fr/blocky/resources/";
    static File	folder;
    public static JPasswordField pass;
    public static JTextField username;
    public static JButton okButton;
    public static JLabel labelState;
    public static JPanel statePanel;
    public static BlockyProgressBar barState;
    public static JFrame frame;
    private static BlockyScriptListener scriptListener;
    private static JPanel loginPart;
    private static BlockyPanel contentPane;
    private static Properties props;
    private static JLabel loginInfosLabel;
    private static JLabel logInfos;

	public static void main(String[] args)
	{
	    try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	    props = new Properties();
	    File file = new File(System.getProperty("user.home"), "blocky.properties");
	    if(!file.exists())
            try
            {
                file.createNewFile();
            }
            catch (IOException e2)
            {
                e2.printStackTrace();
            }
	    try
        {
            props.load(new FileInputStream(file));
            String folder = props.getProperty("workFolder");
            if(folder != null)
                BlockyLauncher.folder = new File(folder);
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
	    scriptListener = new BlockyScriptListener();
	    frame = new JFrame("Blocky Launcher");
	    frame.addWindowListener(new WindowAdapter()
	    {
	        public void windowClosing(WindowEvent e)
	        {
	            saveProps();
	            System.exit(0);
	        }
	    });
	    loginPart = new JPanel();
	    loginPart.setLayout(new BorderLayout());
	    username = new JTextField(40);
	    JPanel usernamePan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    usernamePan.setOpaque(false);
	    JLabel l = new JLabel("Username: ");
	    l.setForeground(Color.white);
	    usernamePan.add(l);
	    usernamePan.add(username);
	    
	    JPanel passPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    passPan.setOpaque(false);
	    pass = new JPasswordField(40);
	    pass.addKeyListener(new KeyListener()
	    {

            @Override
            public void keyPressed(KeyEvent arg0)
            {
                if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    final String usernameS = username.getText();
                    String password = new String(pass.getPassword());
                    if(checkLoginAndAccess(usernameS, password))
                    {
                        new Thread()
                        {
                            public void run()
                            {
                                try
                                {
                                    extractAndLaunch(usernameS);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0)
            {
                
            }

            @Override
            public void keyTyped(KeyEvent arg0)
            {
                
            }
	        
	    });
	    l = new JLabel("Password: ");
	    l.setForeground(Color.white);
	    passPan.add(l);
	    passPan.add(pass);
	    
	    JPanel okPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    okPan.setOpaque(false);
	    okButton = new JButton("Connect");
	    okButton.setOpaque(false);
	    okButton.addActionListener(new ActionListener()
	    {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                final String usernameS = username.getText();
                String password = new String(pass.getPassword());
                if(checkLoginAndAccess(usernameS, password))
                {
                    new Thread()
                    {
                        public void run()
                        {
                            try
                            {
                                extractAndLaunch(usernameS);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
	    });
	    okPan.add(okButton);
	    
	    JPanel pane = new JPanel();
	    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	    loginPart.add(pane, "East");
	    pane.setOpaque(false);
	    pane.add(usernamePan);
	    pane.add(passPan);
	    pane.add(okPan);
	    
	    JPanel logInfosPane = new JPanel();
	    logInfosPane.setLayout(new BoxLayout(logInfosPane, BoxLayout.Y_AXIS));
	    loginInfosLabel = new JLabel("<html><b>Login infos</b></html>");
	    loginInfosLabel.setForeground(Color.white);
	    logInfosPane.add(loginInfosLabel);
	    logInfosPane.setOpaque(false);
	    logInfos = new JLabel("<html><u>Please enter your ids.</u></html>");
	    logInfos.setForeground(Color.white);
	    logInfosPane.add(logInfos);
	    loginPart.add(logInfosPane,"West");
	    TitledBorder border = new TitledBorder("Login");
	    border.setTitleColor(Color.white);
	    loginPart.setBorder(border);
	    loginPart.setOpaque(false);
	    
	    contentPane = new BlockyPanel();
	    contentPane.setLayout(new BorderLayout());
	    contentPane.setOpaque(true);
	    contentPane.add(loginPart, "South");
	    contentPane.setPreferredSize(new Dimension(1920/2,1012/2));
	    
	    JPanel leftPart = new JPanel();
	    leftPart.setOpaque(true);
	    final JEditorPane updates = new JEditorPane();
	    updates.setText("Fetching update data...");
	    new Thread()
	    {
	        public void run()
	        {
	            try
                {
                    updates.setPage(RESOURCES_LINK_START+"update.html");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    updates.setText(e.getClass().getCanonicalName()+": "+e.getMessage());
                    updates.setForeground(Color.white);
                }
	        }
	    }.start();
	    updates.setEditable(false);
	    updates.setBackground(new Color(0x1C1C1C));
	    updates.setBorder(null);
	    updates.setForeground(Color.white);
	    JScrollPane scroll = new JScrollPane(updates);
	    scroll.getViewport().setOpaque(false);
	    scroll.setBorder(null);
	    scroll.setViewportBorder(null);
	    updates.setOpaque(false);
	    scroll.setOpaque(false);
	    scroll.setPreferredSize(new Dimension(200,1012/2-loginPart.getPreferredSize().height-20));
	    JPanel scrollPanel = new JPanel();
	    scrollPanel.add(scroll);
	    scrollPanel.setOpaque(false);
	    border = new TitledBorder("Updates");
	    border.setTitleColor(Color.white);
	    scrollPanel.setBorder(border);
	    contentPane.add(scrollPanel, "West");
	    
	    JPanel center = new JPanel(new BorderLayout());
	    center.setOpaque(false);
	    statePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    statePanel.setOpaque(false);
	    statePanel.setVisible(false);
	    labelState = new JLabel("<Nom de fichier bidon>: ");
	    labelState.setForeground(Color.white);
	    barState = new BlockyProgressBar();
	    barState.setValue(50);
	    barState.setPreferredSize(new Dimension(username.getPreferredSize().width,20));
	    statePanel.add(labelState);
	    statePanel.add(barState);
	    center.add(statePanel,"South");
	    
	    JPanel optionsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    optionsPane.setOpaque(false);
	    JButton options = new OptionsButton("Options");
	    options.setOpaque(false);
	    optionsPane.add(options);
	    center.add(optionsPane,"North");
	    contentPane.add(center,"Center");
	    frame.setContentPane(contentPane);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);
	}
	
	protected static void saveProps()
    {
	    File file = new File(System.getProperty("user.home"), "blocky.properties");
	    try
        {
            props.store(new FileOutputStream(file), "Blocky launcher options");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected static boolean checkLoginAndAccess(String username, String password)
    {
	    ArrayList<String> keys = new ArrayList<String>();
	    ArrayList<String> values = new ArrayList<String>();
	    keys.add("username");
	    keys.add("password");
	    values.add(username);
	    values.add(password);
	    try
        {
            okButton.setEnabled(false);
            BlockyLauncher.username.setEnabled(false);
            pass.setEnabled(false);
            String response = post("http://jglrxavpok.minecraftforgefrance.fr/blocky/loginCheck.php?request=check", keys, values);
            if(response.equals("ok"))
            {
                if(post("http://jglrxavpok.minecraftforgefrance.fr/blocky/loginCheck.php?request=canPlay", keys, values).equals("1"))
                {
                    logInfos.setText("<html><u>Launching game...</u></html>");
                    return true;
                }
                else
                {
                    logInfos.setText("<html><u><font color=\"red\">This account isn't allowed to play the game.</font></u></html>");
                }
            }
            else
            {
                logInfos.setText("<html><u><font color=\"red\">"+response+"</font></u></html>");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        okButton.setEnabled(true);
        BlockyLauncher.username.setEnabled(true);
        pass.setEnabled(true);
        return false;
    }
	
	public static String post(String adress,List<String> keys,List<String> values) throws IOException
    {
        String result = "";
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try  
        {
            String data="";
            if(keys != null && values != null)
            for(int i=0;i<keys.size();i++)
            {
                if (i!=0) data += "&";
                data +=URLEncoder.encode(keys.get(i), "UTF-8")+"="+URLEncoder.encode(values.get(i), "UTF-8");
            }
            URL url = new URL(adress);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ligne;
            int i = 0;
            while ((ligne = reader.readLine()) != null)
            {
                result+=(i != 0 ? "\n" : "")+ligne;
                i++;
            }
        }
        catch (IOException e) 
        {
            throw e;
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch(Exception e)
            {
                ;
            }
            try
            {
                reader.close();
            }
            catch(Exception e)
            {
                ;
            }
        }
        return result;
    }

	private static void update()
	{
        String list = null;
        try
        {
            list = post(RESOURCES_LINK_START+"update.script", null, null);
            Script script = new Script(list);
            script.setListener(scriptListener);
            while(script.hasNext())
            {
                script.executeNext();
            }
            statePanel.setVisible(true);
            labelState.setText("Extracting natives");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
	
    public static void extractAndLaunch(final String s) throws Exception
	{
        int updateLevel = hasUpdate();
        if(updateLevel == 1)
        {
            contentPane.remove(loginPart);
            JPanel confirmUpdatePane = new JPanel();
            confirmUpdatePane.setOpaque(false);
            confirmUpdatePane.setLayout(new BoxLayout(confirmUpdatePane, BoxLayout.Y_AXIS));
            JLabel l = new JLabel("A new update is available. Download it ?");
            l.setForeground(Color.white);
            JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
            labelPane.add(l);
            labelPane.setOpaque(false);
            confirmUpdatePane.add(labelPane);
            
            JPanel confirmPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
            confirmPane.setOpaque(false);
            final JButton yes = new JButton("Accept");
            yes.setOpaque(false);
            final JButton cancel = new JButton("Cancel");
            cancel.setOpaque(false);
            confirmPane.add(yes);
            confirmPane.add(cancel);
            confirmUpdatePane.add(confirmPane);
            
            TitledBorder border = new TitledBorder("Update time!");
            border.setTitleColor(Color.white);
            confirmUpdatePane.setBorder(border);

            confirmUpdatePane.setPreferredSize(loginPart.getPreferredSize());
            contentPane.add(confirmUpdatePane, "South");
            
            contentPane.validate();
            contentPane.repaint();
            frame.repaint();
            
            yes.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    yes.setEnabled(false);
                    cancel.setEnabled(false);
                    new Thread()
                    {
                        public void run()
                        {
                            try
                            {
                                update();
                                launchGame(s);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
            
            cancel.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    try
                    {
                        launchGame(s);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(updateLevel == 2)
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        update();
                        launchGame(s);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();
        else
            launchGame(s);
	}
    
    private static void launchGame(String s) throws Exception
    {
        saveProps();
        FileInputStream in = new FileInputStream(new File(getFolder(), "launchSetup.script"));
        String str = IO.readString(in, "UTF-8");
        System.err.println(str);
        in.close();
        Script script = new Script(str);
        script.setListener(scriptListener);
        while(script.hasNext())
        {
            script.executeNext();
        }
        try
        {
            LWJGLNatives.load(getFolder().getPath()+"/natives");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        frame.dispose();
        Class.forName("org.jglrxavpok.blocky.BlockyMain").getMethod("main", new Class<?>[]
                {
                String[].class
                }).invoke(null, new Object[]{new String[]
                        {
                        s, folder.getPath()
                        }});
    }
	
    private static int hasUpdate()
    {
        File launchSetup = new File(getFolder(), "launchSetup.script");
        if(!launchSetup.exists())
            return 2;
        try
        {
            int buildNbr = Integer.parseInt(post(RESOURCES_LINK_START+"version",null,null));
            int currentVersion = -1;
            File versionFile = new File(getFolder(), "version");
            if(!versionFile.exists())
                return 2;
            else
            {
                FileInputStream in = new FileInputStream(versionFile);
                currentVersion = Integer.parseInt(IO.readString(in, "UTF-8"));
                in.close();
            }
            if(buildNbr > currentVersion)
            {
                return 1;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static void injectIntoClasspath(URL path)
	{
		try
		{
			URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[]{URL.class}); 
			addURL.setAccessible(true);
			addURL.invoke(classLoader, new Object[]{path});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static File getFolder()
	{
		if(folder == null)
		{
		    String savedPath = getProperty("workFolder");
		    if(savedPath == null)
		    {
    			String appdata = System.getenv("APPDATA");
    			if(appdata != null)
    			{
    				folder = new File(appdata, ".blocky");
    			}
    			else
    			{
    				folder = new File(System.getProperty("user.home"), ".blocky");
    			}
		    }
		    else
		    {
		        folder = new File(savedPath);
		    }
		    try
		    {
		        if(!folder.exists())folder.mkdirs();
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		    }
		}
		return folder;
	}

	public static void setProperty(String key, String value)
	{
	    props.setProperty(key,value);
	}
	
    public static String getProperty(String string)
    {
        return props.getProperty(string);
    }
    
}
