package com.droid.explorer.command.shell.impl

import com.droid.explorer.command.shell.ShellCommand
import com.droid.explorer.controller.Entry
import com.droid.explorer.tracking.PathTracking
import java.util.*

/**
 * Created by Jonathan on 4/23/2016.
 */
class ListFiles(val directory: String, vararg extraArgs: String) : ShellCommand<Entry>() {

	override fun run() = ArrayList<Entry>().apply { output().forEach { add(PathTracking.parseEntry(it)) }}

	override val shellArgs = arrayOf("ls", *extraArgs, "\"$directory\"")

}