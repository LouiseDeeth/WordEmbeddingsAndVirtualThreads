package ie.atu.sw;

/**
 * Class for displaying and simulating a progress bar.
 * 
 * The progress bar
 * 
 * <p>
 * Notes:
 * </p>
 *
 * <p>
 * The progress meter is designed to show the progress of the task by printing a
 * progress bar to the console
 * </p>
 * <ul>
 * <li>The progress meter will <strong>NOT work</strong> in the Eclipse console
 * but works on Windows (DOS), Mac, and Linux terminals.</li>
 * <li>The progress bar uses the line feed character (<code>"\r"</code>) to
 * overwrite the current line, creating an animated effect.</li>
 * <li>If the variable size is greater than the terminal width, a new line
 * escape character "\n" will be automatically added and the meter won't work
 * properly.</li>
 * </ul>
 * 
 */
public class ProgressReporter {

	public static void simulateProgress() {
		System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
		int size = 100; // The size of the meter. 100 equates to 100%
		for (int i = 0; i < size; i++) { // The loop equates to a sequence of processing steps
			printProgress(i + 1, size); // After each (some) steps, update the progress meter
			try {
				Thread.sleep(10); // Slows things down so the animation is visible
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		System.out.print(ConsoleColour.RESET); // Reset the console color
	}

	/**
	 * The print progress method to show progress as words are being parsed in.
	 * 
	 * @param index The current progress step
	 * @param total The total number of steps required to complete the task
	 */
	public static void printProgress(int index, int total) {
		if (index > total)
			return; // Out of range
		int size = 50; // Must be less than console width
		char done = '█'; // Change to whatever you like.
		char todo = '░'; // Change to whatever you like.

		// Compute basic metrics for the meter
		int complete = (100 * index) / total; // Calculate percentage
		int completeLen = size * complete / 100;

		/*
		 * A StringBuilder should be used for string concatenation inside a loop.
		 * However, as the number of loop iterations is small, using the "+" operator
		 * may be more efficient as the instructions can be optimized by the compiler.
		 * Either way, the performance overhead will be marginal.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append((i < completeLen) ? done : todo);
		}
		sb.append("] ").append(complete).append("%");

		/*
		 * The line feed escape character "\r" returns the cursor to the start of the
		 * current line. Calling print(...) overwrites the existing line and creates the
		 * illusion of an animation.
		 */
		System.out.print("\r" + sb.toString());

		// Once the meter reaches its max, move to a new line.
		if (index == total)
			System.out.println();
	}
}
