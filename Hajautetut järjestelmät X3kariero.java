// SendX.java
// Simo Juvaste 24.1.2011
// 
// Program to send X-tasks by email (SMTP)
//
// Invoke without parameters to see parameters
//
// If you have problems using this program, please report to me
// Change debug printing to level 2, include the command line you used
// and the output of the whole program
// 
// TODO:
// - tolerance against glitches in SMPT negotiation
// - cleaner exception handling
// - support for ESMTP
// - GUI

import java.util.Scanner;
import java.util.LinkedList;
import java.io.*;
import java.net.*;


// main class
public class kariero {

    // STMP server address
    String smtpserver = null;
    int port = 55123;
    Socket sock = null;
    PrintWriter out = null;
    BufferedReader in = null;

    // receiver of the tasks
    // String receiver = "simo.juvaste@uef.fi";
    String receiver = "sjuva@cs.joensuu.fi";
    String course = "HAJ"; // course name
    String student = "";
    int task = 0;
    int maxtask = 4;
    File f;
    
    LinkedList<String> text = null;
    String fileName = "";

    // 2 to see all steps
    // 1 to see some steps
    // 0 to see no steps
    int debug = 1;

    // main
    public static void main(String[] args) {
        kariero o = new kariero();
        o.realMain(args);
    }

    // actual work
    void realMain(String[] args) {

        try {
            getParameters(args);    // command line parameters
            checkParameters();      // check thes
            readfile();             // read input file
            check();                // check from user if ok
            connect();              // TCP connect
            sendmail();             // sending the mail
            reportOK();             // final printing if ok

        } catch (Exception e) {
            System.err.println("Exception " + e);
        } finally {
            quit();
        }
    }   // realMain()

    // read parameters from command line
    void getParameters(String[] args) {

        boolean ok = true;

        if (args.length != 4)
            ok = false;
        else {

            ok = ok && getTask(args[0]);
            ok = ok && getEmail(args[1]);
            ok = ok && getFile(args[2]);
            ok = ok && getSmtp(args[3]);
        }

        if (! ok) {
            printUsage();
            throw new Xexception("Invalid parameters");
        }

    }   // getParameters()

    // print short description of program invocation
    public void printUsage() {

        System.err.println("\nUsage: java SendX # email file smtpserver");
        System.err.println("  # given in course (X#)");
        System.err.println("  email is to email you have at WebOodi or given to course");
        System.err.println("  file is your answer. Remember to have main class name as your user name");
        System.err.println("  smtpserver is the outgoing mail server of your ISP");

        System.err.println(" E.g. (from Telekarelia): java SendX 2 name@student.uef.fi name.java smtp.telemail.fi");
        System.err.println(" E.g. (from UEF network): java SendX 3 fname.lname@gmail.com fname.java cs.joensuu.fi\n");
    }

    // get and check task number
    public boolean getTask(String tn) {
        // task number
        try {
            task = Integer.valueOf(tn);
        } catch (NumberFormatException e) {
            System.err.println("Invalid task number");
            return false;
        }

        if (task < 1 || task > maxtask) {
            System.err.println("Invalid task number");
            return false;
        }
        return true;
    }

    // get and check student email address
    public boolean getEmail(String em) {

        // student email address
        student = em;
        if (student.indexOf("@") < 0) {
            System.err.println("Invalid email address");
            return true;
        }
        return true;
    }

    // get and check file name
    public boolean getFile(String fn) {

        // source file name
        fileName = fn;
        f = new File(fileName);
        
        if (! f.canRead()) {
            System.err.println("Invalid filename");
            return false;
        }
        return true;
    }

    // get and check smpt server address
    public boolean getSmtp(String ss) {

        // SMTP server to use for sending the mail
        smtpserver = ss;
        try {
            InetAddress ia = InetAddress.getByName(smtpserver);
        } catch (UnknownHostException e) {
            System.err.println("Invalid smpt server hostname");
            return false;
        }
        return true;
    }

    // let user check the parameters
    void checkParameters() {

        System.out.println("---------------------------------------------");
        System.out.println("Sending X-task");
        System.out.println("---------------------------------------------");
        System.out.println("Check following carefully:");
        System.out.println("---------------------------------------------");
        System.out.println("Destination address:" + receiver);
        System.out.println("SMTP send server:" + smtpserver);
        System.out.println("Course:" + course);
        System.out.println("Task number:" + task);
        System.out.println("Your email address:" + student);
        System.out.println("Source filename:" + fileName);
        System.out.println("Email subject:" + subject());
        System.out.println("---------------------------------------------\n");

        check();

    } // checkParameters()

    // forms the email subject
    String subject() {
        return course + "_X" + task ;
	
    }

    // read file contents to string list
    void readfile() {

        int lines = 0;

        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            text = new LinkedList<String>();
            String line;

            while ((line = input.readLine()) != null) {
                lines++;
                text.add(line);
            }

            report("Read succesfully " + lines + " lines.");
            input.close();

        } catch (Exception e) {
            throw new Xexception("File read failed : " + e);
        }
    } // readfile()


    // check user input
    void check() {
        System.out.print("Is all of above ok? (y/n) : ");

        Scanner user = new Scanner(System.in);
        String answer = user.next();

        if (answer.startsWith("y")) {
            System.out.println("Continuing\n");
            try {
                Thread.sleep(400);
            } catch (Exception e) { }
        } else {
            throw new Xexception("User canceled");
        }

    } // check()

    // TCP connection
    void connect() {

        try {
            sock = new Socket(smtpserver, port);
            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            report("Connection succeeded");

        } catch (Exception e) {
            throw new Xexception("TCP connection failed: " + e);
        }
         
    } // connect()


    // main STMP conversation with server
    void sendmail() {
    	
    	try {
    	
    	send("Task: " + subject());//korjattu
    	send("User: " + student); //korjattu
    	send("");
    	expect("2");
    	send("File: " + f.length());
    	sendFile();
    	expect("2");
    	
    	while(true)
    	{
    		String line = in.readLine();
    		if (line == null)
    		{
    			return;
    		}
    	System.out.println(line);
    	}
        
           
        } catch (Exception e) {
            throw new Xexception("Close failed: " + e);
        }

    } // sendmail()


    // send a string
    void send(String s) {
        try {
            report2("Send>" + s);
            out.println(s);
        } catch (Exception e) {
            throw new Xexception("Send failed: " + e);
        }

    }   // send()

    // receive a string and compare it to es
    void expect(String es) {

        try {
            report2("Expect>" + es);
            String rs = in.readLine();
            if (rs == null)
                throw new Xexception("Receive: connection closed");
            report2("Received>" + rs);

            if (!rs.startsWith(es)) {
                System.err.println("Unexpected return from server:");
                System.err.println("Expected:" + es);
                System.err.println("Recieved:" + rs);
                throw new Xexception("Unexpected return from server");
            }

            report2("Received ok");

        } catch (Exception e) {
            throw new Xexception("Send failed: " + e);
        }
    } // expect()

    // send the file contenst
    void sendFile() {
        try {
            for (String s : text) {
                if (s.equals("."))      // encode termination character from file
                    out.println("..");
                else
                    out.println(s);
            }
            out.println(".");   // termination character

        } catch (Exception e) {
            throw new Xexception("Sending file failed: " + e);
        }
        
    } // sendFile()


    // close sock if it is open
    void quit() {
        try {
            if (sock != null && sock.isConnected() && !sock.isClosed())
                sock.close();
        } catch (Exception e) { }

        report("SendX end");
    } // quit()

    // print if wanted
    void report(String s) {
        if (debug > 0)
            System.out.println(s);
    }   // report

    // print if wanted more
    void report2(String s) {
        if (debug > 1)
            System.out.println(s);
    }   // report

    // final words
    void reportOK() {
        System.out.println("\nSending succeeded!");
        System.out.println("There should be an acknowledgement message to address");
        System.out.println(student + " in a moment.\n");
    }

    // own exception class
    public class Xexception extends RuntimeException {
        public Xexception() {
            super();
        }
        public Xexception(String reason) {
            super(reason);
        }
        public Xexception(Throwable t) {
            super(t);
        }

        public String toString() {
            return "in SendX, quitting: " + this.getMessage();
        }
    } // class Xexception


} // class SendX

