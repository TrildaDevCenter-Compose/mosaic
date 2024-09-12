package com.jakewharton.mosaic.terminal

public expect object Tty {
	/**
	 * Save the current terminal settings and enter "raw" mode.
	 *
	 * Raw mode is described as "input is available character by character, echoing is disabled,
	 * and all special processing of terminal input and output characters is disabled."
	 *
	 * The saved settings can be restored by calling [close][AutoCloseable.close] on
	 * the returned instance.
	 *
	 * See [`termios(3)`](https://linux.die.net/man/3/termios) for more information.
	 *
	 * In addition to the flags required for entering "raw" mode, on POSIX-compliant platforms,
	 * this function will change the standard input stream to block indefinitely until a minimum
	 * of 1 byte is available to read. This allows the reader thread to fully be suspended rather
	 * than consuming CPU. Use [stdinReader] to read in a manner that can still be interrupted.
	 */
	public fun enableRawMode(): AutoCloseable

	/**
	 * Create a [StdinReader] which will read from this process' stdin stream while also
	 * supporting interruption.
	 *
	 * Use with [enableRawMode] to read input byte-by-byte.
	 */
	public fun stdinReader(): StdinReader
}

public expect class StdinReader : AutoCloseable {
	/**
	 * Read up to [length] bytes into [buffer] at [offset]. The number of bytes read will be returned.
	 * 0 will be returned if [interrupt] is called while waiting for input. -1 will be returned if
	 * the input stream is closed.
	 */
	public fun read(buffer: ByteArray, offset: Int, length: Int): Int

	/** Signal blocking calls to [read] to wake up and return 0. */
	public fun interrupt()

	/**
	 * Free the resources associated with this reader.
	 *
	 * This call can be omitted if your process is exiting.
	 */
	override fun close()
}