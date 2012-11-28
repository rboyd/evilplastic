import lejos.nxt.Button;
import lejos.nxt.LCD;
import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;

public class BlockPuppet
{
  public static void main(String[] args) {
    double multiplier = 3;

    LCD.drawString("right BT",0, 0);
    NXTConnection connection = null;

    if(Button.waitForAnyPress() == Button.ID_RIGHT){
      LCD.drawString("waiting for BT", 0,1 );
      connection = Bluetooth.waitForConnection();
    } else {
      LCD.drawString("waiting for USB", 0,1 );
      connection = USB.waitForConnection();
    }

    LCD.drawString("USB CONNECTED", 0,1 );
    Button.waitForAnyPress();

    DataOutputStream dataOut = connection.openDataOutputStream();
    DataInputStream dataIn = connection.openDataInputStream();

    boolean running = true;
    while (running) {
      int angle = 0;
      char motor = ' ';
      LCD.drawString("WAITING FOR READ", 0, 0);
      try {
      motor = dataIn.readChar();
      LCD.drawString("READ CHAR", 0,1 );
      angle = dataIn.readInt();
      LCD.drawString("READ INT", 0,1 );
      } catch (java.io.IOException e)
      {
      }

      //if (motor == 'F') {
        //running = false;
      //} else {
      //Motor.A.rotate((int)(angle * multiplier));
      //}

      //RegulatedMotor activeMotor = Motor.A;
      switch (motor) {
        case 'A': Motor.A.rotate((int)(angle * multiplier)); break;
        case 'B': Motor.B.rotate((int)(angle * multiplier)); break;
        case 'C': Motor.C.rotate((int)(angle * multiplier)); break;
        case 'F': running = false;
      }
    }
  }
}
