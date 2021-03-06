/*
 *    Copyright 2016 Jonathan Beaudoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.droid.explorer.command.adb

import com.droid.explorer.command.Command
import java.io.File
import java.io.IOException
import java.net.Socket

/**
 * Created by Jonathan on 4/23/2016.
 */
abstract class AdbCommand : Command {

    private val process by lazy { "\"${File("build/resources/main/com/droid/explorer/adb/adb.exe").absolutePath}\"" }

    override fun run(block: ((String) -> Unit)?) {
        try {
            before()
            val process = ProcessBuilder(process, *args).start()
            while (!isAdbServerOnline()) {
            }
            if (readOutput) {
                process.inputStream.reader().readLines().filterNot(String::isNullOrEmpty).forEach {
                    block?.invoke(it)
                }
            }
        } finally {
            after()
        }
    }

    private fun isAdbServerOnline(): Boolean {
        return try {
            Socket("localhost", 5037).close()
            true
        } catch (e: IOException) {
            false
        }
    }

}