package oz.temp.number;

public class Prime {

	public static void main(String[] args) {

		long limit = 10000000;
		int i = 2;
		int numofprime = 0;
		double k;
		long t = System.currentTimeMillis();
		System.out.println("Prime numbers between 1 and " + limit);

		for (i = 1; i < limit; i++) {
			if ((i % 1000000) == 0)
				System.out.print(".");
			boolean isPrime = true;
			k = Math.sqrt(i);
			for (int j = 2; j <= k; j++) {
				if (i % j == 0) {
					isPrime = false;
					// System.out.println("non prime number: " +
					// String.valueOf(i));
					break;
				}
			}
			if (isPrime) {
				numofprime++;
				// System.out.println("prime number: " + String.valueOf(i));
			}
		}
		t = System.currentTimeMillis() - t;
		System.out.println();
		System.out.println("There are " + numofprime + " between 1 and " + limit);
		System.out.println("Total Time is: " + t);
	}
}
