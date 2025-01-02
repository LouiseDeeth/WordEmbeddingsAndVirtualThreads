package ie.atu.sw;

/**
 * Class for displaying and simulating a progress bar.
 * 
 * The progress meter is designed to show the progress of the task by printing a
 * progress bar to the console
 * 
 * The progress meter will <b>NOT</b> work in the Eclipse console but works on
 * Windows (DOS), Mac, and Linux terminals.
 * The progress bar uses the line feed character "\r" to overwrite the
 * current line, creating an animated effect.
 * 
 */
public class ProgressReporter {

	/**
	 * The print progress method to show progress as words are being parsed in.
	 * 
	 * @param index The current progress step
	 * @param total The total number of steps required to complete the task
	 * 
	 * O(n) as loop iterates n times
	 */
	public static void printProgress(int index, int total) {
		if (index > total)
			return; // Out of range

		int size = 50; // Must be less than console width
		char done = '█'; // Change to whatever you like.
		char todo = '░'; // Change to whatever you like.

		// Calculate the percentage of completion
		int complete = (100 * index) / total;
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
