package com.droid.explorer

import com.droid.explorer.command.shell.impl.ListFiles


/**
 * Created by Jonathan on 4/20/2016.
 */
fun main(args: Array<String>) {
	ListFiles("/data", "-l")({println(it)})
}