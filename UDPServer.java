/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtcp;

/**
 *
 * @author promphorn
 */
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import java.io.*;
import java.net.*;

class UDPServer {

    public static void main(String args[]) throws Exception {

        String switchStatus = "false";
        int currentLine = 1;

        while (true) {
            DatagramSocket serverSocket = new DatagramSocket(9899);
            byte[] receiveData = new byte[100];
            byte[] sendData = new byte[100];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence.trim());
            serverSocket.close();

            String[] input = sentence.split(",");
            String dataType = input[0];
            String sensor = input[1];

            String ip = InetAddress.getLocalHost().getHostAddress();

            //System.out.println(i);
            InetAddress addr;
            try {

                addr = InetAddress.getByName(ip);
                OSCPortOut sender = null;
                OSCMessage msg = null;

                if (dataType.contains("switch")) {
                    switchStatus = input[2];
                    //System.out.println("switchStatus " + switchStatus);
                    if (switchStatus.contains("true")) {
                        if (currentLine == 4) {
                            currentLine = 1;
                            System.out.println("Current Line " + currentLine);
                        } else {
                            currentLine++;
                             System.out.println("Current Line " + currentLine);
                        }
                    }

                } else {
                    if (currentLine == 1) {
                        int i = (int) (Float.parseFloat(input[2]) * -100);
                        sender = new OSCPortOut(addr, 7100);
                        msg = new OSCMessage(Integer.toString(i));
                    } else if (currentLine == 2) {
                        int i = (int) (Float.parseFloat(input[2]) * -100);
                        sender = new OSCPortOut(addr, 7200);
                        msg = new OSCMessage(Integer.toString(i));
                    }
                    else if (currentLine == 3) {
                        int i = (int) (Float.parseFloat(input[2]) * -100);
                        sender = new OSCPortOut(addr, 7300);
                        msg = new OSCMessage(Integer.toString(i));
                    }
                    else if (currentLine == 4) {
                        int i = (int) (Float.parseFloat(input[2]) * -100);
                        sender = new OSCPortOut(addr, 7400);
                        msg = new OSCMessage(Integer.toString(i));
                    }else { System.out.println("wrong sensor, Couldn't send");}

                }

                try {
                    sender.send(msg);
                    sender.close();
                    System.out.println("send");
                } catch (Exception e) {
                    System.out.println("Couldn't send");
                }
            } catch (UnknownHostException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }
}
