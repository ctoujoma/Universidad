import java.util.Scanner;

public class intervals {
	public static void main(String[] args) {
		double x1, x2;
		double y1, y2;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Aquest programa comproba si dos intervals\nes interseccionen o no en la recta real.");
		System.out.print("Introdueix dos numeros (per al primer interval): ");
		x1 = sc.nextDouble();
		x2 = sc.nextDouble();
		System.out.print("Introdueix dos numeros (per al segon interval): ");
		y1 = sc.nextDouble();
		y2 = sc.nextDouble();
		
		if ((x1 < y1 && x2 < y1) || (y1 < x1 && y2 < x1)) {
			System.out.println("Els intervals NO es interseccionen.");
		} if ((x1 < y1 && y1 <= x2) || (y1 < x1 && x1 <= y2)) {
			System.out.println("Els intervals SI es interseccionen.");
		} if (x1 == y1 && x2 == y2) {
			System.out.println("Son el MATEIX intervals.");
		}
	}
}
