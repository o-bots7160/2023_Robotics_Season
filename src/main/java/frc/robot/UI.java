package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class UI {
    
   private static final Joystick _joystick        = new Joystick(0);
   private static final Joystick _buttons1        = new Joystick(1);
   private static final Joystick _buttons2        = new Joystick(2);
   private static final Joystick _joystick2       = new Joystick(3);

   public static boolean _manualUp() {
      if (_buttons1.getRawButton(3)) {
         return true;
      }else {
         return false;
      }
      
   }
   public static boolean _manualDn() {
      if (_buttons1.getRawButton(6)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _manualIn() {
      if (_buttons1.getRawButton(7)) {
         return true;
      }else {
         return false;
      }
      
   }
   public static boolean _manualOut() {
      if (_buttons1.getRawButton(2)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _manualRotUp() {
      if (_buttons1.getRawButton(4)) {
         return true;
      }else {
         return false;
      }
   }

   public static boolean _manualRotDn() {
      if (_buttons1.getRawButton(11)) {
         return true;
      }else {
         return false;
      }
   }

   public static boolean _floor() {
      if (_buttons2.getRawButton(4)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _mid() {
      if (_buttons2.getRawButton(5)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _top() {
      if (_buttons2.getRawButton(6)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _PlacePos() {
      if (_buttons2.getRawButton(2)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _stow() {
      if (_buttons2.getRawButton(1)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _clawGrabRel() {
      if (_buttons2.getRawButton(3)) {
         return true;
      }else {
         return false;
      }
   }
   public static boolean _coneCube() {
      if (_buttons1.getRawButton(5)) {
         return true;
      }else {
         return false;
      }
   }

   public static boolean _clawIn() {
      if (_joystick2.getRawButton(3)) {
         return true;
      }else {
         return false;
      }
   }

   public static boolean _clawOut() {
      if (_joystick2.getRawButton(4)) {
         return true;
      }else {
         return false;
      }
   }

}
