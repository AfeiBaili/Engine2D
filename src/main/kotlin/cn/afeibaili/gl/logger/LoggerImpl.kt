package cn.afeibaili.gl.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * # 内部日志实现
 *
 *@author AfeiBaili
 *@version 2026/6/2 21:33
 */

internal class LoggerImpl(override val name: String) : Logger {

    override fun info(msg: Any) {
        print("I", msg)
    }

    override fun warn(msg: Any) {
        print("W", msg)
    }

    override fun error(msg: Any) {
        print("E", msg, errPrinter)
    }

    override fun debug(msg: Any) {
        print("D", msg)
    }


    private fun print(level: String, msg: Any, printer: PrintWriter = outPrinter) {
        loggerScope.launch {
            printer.println("[$level] ${getDate()} $name: $msg")
        }
    }

    private fun getDate() = LocalDateTime.now().format(formatter)

    companion object {
        val loggerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val outPrinter = PrintWriter(System.out, true)
        val errPrinter = PrintWriter(System.err, true)
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    }
}