package Barramundi;

class WakeUp { 
public static void main (String a[]) {
	HFA h = new HFA("COM1",2400,false);
	h.wakeUp();
}
}
