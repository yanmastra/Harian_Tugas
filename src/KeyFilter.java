import java.awt.event.KeyEvent;

public class KeyFilter {
	public static boolean isNumericKey(KeyEvent key) {
		if(key.getKeyCode()>=48 && key.getKeyCode()<=57)
			return true;
		else if(key.getKeyCode()==8 || key.getKeyCode()==10)
			return true;
		else return false;
	}
	public static boolean isNumericKey(KeyEvent key, boolean arg) {
		if(arg) {
			if(isNumericKey(key) || isControlKey(key)) return true;
			else return false;
		}else {
			if(isNumericKey(key)) return true;
			else return false;
		}
	}
	public static boolean isControlKey(KeyEvent key) {
		int num = key.getKeyCode();
		if(num>=33 && num<=40) return true;
		else if(num>=16 && num<=18) return true;
		else if(num==127) return true;
		else return false;
	}
}
