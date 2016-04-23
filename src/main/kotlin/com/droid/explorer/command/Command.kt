package com.droid.explorer.command

/**
 * Created by Jonathan on 4/23/2016.
 */
interface Command<T> {

	val args: Array<String>

	operator fun invoke(action: (T) -> Any)

}