# SimplifyText Application
#### @author Louise Deeth
#### @version Java 20.0.1

## Description
### The SimplifyText Application is a Java-based tool designed to process text files using word embeddings and simplify their content by replacing complex words with their most similar counterparts. It utilises multithreading for efficient processing and supports user interaction through a menu-driven console interface.

## Features 
* Text Simplification: Simplifies text by finding the most similar words based on cosine similarity with Google’s 1000 most common words.
* Embeddings Parsing: Loads and parses word embeddings from a file, storing them in an efficient map structure.
* Multithreaded Processing: Utilises virtual threads for fast and parallel processing of large text files.
* Configurable File Paths: Allows users to specify input, output, and embeddings file paths.
* Progress Reporting: Displays progress during embeddings loading and text simplification.
* Customizable Console Appearance: Offers options to adjust console background and font colours.

## How It Works
* Specify file paths for the embeddings file, input text file, and output file.
*	Load word embeddings and Google’s 1000 most common words using virtual threads.
*	Process the input text file, replacing complex words with simpler alternatives.
*	Save the simplified text to the specified output file.

## To Run
*	Navigate to the directory containing the .jar file. 
*	Execute the following command: java -cp ./oop.jar ie.atu.sw.Runner
*	Use the menu-driven options:
	* Specify the embeddings file (e.g., ./word-embeddings.txt). Progress will be shown.
	* Load the Google 1000 file.
	* Specify an input file (e.g., input.txt).
	* Specify an output file (e.g., out.txt).

